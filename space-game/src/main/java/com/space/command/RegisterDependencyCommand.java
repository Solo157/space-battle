package com.space.command;

import com.space.ioc.IoC;

import java.util.*;
import java.util.function.Function;

/**
 * Команда для регистрации зависимости и ее стратегии в IoC. Т.е. в текущий currentScope устанавливается новая зависимость
 * или заменяется уже существующая.
 */
public class RegisterDependencyCommand implements ICommand {

    private String dependency;
    private Function<Object[], Object> dependencyResolverStrategy;

    public RegisterDependencyCommand(String dependency, Function<Object[], Object> dependencyResolverStrategy) {
        this.dependency = dependency;
        this.dependencyResolverStrategy = dependencyResolverStrategy;
    }

    @Override
    public void execute() {
        // Предположим, что Ioc.resolve возвращает текущий слой зависимостей как Map
        Map<String, Function<Object[], Object>> currentScope = IoC.resolve("IoC.Scope.Current");
        currentScope.put(dependency, dependencyResolverStrategy);
    }

}
