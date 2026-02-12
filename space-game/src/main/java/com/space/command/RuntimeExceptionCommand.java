package com.space.command;

public class RuntimeExceptionCommand implements ICommand {

    @Override
    public void execute() {
        throw new RuntimeException();
    }

}
