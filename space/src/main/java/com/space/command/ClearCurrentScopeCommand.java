package com.space.command;

public class ClearCurrentScopeCommand implements ICommand {

    @Override
    public void execute() {
        InitCommand.currentScopes.remove();
    }

}
