package com.space.service.games;

import com.space.command.ICommand;
import com.space.event.ManualResetEvent;
import com.space.serverthread.ServerThread;
import com.space.service.NeighbourhoodSystemService;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Кастомная игра, которую может создать пользователь.
 */
public class CustomSpaceBattleGame implements SpaceBattleGame {

    /**
     * Пользователи игры.
     */
    private final List<Integer> users = new ArrayList<>();
    /**
     * Сервер игры, где выполняются команды в очереди.
     */
    private final ServerThread serverThread;
    /**
     * Идентификатор игры.
     */
    private final String gameId;

    public CustomSpaceBattleGame(List<Integer> users, String gameId) {
        BlockingQueue<ICommand> queue = new ArrayBlockingQueue<>(100);
        BlockingQueue<ICommand> moveQueue = new ArrayBlockingQueue<>(100);
        this.serverThread = new ServerThread(queue, moveQueue);
        var event = new ManualResetEvent();
        this.serverThread.setEvent(event);
        this.users.addAll(users);
        this.gameId = gameId;

        serverThread.start();
    }

    @Override
    public String getGameId() {
        return gameId;
    }

    @Override
    public List<Integer> getUsers() {
        return users;
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
