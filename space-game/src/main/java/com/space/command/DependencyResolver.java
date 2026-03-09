package com.space.command;

import java.util.*;
import java.util.function.Function;

/**
 * Класс разрешения зависимости. Один из возможных вариантов, т.к. является наследником IDependencyResolver.
 * В dependencies устанавливается какой-то скоуп, клиент данного класса устанавливает этот скоуп в dependencies.
 * Затем происходит сначала поиск в текущем переданном скоупе, т.е. в dependencies, если нашли, то возвращаем.
 * Если не найдено, то берем парент скоуп текущего скоупа, если он null, то кидаем исключение, если нет, то в
 * цикле while он (парент скоуп) становится currentDependencies и уже у него пытаемся найти зависимость, нашли - отдаем,
 * если нет, то смотрим уже его пареент скоуп и т.д. пока не доайдет до самого конечного/высокого парент скоупа.
 * В итоге либо, найдем зависимость, либо выбросим исключение, что парент скоуп не найден.
 */
public class DependencyResolver implements IDependencyResolver {

    /**
     * Зависимости, где будем искать переданную зависимость. По сути это переданный текущий скоуп.
     */
    private Map<String, Function<Object[], Object>> dependencies;

    public DependencyResolver(Object scope) {
        this.dependencies = (Map<String, Function<Object[], Object>>) scope;
    }

    public Object resolve(String dependency, Object[] args) {
        Map<String, Function<Object[], Object>> currentDependencies = dependencies;

        while (true) {
            Function<Object[], Object> dependencyResolverStrategy = null;
            // пытаемся найти зависимость в текущем скоупе
            if (currentDependencies.containsKey(dependency)) {
                dependencyResolverStrategy = currentDependencies.get(dependency);
                // если нашли зависимость, то вычисляем по ее стратегии, т.е. по ее функции, которая к ней относится
                return dependencyResolverStrategy.apply(args);
            }

            // в данной функции просто возвращается объект мапа (ничего не вычисляется), который был вычислен
            // в зависимости "IoC.Scope.Current" когда создавался текущий currentScope
            Function<Object[], Object> parentResolverFunc = currentDependencies.get("IoC.Scope.Parent");
            // нет больше парент скоупа, значит кидаем исключение
            if (parentResolverFunc == null) {
                throw new RuntimeException("Parent scope resolver not found");
            }

            // получаем мапу - парент скоуп и устанавливаем ее как текущие зависимости (скоуп) в котором надо искать зависимость
            Object result = parentResolverFunc.apply(args);
            currentDependencies = (Map<String, Function<Object[], Object>>) result;
        }
    }

}
