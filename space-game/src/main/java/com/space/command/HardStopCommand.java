package com.space.command;

import com.space.serverthread.ServerThread;

public class HardStopCommand implements ICommand {

    private final ServerThread serverThread;

    public HardStopCommand(ServerThread serverThread) {
        this.serverThread = serverThread;
    }

    @Override
    public void execute() {
        serverThread.setCurrentState(null);
    }
}
