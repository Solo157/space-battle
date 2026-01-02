package com.space.command;

import com.space.ioc.IoC;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
import java.util.function.Function;

public class InitCommand implements ICommand {

    public static ThreadLocal<Object> currentScopes = new ThreadLocal<>();
    public final static ConcurrentMap<String, Function<Object[], Object>> rootScope = new ConcurrentHashMap<>();
    public static boolean alreadyExecutesSuccessfully = false;

    @Override
    public void execute() {

        if (alreadyExecutesSuccessfully) {
            return;
        }

        synchronized (rootScope) {

            rootScope.put("IoC.Scope.Current.Set", args -> new SetCurrentScopeCommand(args[0]));

            rootScope.put("IoC.Scope.Current.Clear", args -> new ClearCurrentScopeCommand());

            rootScope.put("IoC.Scope.Current", args -> {
                Object scope = currentScopes.get();
                return scope != null ? scope : rootScope;
            });

            rootScope.put("IoC.Scope.Parent", args -> {
                throw new RuntimeException("The root scope has no a parent scope.");
            });

            rootScope.put("IoC.Scope.Create.Empty", args -> new ConcurrentHashMap<String, Function<Object[], Object>>());

            rootScope.put("IoC.Scope.Create", args -> {
                var creatingScope = IoC.<Map<String, Function<Object[], Object>>>resolve("IoC.Scope.Create.Empty");

                if (args.length > 0) {
                    Object parentScope = args[0];
                    creatingScope.put("IoC.Scope.Parent", innerArgs -> parentScope);
                } else {
                    Object parentScope = IoC.resolve("IoC.Scope.Current");
                    creatingScope.put("IoC.Scope.Parent", innerArgs -> parentScope);
                }
                return creatingScope;
            });

            rootScope.put("IoC.Register", args -> new RegisterDependencyCommand((String) args[0], (Function<Object[], Object>) args[1]));

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
