package com.space;

import com.space.adapter.MovingObjectAdapter;
import com.space.api.dto.CommandDTO;
import com.space.command.CollisionCheckCommand;
import com.space.command.CollisionMacroCommand;
import com.space.command.MoveCommand;
import com.space.entity.NeighbourhoodSystem;
import com.space.entity.Point;
import com.space.entity.UObject;
import com.space.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тестирование системы окрестностей для игры.
 */
@SpringBootTest
public class NeighbourhoodSystemTest {

    @Autowired
    private SpaceBattleService spaceBattleService;

    @Autowired
    private SpaceBattleCrudService spaceBattleCrudService;

    @Autowired
    private NeighbourhoodSystemService neighbourhoodSystemService;

    /**
     * Тестирует корректность определения принадлежности объектов к определённым окрестностям
     * после их размещения и перемещения в системе окрестностей.
     */
    @Test
    public void shouldCorrectlyIdentifyObjectPlacementInNeighbourhoods() {
        spaceBattleCrudService.resetDefaultCrudService();

        // Получение объектов
        String objId3 = "objectId3";
        String objId4 = "objectId4";
        String gameId = UUID.randomUUID().toString();
        Integer user = 1;
        UObject battleObject3 = spaceBattleCrudService.findSpaceBattleObject(objId3);
        UObject battleObject4 = spaceBattleCrudService.findSpaceBattleObject(objId4);

        // Создаем игру
        spaceBattleService.createGame(List.of(user), gameId);

        // Получение системы окрестностей и сами окрестности
        List<NeighbourhoodSystem> systems = neighbourhoodSystemService.getNeighbourhoodSystemByGameId(gameId);
        NeighbourhoodSystem.Neighbourhood neighbourhood1 = systems.get(0).getNeighborhoods().get(0);
        NeighbourhoodSystem.Neighbourhood neighbourhood2 = systems.get(0).getNeighborhoods().get(1);

        // Распределяем объекты по конкретным окрестностям (квадратам)
        neighbourhood1.getUObjectMap().put(objId3, battleObject3);
        neighbourhood2.getUObjectMap().put(objId4, battleObject4);

        // Проверяем размещение
        assertEquals(1, neighbourhood1.getUObjectMap().size());
        assertEquals(1, neighbourhood2.getUObjectMap().size());
        // далее проверяем, что объекты находится на нужных окрестностях
        assertTrue(neighbourhood1.isObjectInsideNeighbourhoodPart(battleObject3));
        assertFalse(neighbourhood1.isObjectInsideNeighbourhoodPart(battleObject4));
        assertFalse(neighbourhood2.isObjectInsideNeighbourhoodPart(battleObject3));
        assertTrue(neighbourhood2.isObjectInsideNeighbourhoodPart(battleObject4));

        // Проверка макрокоманд, что они пустые
        assertEmptyCommands(battleObject3);
        assertEmptyCommands(battleObject4);

        // Перемещение объекта 3
        new MoveCommand(new MovingObjectAdapter(battleObject3)).execute();

        // Проверка нового местоположения
        Point location = (Point) battleObject3.getProperty("location");
        assertEquals(12, location.getX());
        assertEquals(-5, location.getY());

        // Запуск команды обновления макрокоманды
        CommandDTO cmdDto = new CommandDTO();
        cmdDto.setCommandId(CommandType.UPDATE_AND_CHECK_COLLISION_COMMAND.name());
        cmdDto.setGameObjectId(objId3);
        cmdDto.setGameId(gameId);
        spaceBattleService.runCommand(cmdDto);

        CollisionMacroCommand macroCommand = (CollisionMacroCommand) battleObject3.getProperty("macroCommand");
        macroCommand.execute();
        CollisionCheckCommand checkCommand = (CollisionCheckCommand) macroCommand.getCollisionCheckCommands().get(0);
        // Проверяем, что коллизий нет
        assertFalse(checkCommand.isCollide());

        // Проверка изменений в системах
        assertEquals(0, neighbourhood1.getUObjectMap().size());
        assertEquals(2, neighbourhood2.getUObjectMap().size());
        // далее проверяем, что объекты находится на нужных окрестностях
        assertFalse(neighbourhood1.isObjectInsideNeighbourhoodPart(battleObject3));
        assertFalse(neighbourhood1.isObjectInsideNeighbourhoodPart(battleObject4));
        assertTrue(neighbourhood2.isObjectInsideNeighbourhoodPart(battleObject3));
        assertTrue(neighbourhood2.isObjectInsideNeighbourhoodPart(battleObject4));

        // Проверка команд в макрокомандах объектов
        CollisionMacroCommand object3MacroCommand = (CollisionMacroCommand) battleObject3.getProperty("macroCommand");
        CollisionMacroCommand object4MacroCommand = (CollisionMacroCommand) battleObject4.getProperty("macroCommand");
        assertEquals(1, object3MacroCommand.getCollisionCheckCommands().size());
        assertEquals(0, object4MacroCommand.getCollisionCheckCommands().size());
    }

    // Вспомогательный метод для проверки отсутствия команд
    private void assertEmptyCommands(UObject obj) {
        CollisionMacroCommand macro = (CollisionMacroCommand) obj.getProperty("macroCommand");
        assertNotNull(macro);
        assertTrue(macro.getCollisionCheckCommands().isEmpty());
    }

    /**
     * Проверяет, обнаруживается ли коллизия при перемещении объекта в занятый участок окрестности,
     * что должно привести к срабатыванию collision.
     * 2 объекта в разных окрестностях, один сдвигается в окрестность, где находится другой объект и происходит коллизия.
     */
    @Test
    public void shouldDetectCollisionWhenObjectMovesIntoOccupiedNeighbourhood() {
        spaceBattleCrudService.resetDefaultCrudService();

        // Получение объектов
        String objId3 = "objectId3";
        String objId7 = "objectId7";
        String gameId = UUID.randomUUID().toString();
        Integer user = 1;
        UObject battleObject3 = spaceBattleCrudService.findSpaceBattleObject(objId3);
        UObject battleObject7 = spaceBattleCrudService.findSpaceBattleObject(objId7);

        // Создаем игру
        spaceBattleService.createGame(List.of(user), gameId);

        // Получение систем окрестностей и их конкретных окрестностей
        List<NeighbourhoodSystem> systems = neighbourhoodSystemService.getNeighbourhoodSystemByGameId(gameId);
        NeighbourhoodSystem.Neighbourhood neighbourhood1 = systems.get(0).getNeighborhoods().get(0);
        NeighbourhoodSystem.Neighbourhood neighbourhood2 = systems.get(0).getNeighborhoods().get(1);

        // Распределяем объекты по частям
        neighbourhood1.getUObjectMap().put(objId3, battleObject3);
        neighbourhood2.getUObjectMap().put(objId7, battleObject7);

        // Проверяем размещение
        assertEquals(1, neighbourhood1.getUObjectMap().size());
        assertEquals(1, neighbourhood2.getUObjectMap().size());
        // далее проверяем, что объекты находится на нужных окрестностях
        assertTrue(neighbourhood1.isObjectInsideNeighbourhoodPart(battleObject3));
        assertFalse(neighbourhood1.isObjectInsideNeighbourhoodPart(battleObject7));
        assertFalse(neighbourhood2.isObjectInsideNeighbourhoodPart(battleObject3));
        assertTrue(neighbourhood2.isObjectInsideNeighbourhoodPart(battleObject7));

        // Проверка макрокоманд, что они пустые
        assertEmptyCommands(battleObject3);
        assertEmptyCommands(battleObject7);

        // Перемещение объекта 3
        new MoveCommand(new MovingObjectAdapter(battleObject3)).execute();

        // Проверка нового местоположения
        Point object3location = (Point) battleObject3.getProperty("location");
        assertEquals(12, object3location.getX());
        assertEquals(-5, object3location.getY());
        Point object4location = (Point) battleObject7.getProperty("location");
        assertEquals(12, object4location.getX());
        assertEquals(-5, object4location.getY());

        // Запуск команды обновления макрокоманды
        CommandDTO cmdDto = new CommandDTO();
        cmdDto.setCommandId(CommandType.UPDATE_AND_CHECK_COLLISION_COMMAND.name());
        cmdDto.setGameObjectId(objId3);
        cmdDto.setGameId(gameId);
        spaceBattleService.runCommand(cmdDto);

        CollisionMacroCommand macroCommand = (CollisionMacroCommand) battleObject3.getProperty("macroCommand");
        macroCommand.execute();

        CollisionCheckCommand checkCommand = (CollisionCheckCommand) macroCommand.getCollisionCheckCommands().get(0);
        // Проверяем, что коллизия есть
        assertTrue(checkCommand.isCollide());
    }

    /**
     * Тестирует, что объект, перемещённый внутри одной части системы окрестностей, остаётся в той же части,
     * и его состояние корректно обновляется.
     * 1 объект в первой окрестности, 2 объект в этой же окрестности и также в 3 окрестности.
     * 1 объект смещается в 3 окрестность, но не выходит из 1 окрестности.
     */
    @Test
    public void shouldMaintainObjectInOriginalNeighbourhoodWhenMovingWithinSamePart() {
        spaceBattleCrudService.resetDefaultCrudService();

        // Получение объектов
        String objId5 = "objectId5";
        String objId6 = "objectId6";
        String gameId = UUID.randomUUID().toString();
        Integer user = 1;
        UObject battleObject5 = spaceBattleCrudService.findSpaceBattleObject(objId5);
        UObject battleObject6 = spaceBattleCrudService.findSpaceBattleObject(objId6);

        // Создаем игру
        spaceBattleService.createGame(List.of(user), gameId);

        // Получение систем окрестностей и их конкретных окрестностей
        List<NeighbourhoodSystem> systems = neighbourhoodSystemService.getNeighbourhoodSystemByGameId(gameId);
        NeighbourhoodSystem.Neighbourhood neighbourhood1 = systems.get(0).getNeighborhoods().get(0);
        NeighbourhoodSystem.Neighbourhood neighbourhood2 = systems.get(1).getNeighborhoods().get(0);

        // Распределяем объекты по окрестностям разных систем окрестностей
        neighbourhood1.getUObjectMap().put(objId5, battleObject5);
        neighbourhood1.getUObjectMap().put(objId6, battleObject6);
        neighbourhood2.getUObjectMap().put(objId6, battleObject6);

        // Проверяем размещение
        assertEquals(2, neighbourhood1.getUObjectMap().size());
        assertEquals(1, neighbourhood2.getUObjectMap().size());
        // далее проверяем, что объекты находится на нужных окрестностях
        assertTrue(neighbourhood1.isObjectInsideNeighbourhoodPart(battleObject5));
        assertTrue(neighbourhood1.isObjectInsideNeighbourhoodPart(battleObject6));
        assertFalse(neighbourhood2.isObjectInsideNeighbourhoodPart(battleObject5));
        assertTrue(neighbourhood2.isObjectInsideNeighbourhoodPart(battleObject6));

        // Проверка макрокоманд, что они пустые
        assertEmptyCommands(battleObject5);
        assertEmptyCommands(battleObject6);

        CollisionMacroCommand object5MacroCommand = (CollisionMacroCommand) battleObject5.getProperty("macroCommand");
        object5MacroCommand.setCollisionCheckCommands(List.of(new CollisionCheckCommand(battleObject5, battleObject6)));
        CollisionMacroCommand object6MacroCommand = (CollisionMacroCommand) battleObject6.getProperty("macroCommand");
        object6MacroCommand.setCollisionCheckCommands(List.of(new CollisionCheckCommand(battleObject5, battleObject6)));
        // Проверка макрокоманд у объектов
        assertEquals(1, object5MacroCommand.getCollisionCheckCommands().size());
        assertEquals(1, object6MacroCommand.getCollisionCheckCommands().size());

        // Перемещение объекта 3
        new MoveCommand(new MovingObjectAdapter(battleObject5)).execute();

        // Проверка нового местоположения
        Point location = (Point) battleObject5.getProperty("location");
        assertEquals(7, location.getX());
        assertEquals(-5, location.getY());

        // Запуск команды обновления макрокоманды
        CommandDTO cmdDto = new CommandDTO();
        cmdDto.setCommandId(CommandType.UPDATE_AND_CHECK_COLLISION_COMMAND.name());
        cmdDto.setGameObjectId(objId5);
        cmdDto.setGameId(gameId);
        spaceBattleService.runCommand(cmdDto);

        // Проверка изменений в системах
        assertEquals(2, neighbourhood1.getUObjectMap().size());
        assertEquals(2, neighbourhood2.getUObjectMap().size());
        // далее проверяем, что объекты находится на нужных окрестностях
        assertTrue(neighbourhood1.isObjectInsideNeighbourhoodPart(battleObject5));
        assertTrue(neighbourhood1.isObjectInsideNeighbourhoodPart(battleObject6));
        assertTrue(neighbourhood2.isObjectInsideNeighbourhoodPart(battleObject5));
        assertTrue(neighbourhood2.isObjectInsideNeighbourhoodPart(battleObject6));

        // Проверка команд макросов
        CollisionMacroCommand object5MacroCommandResult = (CollisionMacroCommand) battleObject5.getProperty("macroCommand");
        CollisionMacroCommand object6MacroCommandResult = (CollisionMacroCommand) battleObject6.getProperty("macroCommand");
        assertEquals(1, object5MacroCommandResult.getCollisionCheckCommands().size());
        assertEquals(1, object6MacroCommandResult.getCollisionCheckCommands().size());
    }

}
