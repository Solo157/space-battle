package com.space;

import com.space.command.CheckFuelCommand;
import com.space.exception.exception.CommandException;
import com.space.adapter.CheckFuelObjectAdapter;
import com.space.entity.SpaceShip;
import org.junit.Test;

import static org.junit.Assert.assertThrows;

public class CheckFuelCommandTest {

    @Test
    public void checkFuelCommand_successfully() {
        // when
        SpaceShip spaceShip = new SpaceShip();
        spaceShip.setProperty("fuel", 10);

        CheckFuelObjectAdapter checkFuelObjectAdapter = new CheckFuelObjectAdapter(spaceShip);

        // do
        CheckFuelCommand checkFuelCommand = new CheckFuelCommand(checkFuelObjectAdapter);
        checkFuelCommand.execute();
    }

    @Test
    public void checkFuelCommand_notEnoughFuel() {
        // when
        SpaceShip spaceShip = new SpaceShip();
        spaceShip.setProperty("fuel", 0);

        CheckFuelObjectAdapter checkFuelObjectAdapter = new CheckFuelObjectAdapter(spaceShip);

        // do
        CheckFuelCommand checkFuelCommand = new CheckFuelCommand(checkFuelObjectAdapter);
        // assert
        assertThrows(CommandException.class, checkFuelCommand::execute);
    }

}
