package com.space.command;

import java.util.*;
import java.util.function.Function;

public class DependencyResolver implements IDependencyResolver {

    private Map<String, Function<Object[], Object>> dependencies;

    public DependencyResolver(Object scope) {
        this.dependencies = (Map<String, Function<Object[], Object>>) scope;
    }

    public Object resolve(String dependency, Object[] args) {
        Map<String, Function<Object[], Object>> currentDependencies = dependencies;

        while (true) {
            Function<Object[], Object> dependencyResolverStrategy = null;
            if (currentDependencies.containsKey(dependency)) {
                dependencyResolverStrategy = currentDependencies.get(dependency);
                return dependencyResolverStrategy.apply(args);
            }

            Function<Object[], Object> parentResolverFunc = currentDependencies.get("IoC.Scope.Parent");
            if (parentResolverFunc == null) {
                throw new RuntimeException("Parent scope resolver not found");
            }

            Object result = parentResolverFunc.apply(args);
            currentDependencies = (Map<String, Function<Object[], Object>>) result;
        }
    }

}
