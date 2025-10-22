package com.space.command;

import com.space.exception.exception.OneTimeRepeatICommandException;

public class RetryCommandOneTime implements ICommand {

    private ICommand iCommand;

    public RetryCommandOneTime() {
    }

    public void setCommand(ICommand command) {
        this.iCommand = command;
    }

    @Override
    public void execute() {
        try {
            iCommand.execute();
        } catch (Exception e) {
            throw new OneTimeRepeatICommandException(e.getMessage());
        }
    }

}
