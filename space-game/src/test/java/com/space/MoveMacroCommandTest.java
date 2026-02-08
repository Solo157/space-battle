package com.space;

import com.space.adapter.BurnFuelObjectAdapter;
import com.space.adapter.CheckFuelObjectAdapter;
import com.space.adapter.MovingObjectAdapter;
import com.space.command.*;
import com.space.exception.exception.CommandException;
import com.space.entity.*;
import com.space.entity.Vector;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class MoveMacroCommandTest {

    @Test
    public void executeMacroCommand_correctLogicOfMacroCommand() {
        // when
        SpaceShip spaceShip = new SpaceShip();
        spaceShip.setProperty("fuel", 2);
        spaceShip.setProperty("velocity", new Vector(-7, 3));
        spaceShip.setProperty("location", new Point(12, 5));
        spaceShip.setProperty("fuelFlowRate", 2);

        MovingObjectAdapter movingObjectAdapter = new MovingObjectAdapter(spaceShip);
        CheckFuelObjectAdapter checkFuelObjectAdapter = new CheckFuelObjectAdapter(spaceShip);
        BurnFuelObjectAdapter burnFuelObjectAdapter = new BurnFuelObjectAdapter(spaceShip);

        CheckFuelCommand checkFuelCommand = new CheckFuelCommand(checkFuelObjectAdapter);
        BurnFuelCommand burnFuelCommand = new BurnFuelCommand(burnFuelObjectAdapter);
        MoveCommand moveCommand = new MoveCommand(movingObjectAdapter);
        MoveMacroCommand moveMacroCommand = new MoveMacroCommand(List.of(checkFuelCommand, moveCommand, burnFuelCommand));

        // assert
        // выполнили первый раз с количеством топлива 2 - ok
        moveMacroCommand.execute();

        Point location = (Point) spaceShip.getProperty("location");
        assertEquals(5, location.getX());
        assertEquals(8, location.getY());
        assertEquals(0, (int) spaceShip.getProperty("fuel"));

        // do
        // выполнили второй раз с количеством топлива уже 0 - топлива не хватило, выброс исключения
        assertThrows(CommandException.class, moveMacroCommand::execute);
    }

}
