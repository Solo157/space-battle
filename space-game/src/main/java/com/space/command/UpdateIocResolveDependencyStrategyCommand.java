package com.space.command;

import com.space.ioc.IoC;

import java.util.function.BiFunction;

public class UpdateIocResolveDependencyStrategyCommand implements ICommand {

    public BiFunction<String, Object[], Object> updateIoCStrategy;

    public UpdateIocResolveDependencyStrategyCommand(
            BiFunction<String, Object[], Object> updater
    ) {
        this.updateIoCStrategy = updater;
    }

    @Override
    public void execute() {
        IoC.strategy = updateIoCStrategy;
    }

}
