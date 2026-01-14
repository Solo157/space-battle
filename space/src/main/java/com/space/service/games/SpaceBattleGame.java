package com.space.service.games;

import com.space.command.ICommand;

/**
 * Интерфейс для каждой игры.
 */
public interface SpaceBattleGame {

    String getGameId();

    void addCommandForRun(ICommand iCommand);

    void waitOne();
}
