package com.space.command;

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
