package com.space.ioc;

import com.space.command.UpdateIocResolveDependencyStrategyCommand;

import java.util.function.BiFunction;
import java.util.function.Function;

public class IoC {

    public static BiFunction<String, Object[], Object> strategy = (String dependency, Object[] args) -> {
        if ("Update Ioc Resolve Dependency Strategy".equals(dependency))
        {

//            Function<BiFunction<String, Object[], Object>, BiFunction<String, Object[], Object>> objectObjectFunction = (obh) -> {
//                return (BiFunction<String, Object[], Object>) args[0];
//            };

            return new UpdateIocResolveDependencyStrategyCommand(
//                    objectObjectFunction
                    (BiFunction<String, Object[], Object>) args[0]
            );
        }
        else
        {
            throw new IllegalArgumentException("Dependency {dependency} is not found.");
        }
    };

    @SuppressWarnings("unchecked")
    public static <T> T resolve(String dependency, Object... args) {

        return (T) strategy.apply(dependency, args);

    }

}
