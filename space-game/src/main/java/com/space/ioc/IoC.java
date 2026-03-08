package com.space.ioc;

import com.space.command.UpdateIocResolveDependencyStrategyCommand;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Класс, который принимает зависимость и аргументы для поиска в стратегии зависимости.
 * По умолчанию стратегии нет, но в InitCommand при первом вызове она устанавливается.
 * Фактически при первом вызове InitCommand (инициализации) стратегия (strategy) должна вернуть команду, которая
 * установит стратегию разрешения зависимости. Стратегию можно менять на другие стратегии.
 */
public class IoC {

    public static BiFunction<String, Object[], Object> strategy = (String dependency, Object[] args) -> {
        if ("Update Ioc Resolve Dependency Strategy".equals(dependency)) {
            return new UpdateIocResolveDependencyStrategyCommand((BiFunction<String, Object[], Object>) args[0]);
        }

        throw new IllegalArgumentException("Dependency {dependency} is not found.");
    };

    @SuppressWarnings("unchecked")
    public static <T> T resolve(String dependency, Object... args) {

        return (T) strategy.apply(dependency, args);

    }

}
