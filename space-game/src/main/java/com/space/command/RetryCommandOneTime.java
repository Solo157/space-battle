package com.space.command;

public class RetryCommandOneTime implements ICommand {

    private ICommand iCommand;
    private final LogExceptionCommand logExceptionCommand ;

    public RetryCommandOneTime(LogExceptionCommand logExceptionCommand) {
        this.logExceptionCommand = logExceptionCommand;
    }

    public LogExceptionCommand getLogExceptionCommand() {
        return logExceptionCommand;
    }

    public void setCommand(ICommand command) {
        this.iCommand = command;
    }

    @Override
    public void execute() {
        try {
            iCommand.execute();
        } catch (Exception e) {
            logExceptionCommand.execute();
        }
    }

}
