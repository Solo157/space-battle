package com.space.command;

/**
 * Команда для установки нового скоупа, как текущего скоупа для потока, который работает с этим текущим скоупом.
 */
public class SetCurrentScopeCommand implements ICommand {

    private Object scope;

    public SetCurrentScopeCommand(Object scope) {
        this.scope = scope;
    }

    @Override
    public void execute() {
        InitCommand.currentScopes.set(scope);
    }

}
