package com.space.exception.hendler;

import com.space.command.ICommand;
import com.space.command.LogExceptionCommand;
import com.space.command.RetryCommandTwoTime;
import com.space.exception.exception.TowTimesRepeatICommandException;

import java.util.function.BiFunction;

public class MoveStaticObjectExceptionHandler implements BiFunction<ICommand, Exception, ICommand> {

    private final LogExceptionCommand logExceptionCommand;
    private final RetryCommandTwoTime retryCommandTwoTime;

    public MoveStaticObjectExceptionHandler(LogExceptionCommand logExceptionCommand,
                                           RetryCommandTwoTime retryCommandTwoTime) {
        this.logExceptionCommand = logExceptionCommand;
        this.retryCommandTwoTime = retryCommandTwoTime;
    }

    @Override
    public ICommand apply(ICommand command, Exception e) {
        logExceptionCommand.setMessage(e.getMessage());
        retryCommandTwoTime.setCommand(command);

        try {
            retryCommandTwoTime.execute();
        } catch (TowTimesRepeatICommandException exception) {
            logExceptionCommand.execute();
        }

        return null;
    }

}
