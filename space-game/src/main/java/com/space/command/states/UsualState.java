package com.space.command.states;

import com.space.command.ICommand;
import com.space.ioc.IoC;
import com.space.serverthread.ServerThread;

import java.util.concurrent.BlockingQueue;

public class UsualState implements State {

    private final ServerThread serverThread;

    public UsualState(ServerThread serverThread) {
        this.serverThread = serverThread;
    }

    @Override
    public State handle() {
        BlockingQueue<ICommand> queue = serverThread.queue;

        // если очередь пустая, то вызывающий поток можно отпустить.
        if (queue.isEmpty()) {
            serverThread.actionAfterStop();
        }

        ICommand cmd = null;
        try {
            cmd = queue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        try {
            cmd.execute();
        } catch (Exception e) {
            IoC.<String>resolve("ExceptionHandler", e);
        }

        return serverThread.getCurrentState();
    }

}
