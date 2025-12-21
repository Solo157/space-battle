package com.space;

import com.space.command.ICommand;
import com.space.command.InitCommand;
import com.space.command.UpdateIocResolveDependencyStrategyCommand;
import com.space.ioc.IoC;

import java.util.function.BiFunction;

public class ScopeUtils {
    private ScopeUtils() {
    }

    public static void setUp() {
        reset();

        new InitCommand().execute();
        var iocScope = IoC.<Object>resolve("IoC.Scope.Create");
        IoC.<ICommand>resolve("IoC.Scope.Current.Set", iocScope).execute();
    }

    /**
     * Сброс всех скоупов.
     */
    public static void reset() {
        InitCommand.currentScopes = new ThreadLocal<>();
        InitCommand.rootScope.clear();
        InitCommand.alreadyExecutesSuccessfully = false;
    }

    /**
     * Установить дефолтную стратегию. Используется только для тестов.
     */
    public static void setDefaultStrategy() {
        IoC.strategy = (String dependency, Object[] args) -> {
            if ("Update Ioc Resolve Dependency Strategy".equals(dependency))
            {

                return new UpdateIocResolveDependencyStrategyCommand((BiFunction<String, Object[], Object>) args[0]);
            }
            else
            {
                throw new IllegalArgumentException("Dependency {dependency} is not found.");
            }
        };
    }

}
