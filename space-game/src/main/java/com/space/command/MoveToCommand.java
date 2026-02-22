package com.space.command;

import com.space.command.states.MoveToState;
import com.space.serverthread.ServerThread;

public class MoveToCommand implements ICommand {

    private ServerThread serverThread;

    public MoveToCommand(ServerThread serverThread) {
        this.serverThread = serverThread;
    }


    @Override
    public void execute() {
        serverThread.setCurrentState(new MoveToState(serverThread));
    }

}
