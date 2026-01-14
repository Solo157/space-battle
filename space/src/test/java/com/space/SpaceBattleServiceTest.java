package com.space;

import com.space.api.dto.CommandDTO;
import com.space.entity.Point;
import com.space.entity.UObject;
import com.space.service.CommandType;
import com.space.service.SpaceBattleCrudService;
import com.space.service.SpaceBattleService;
import com.space.service.games.SpaceBattleGame1;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class SpaceBattleServiceTest {

    @Autowired
    private SpaceBattleService spaceBattleService;

    @Autowired
    private SpaceBattleCrudService spaceBattleCrudService;

    @Autowired
    private SpaceBattleGame1 spaceBattleGame1;

    @Test
    void runCommandTest() {
        // before
        UObject spaceBattleObject1 = spaceBattleCrudService.findSpaceBattleObject("objectId1");

        assertEquals(12, ((Point)spaceBattleObject1.getProperty("location")).getX());
        assertEquals(5, ((Point)spaceBattleObject1.getProperty("location")).getY());

        CommandDTO commandDTO = new CommandDTO();
        commandDTO.setCommandId(CommandType.MOVE_COMMAND.name());
        commandDTO.setGameObjectId("objectId1");
        commandDTO.setGameId("gameId1");
        commandDTO.setArgs("{\"SetVelocity\": 2}");

        // do
        spaceBattleService.runCommand(commandDTO);
        spaceBattleGame1.waitOne();

        // after
        assertEquals(7, ((Point)spaceBattleObject1.getProperty("location")).getX());
        assertEquals(10, ((Point)spaceBattleObject1.getProperty("location")).getY());
    }

}
