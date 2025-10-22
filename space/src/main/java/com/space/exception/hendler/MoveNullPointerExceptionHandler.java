package com.space.exception.hendler;

import com.space.command.ICommand;
import com.space.command.LogExceptionCommand;
import com.space.command.RetryCommandOneTime;
import com.space.exception.exception.OneTimeRepeatICommandException;

import java.util.function.BiFunction;

public class MoveNullPointerExceptionHandler implements BiFunction<ICommand, Exception, ICommand> {

    private final LogExceptionCommand logExceptionCommand;
    private final RetryCommandOneTime retryCommandOneTime;

    public MoveNullPointerExceptionHandler(LogExceptionCommand logExceptionCommand,
                                           RetryCommandOneTime retryCommandOneTime) {
        this.logExceptionCommand = logExceptionCommand;
        this.retryCommandOneTime = retryCommandOneTime;
    }

    @Override
    public ICommand apply(ICommand command, Exception e) {

        logExceptionCommand.setMessage(e.getMessage());
        retryCommandOneTime.setCommand(command);

        try {
            retryCommandOneTime.execute();
        } catch (OneTimeRepeatICommandException exception) {
            logExceptionCommand.execute();
        }

        return null;
    }

}
