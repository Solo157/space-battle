package com.space.command;

import com.space.exception.exception.TowTimesRepeatICommandException;

public class RetryCommandTwoTime implements ICommand {

    private ICommand iCommand;
    private int executeFailCount;

    public RetryCommandTwoTime() {
    }

    public void setCommand(ICommand command) {
        this.iCommand = command;
    }

    @Override
    public void execute() {
        boolean repeat = true;

        while (repeat) {
            try {
                iCommand.execute();
                repeat = false;
            } catch (Exception e) {
                if (++executeFailCount == 2) {
                    throw new TowTimesRepeatICommandException(e.getMessage());
                }
            }
        }

    }

}
