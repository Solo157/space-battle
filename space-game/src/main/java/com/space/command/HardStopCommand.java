package com.space.command;

import com.space.serverthread.ServerThread;

public class HardStopCommand implements ICommand {

    private final ServerThread st;

    public HardStopCommand(ServerThread serverThread) {
        this.st = serverThread;
    }

    @Override
    public void execute() {
        st.stop();
    }
}
