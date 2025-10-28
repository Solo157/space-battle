package com.space.exception.hendler;

import com.space.command.ICommand;
import com.space.command.RetryCommandTwoTime;

import java.util.function.BiFunction;

public class MoveStaticObjectExceptionHandler implements BiFunction<ICommand, Exception, ICommand> {

    private final RetryCommandTwoTime retryCommandTwoTime;

    public MoveStaticObjectExceptionHandler(RetryCommandTwoTime retryCommandTwoTime) {
        this.retryCommandTwoTime = retryCommandTwoTime;
    }

    @Override
    public ICommand apply(ICommand command, Exception e) {
        retryCommandTwoTime.setCommand(command);
        retryCommandTwoTime.getLogExceptionCommand().setMessage(e.getMessage());
        return retryCommandTwoTime;
    }

}
