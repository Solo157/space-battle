package com.space.command;

import com.space.command.states.UsualState;
import com.space.serverthread.ServerThread;

public class RunCommand implements ICommand {

    private ServerThread serverThread;

    public RunCommand(ServerThread serverThread) {
        this.serverThread = serverThread;
    }

    @Override
    public void execute() {
        serverThread.setCurrentState(new UsualState(serverThread));
    }

}
