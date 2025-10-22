package com.space.command;

public class RetryCommandTwoTime implements ICommand {

    private ICommand iCommand;
    private int executeFailCount;
    private final LogExceptionCommand logExceptionCommand ;

    public RetryCommandTwoTime(LogExceptionCommand logExceptionCommand) {
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
        boolean repeat = true;

        while (repeat) {
            try {
                iCommand.execute();
                repeat = false;
            } catch (Exception e) {
                if (++executeFailCount == 2) {
                    logExceptionCommand.execute();
                    repeat = false;
                }
            }
        }

    }

}
