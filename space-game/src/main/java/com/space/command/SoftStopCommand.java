package com.space.command;

import com.space.serverthread.ServerThread;

public class SoftStopCommand implements ICommand {

    private final ServerThread serverThread;

    public SoftStopCommand(ServerThread serverThread) {
        this.serverThread = serverThread;
    }

    @Override
    public void execute() {
        Runnable oldBehaviour = serverThread.behaviour;

        serverThread.updateBeviour(() -> {
            if (!serverThread.queue.isEmpty()) {
                oldBehaviour.run();
            } else {
                serverThread.stop();
            }
        });
    }

}