package com.space.command;

import com.space.serverthread.ServerThread;

public class SetHookAfterStopCommand implements ICommand {

    private ServerThread st;

    public SetHookAfterStopCommand(ServerThread st) {
        this.st = st;
    }

    @Override
    public void execute() {
        st.actionAfterStop();
    }
}
