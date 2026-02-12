package com.space.exception.hendler;

import com.space.command.ICommand;
import com.space.command.RetryCommandOneTime;

import java.util.function.BiFunction;

public class MoveNullPointerExceptionHandler implements BiFunction<ICommand, Exception, ICommand> {

    private final RetryCommandOneTime retryCommandOneTime;

    public MoveNullPointerExceptionHandler(RetryCommandOneTime retryCommandOneTime) {
        this.retryCommandOneTime = retryCommandOneTime;
    }

    @Override
    public ICommand apply(ICommand command, Exception e) {
        retryCommandOneTime.setCommand(command);
        retryCommandOneTime.getLogExceptionCommand().setMessage(e.getMessage());
        return retryCommandOneTime;
    }

}
