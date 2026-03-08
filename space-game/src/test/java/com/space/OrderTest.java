package com.space;

import com.space.command.ICommand;
import com.space.entity.Order;
import com.space.entity.Point;
import com.space.entity.UObject;
import com.space.ioc.IoC;
import com.space.serverthread.ServerThreadIoCDependencyRegistrator;
import com.space.service.SpaceBattleCrudService;
import com.space.service.SpaceBattleService;
import com.space.service.UserScopeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

/**
 * Тест, проверяющий работу/отправку ордера польльзователем/игроком и его интерпретацией в команду.
 */
@SpringBootTest
public class OrderTest {

    @Autowired
    private SpaceBattleCrudService spaceBattleCrudService;

    @Autowired
    private ServerThreadIoCDependencyRegistrator serverThreadIoCDependencyRegistrator;

    @Autowired
    private SpaceBattleService spaceBattleService;

    @MockitoSpyBean
    private UserScopeService userScopeService;

    /**
     * Тест проверяющий, что ордер, который отправляет клиент корректно интерпретируется в команду и эта команда выполняется.
     */
    @Test
    public void testOrderWorkSuccessfully() throws InterruptedException {
        spaceBattleCrudService.resetDefaultCrudService();

        UObject spaceShip = spaceBattleCrudService.findSpaceBattleObject("objectId8");
        assertEquals(12, ((Point)spaceShip.getProperty("location")).getX());
        assertEquals(5, ((Point)spaceShip.getProperty("location")).getY());

        Mockito.when(userScopeService.getUserScope(any())).thenReturn(getUserScopeWithStartMoveAndGameObjectId("545"));

        String gameId = UUID.randomUUID().toString();
        spaceBattleService.createGame(List.of(545), gameId);

        UObject order = new Order();
        order.setProperty("action", "START_MOVE");
        order.setProperty("gameId", gameId);
        order.setProperty("objectId", "objectId8");
        order.setProperty("player", "545");
        order.setProperty("velocity", "5");

        spaceBattleService.runOrder(order);

        Point location = (Point) spaceShip.getProperty("location");
        assertEquals(5, location.getX());
        assertEquals(8, location.getY());
    }

    public Object getUserScopeWithStartMoveAndGameObjectId(String userId) {
        IoC.<ICommand>resolve("IoC.Scope.Current.Clear").execute();
        var iocScope = IoC.<Object>resolve("IoC.Scope.Create");
        IoC.<ICommand>resolve("IoC.Scope.Current.Set", iocScope).execute();
        ServerThreadIoCDependencyRegistrator.registerStartMoveCommand();
        serverThreadIoCDependencyRegistrator.registerGameObjectId(userId);
        return iocScope;
    }

    /**
     * Тест, проверяющий, что у игрока/пользователя нет разрешенной к выполнению команды START_MOVE.
     * Поэтому выбрасывается исключение.
     */
    @Test
    public void testStartMoveCommandNotAllowedForPlayer() throws InterruptedException {
        spaceBattleCrudService.resetDefaultCrudService();

        UObject spaceShip = spaceBattleCrudService.findSpaceBattleObject("objectId8");
        assertEquals(12, ((Point)spaceShip.getProperty("location")).getX());
        assertEquals(5, ((Point)spaceShip.getProperty("location")).getY());

        Mockito.when(userScopeService.getUserScope(any())).thenReturn(getUserScopeWithGameObjectId("545"));

        String gameId = UUID.randomUUID().toString();
        spaceBattleService.createGame(List.of(545), gameId);

        UObject order = new Order();
        order.setProperty("action", "START_MOVE");
        order.setProperty("gameId", gameId);
        order.setProperty("objectId", "objectId8");
        order.setProperty("player", "545");
        order.setProperty("velocity", "5");

        assertThrows(
                RuntimeException.class, () -> {
                    spaceBattleService.runOrder(order);
                }
        );
    }

    public Object getUserScopeWithGameObjectId(String userId) {
        IoC.<ICommand>resolve("IoC.Scope.Current.Clear").execute();
        var iocScope = IoC.<Object>resolve("IoC.Scope.Create");
        IoC.<ICommand>resolve("IoC.Scope.Current.Set", iocScope).execute();
        serverThreadIoCDependencyRegistrator.registerGameObjectId(userId);
        return iocScope;
    }

    /**
     * Тест, проверяющий, что игрок не может управлять и взаимодействовать с объектом, который принадлежит другому игроку.
     * Т.е. идентификатор объекта отсутствует у данного игрока в списке идентификаторов принадлежащих к нему объектов.
     */
    @Test
    public void testObjectNotBelongToPlayer() throws InterruptedException {
        spaceBattleCrudService.resetDefaultCrudService();

        UObject spaceShip = spaceBattleCrudService.findSpaceBattleObject("objectId8");
        assertEquals(12, ((Point)spaceShip.getProperty("location")).getX());
        assertEquals(5, ((Point)spaceShip.getProperty("location")).getY());

        Mockito.when(userScopeService.getUserScope(any())).thenReturn(getUserScopeWithStartMoveCommand());

        String gameId = UUID.randomUUID().toString();
        spaceBattleService.createGame(List.of(545), gameId);

        UObject order = new Order();
        order.setProperty("action", "START_MOVE");
        order.setProperty("gameId", gameId);
        order.setProperty("objectId", "objectId8");
        order.setProperty("player", "545");
        order.setProperty("velocity", "5");

        assertThrows(
                RuntimeException.class, () -> {
                    spaceBattleService.runOrder(order);
                }
        );
    }

    public Object getUserScopeWithStartMoveCommand() {
        IoC.<ICommand>resolve("IoC.Scope.Current.Clear").execute();
        var iocScope = IoC.<Object>resolve("IoC.Scope.Create");
        IoC.<ICommand>resolve("IoC.Scope.Current.Set", iocScope).execute();
        ServerThreadIoCDependencyRegistrator.registerStartMoveCommand();
        return iocScope;
    }

}
