package com.space.command;

public class LogExceptionCommand implements ICommand {

    private String exceptionMessage;

    public LogExceptionCommand(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public LogExceptionCommand() {
    }

    public void setMessage(String message) {
        this.exceptionMessage = message;
    }

    @Override
    public void execute() {
        System.out.println("LOG: " + exceptionMessage);
    }

}
