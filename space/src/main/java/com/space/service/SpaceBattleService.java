package com.space.service;

import com.space.api.dto.CommandDTO;
import com.space.command.InterpretCommand;
import com.space.service.games.SpaceBattleGame;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Севрис по выполнению команд в рамках какой-либо игры.
 */
@Service
@RequiredArgsConstructor
public class SpaceBattleService {

    private Map<String, SpaceBattleGame> gamesMap = new HashMap<>();

    @Autowired
    public SpaceBattleService(List<SpaceBattleGame> spaceBattleGames) {
        this.gamesMap = spaceBattleGames.stream()
                .collect(Collectors.toMap(
                        SpaceBattleGame::getGameId,
                        Function.identity()
                ));
    }

    public void runCommand(CommandDTO commandDTO) {
        if (commandDTO == null) {
            return;
        }

        String gameId = commandDTO.getGameId();
        SpaceBattleGame game = gamesMap.get(gameId);
        if (game == null) {
            throw new RuntimeException("There isn't game for gameId" + gameId);
        }

        InterpretCommand interpretCommand = new InterpretCommand(commandDTO);
        game.addCommandForRun(interpretCommand);

//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
    }

}
