package com.space.command;

public class PrintCommand implements ICommand {
    @Override
    public void execute() {
        System.out.println("PrintCommand");
    }
}
