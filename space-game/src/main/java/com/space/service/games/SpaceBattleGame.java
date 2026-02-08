package com.space.service.games;

import com.space.command.ICommand;

import java.util.*;

/**
 * Интерфейс для каждой игры.
 */
public interface SpaceBattleGame {

    String getGameId();

    List<Integer> getUsers();

    void addCommandForRun(ICommand iCommand);

    void waitOne();
}
