package com.space.service;

import com.space.api.dto.CommandDTO;
import com.space.command.*;
import com.space.entity.UObject;
import com.space.ioc.IoC;
import com.space.service.games.CustomSpaceBattleGame;
import com.space.service.games.SpaceBattleGame;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Севрис по выполнению команд в рамках какой-либо игры.
 */
@Service
@RequiredArgsConstructor
public class SpaceBattleService {

    private final SpaceBattleCrudService spaceBattleCrudService;
    private final NeighbourhoodSystemService neighbourhoodSystemService;
    private final UserScopeService userScopeService;

    private Map<String, SpaceBattleGame> gamesMap = new HashMap<>();

    @Autowired
    public SpaceBattleService(SpaceBattleCrudService spaceBattleCrudService,
                              NeighbourhoodSystemService neighbourhoodSystemService, UserScopeService userScopeService,
                              List<SpaceBattleGame> spaceBattleGames) {
        this.spaceBattleCrudService = spaceBattleCrudService;
        this.neighbourhoodSystemService = neighbourhoodSystemService;
        this.userScopeService = userScopeService;
        this.gamesMap = spaceBattleGames.stream()
                .collect(Collectors.toMap(
                        SpaceBattleGame::getGameId,
                        Function.identity()
                ));
    }

    public UObject getGameObject(String id) {
        return spaceBattleCrudService.findSpaceBattleObject(id);
    }

    /**
     * Метод выполняет/обрабатывает входящий ордер от пользователя. Подразумевается, что пользователь на UI отправляет
     * json, затем он конвертируется в ордер и отправляется в данный метод.
     */
    public void runOrder(UObject order) {
        String action = (String) order.getProperty("action");
        String gameId = (String) order.getProperty("gameId");
        String objectId = (String) order.getProperty("objectId");
        String userId = (String) order.getProperty("player");

        // при выполнении ордера устанавливаем в IoC скоуп конкретного пользователя, чтобы посмотреть зависимости,
        // которые имеются у пользователя/игрока.
        Object userScope = userScopeService.getUserScope(userId);
        IoC.<ICommand>resolve("IoC.Scope.Current.Set", userScope).execute();

        // в текущий выбранный скоуп отправляется objectId - идентификатор игрового объекта. Заранее в скоуп зарегистированы
        // все идентификаторы объектов, если objectId не будет найден, то выбросится исключение
        IoC.<Object>resolve(objectId);
        // в текущий выбранный скоуп отправляется action-название команды, чтобы проверить, сможем ли получить по этому
        // action команду и скоупа игрока, если можем - команда разрешена, если нет - запрещена (выбросится исключение)
        IoC.<ICommand>resolve(action, userId);

        SpaceBattleGame game = gamesMap.get(gameId);
        if (game == null) {
            throw new RuntimeException("There isn't game for gameId" + gameId);
        }

        OrderInterpretCommand interpretCommand = new OrderInterpretCommand(order);
        game.addCommandForRun(interpretCommand);

        game.waitOne();
    }

    /**
     * Выполнить команду на основе входящего DTO. Возможно, метод стоит убрать, т.к. появился метод по обработке ордеров.
     */
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

        game.waitOne();
    }

    /**
     * Создать игру. Также и создается система окрестностей для игры.
     */
    public void createGame(List<Integer> users, String gameId) {
        CustomSpaceBattleGame customSpaceBattleGame = new CustomSpaceBattleGame(users, gameId);
        gamesMap.put(gameId, customSpaceBattleGame);
        neighbourhoodSystemService.createSystem(gameId);
    }

}
