package com.space.command;

import com.space.ioc.IoC;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
import java.util.function.Function;

public class InitCommand implements ICommand {

    /**
     * Текущий скоуп зависимостей для конкретного потока. Скоуп - мапа с ключами-зависимостями, значения-стратегия
     * разрешения этой зависимости:
     * Map<String, Function<Object[], Object>>
     */
    public static ThreadLocal<Object> currentScopes = new ThreadLocal<>();
    /**
     * Рутовый, вышестоящий скоуп для currentScope-а. Т.е. сначала создается данный скоуп с зависимостями
     * IoC.Scope.Current.Set, IoC.Scope.Current.Clear и т.д.
     * Данный скоуп может устанавливается как parentScope для currentScope, если не задан конкретный parentScope.
     */
    public final static ConcurrentMap<String, Function<Object[], Object>> rootScope = new ConcurrentHashMap<>();
    /**
     * Флаг, определяющий, что какой-то тред уже активировал IoC и другим потока не нужно его активировать.
     * Т.е. в IoC уже созданы рутовые зависимости и его можно использовать.
     */
    public static boolean alreadyExecutesSuccessfully = false;

    @Override
    public void execute() {

        synchronized (rootScope) {

            if (alreadyExecutesSuccessfully) {
                return;
            }

            /*
             * Регистрирует в рутовом scope зависимость по установке текущего скоупа. Текущий скоуп просто передается
             * аргументом и устанавливается, как currentScope для конкретного потока, который это вызовет.
             * Поэтому currentScope можно менять для текущего потока и работать с разными скоупами.
             */
            rootScope.put("IoC.Scope.Current.Set", args -> new SetCurrentScopeCommand(args[0]));

            /*
             * Удалить текущий скоуп из currentScope.
             */
            rootScope.put("IoC.Scope.Current.Clear", args -> new ClearCurrentScopeCommand());

            /*
             * Регистрирует зависимость по получению текущего скоуп. Если текущий установлен, то возвращаем его, если
             * нет, то возвращаем рутовый скоуп. Текущий возвращается только для потока, за которым закреплен этот скоуп.
             */
            rootScope.put("IoC.Scope.Current", args -> {
                Object scope = currentScopes.get();
                return scope != null ? scope : rootScope;
            });

            /*
             * Особый парент скоуп, который есть у текущего currentScope.
             */
            rootScope.put("IoC.Scope.Parent", args -> {
                throw new RuntimeException("The root scope has no a parent scope.");
            });

            /*
             * Создание пустого скоупа в виде канкарент мапы.
             */
            rootScope.put("IoC.Scope.Create.Empty", args -> new ConcurrentHashMap<String, Function<Object[], Object>>());

            /*
             * Создание нового текущего currentScope. Если в аргументах передан какой-то парент scope, то он станет
             * parentScope для создаваемого currentScope. Если не передан, то парент parentScope для currentScope
             * будет вычислен из зависимости IoC.Scope.Current, которая определит, что парент скоупом может быть либо
             * текущий currentScope, либо рутовый rootScope (при нем выбрасывается исключение).
             * Важно. Если постоянно вызывать данную зависимость и создавать текущий скоуп, то предыдущий текущий скоуп
             * станет парент скоупом для создаваемого. Затем, если еще раз создавать новый, то предыдущий новый станет
             * парент скоупом.. тем самым получаем некую иерархию, из слоев скоупов.
             * A - текущий скоуп
             * B - парент скоуп
             * создаем новыый скоуп C
             * C - текущий скоуп
             * A - парент скоуп
             * B - парент скоуп скоупа A
             * и т.д.
             * Также важно, что иерархия может строиться только, если создание новых скоупов происходит в одном и том
             * же потоке, иначе просто будет создаваться новый скоуп у которого парент скоуп будет rootScope.
             */
            rootScope.put("IoC.Scope.Create", args -> {
                var creatingScope = IoC.<Map<String, Function<Object[], Object>>>resolve("IoC.Scope.Create.Empty");

                if (args.length > 0) {
                    // можно установить свой кастомный парент скоуп
                    Object parentScope = args[0];
                    creatingScope.put("IoC.Scope.Parent", innerArgs -> parentScope);
                } else {
                    // находим текущий currentScope. фактически искаться будет в rootScope, вычислит значение функции
                    // и возвратит не функцию, а мапу - скоуп и уже эту мапу кладем в функцию для ключа "IoC.Scope.Parent"
                    Object parentScope = IoC.resolve("IoC.Scope.Current");
                    creatingScope.put("IoC.Scope.Parent", innerArgs -> parentScope);
                }
                return creatingScope;
            });

            /*
             * Регистрация зависимости в текущий скоуп currentScope.
             */
            rootScope.put("IoC.Register", args -> new RegisterDependencyCommand((String) args[0], (Function<Object[], Object>) args[1]));

            /*
             * Устанавливаем стратегию разрешения зависимости. Т.е. при получении зависимости dependency и аргументов в
             * виде args, нужно понимать с каким скоупом будем работать, где будем искать эту зависимость и какой класс
             * будет отвечать за поиск зависимости, всей этой логики. Первый поток, который инициирует данный класс
             * устанавливает данную стратегию разрешения зависимости.
             */
            IoC.<ICommand>resolve("Update Ioc Resolve Dependency Strategy",
                    (BiFunction<String, Object[], Object>) (depName, args) -> {
                        if (depName == null || depName.isEmpty()) {
                            return null;
                        }

                        Object scopeObj = currentScopes.get();
                        Object scope = scopeObj != null ? scopeObj : rootScope;

                        DependencyResolver resolver = new DependencyResolver((Map<String, Function<Object[], Object>>) scope);
                        return resolver.resolve(depName, args);
                    }).execute();

            alreadyExecutesSuccessfully = true;
        }
    }

}
