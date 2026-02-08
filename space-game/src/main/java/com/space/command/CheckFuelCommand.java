package com.space.command;

import com.space.exception.exception.CommandException;
import com.space.adapter.ICheckFuelObject;

/**
 * Проверяет, что топлива достаточно, если нет, то выбрасывает исключение CommandException.
 */
public class CheckFuelCommand implements ICommand {

    private final ICheckFuelObject object;

    public CheckFuelCommand(ICheckFuelObject object) {
        this.object = object;
    }

    @Override
    public void execute() {
        int fuel = object.getFuel();

        if (fuel <= 0) {
            throw new CommandException("Not enough fuel");
        }

    }

}
