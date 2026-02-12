package com.space.command;

import java.util.*;

public class MoveMacroCommand implements ICommand {

    private final List<ICommand> iCommands;

    public MoveMacroCommand(List<ICommand> iCommands) {
        this.iCommands = iCommands;
    }

    @Override
    public void execute() {
        iCommands.forEach(ICommand::execute);
    }

}
