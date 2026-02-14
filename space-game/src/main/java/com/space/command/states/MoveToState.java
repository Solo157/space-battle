package com.space.command.states;

import com.space.command.HardStopCommand;
import com.space.command.ICommand;
import com.space.command.RunCommand;
import com.space.serverthread.ServerThread;

import java.util.concurrent.BlockingQueue;

public class MoveToState implements State {

    private final ServerThread serverThread;

    public MoveToState(ServerThread serverThread) {
        this.serverThread = serverThread;
    }

    @Override
    public State handle() {
        BlockingQueue<ICommand> queue = serverThread.queue;
        BlockingQueue<ICommand> moveQueue = serverThread.moveQueue;

        if (queue.isEmpty()) {
            serverThread.actionAfterStop();
        }

        ICommand cmd = null;
        try {
            cmd = queue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // есть некоторые команды, которые нужно выполнять и не складывать в очередь
        // При жестком стопе и при переключении состояния
        if (cmd instanceof HardStopCommand || cmd instanceof RunCommand) {
            cmd.execute();
            return serverThread.getCurrentState();
        }

        moveQueue.add(cmd);

        return serverThread.getCurrentState();
    }

}
