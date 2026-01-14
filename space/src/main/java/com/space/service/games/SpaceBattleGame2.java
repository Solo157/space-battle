package com.space.service.games;

import com.space.command.ICommand;
import com.space.event.ManualResetEvent;
import com.space.serverthread.ServerThread;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Игра gameId1 в которой могут выполняться команды.
 */
@Component
public class SpaceBattleGame2 implements SpaceBattleGame {

    private final ServerThread serverThread;

    public SpaceBattleGame2() {
        BlockingQueue<ICommand> q = new ArrayBlockingQueue<>(100);
        this.serverThread = new ServerThread(q);

        serverThread.start();
    }

    @Override
    public String getGameId() {
        return "gameId2";
    }

    @Override
    public void addCommandForRun(ICommand iCommand) {
        this.serverThread.queue.add(iCommand);
    }

    @Override
    public void waitOne() {
        ManualResetEvent event = this.serverThread.getEvent();
        if (event == null) {
            return;
        }

        try {
            event.waitOne();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
