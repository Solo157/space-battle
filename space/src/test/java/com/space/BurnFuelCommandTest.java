package com.space;

import com.space.command.BurnFuelCommand;
import com.space.adapter.BurnFuelObjectAdapter;
import com.space.entity.SpaceShip;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BurnFuelCommandTest {

    @Test
    public void burnFuelCommand_successfully() {
        // when
        SpaceShip spaceShip = new SpaceShip();
        spaceShip.setProperty("fuel", 10);
        spaceShip.setProperty("fuelFlowRate", 2);

        BurnFuelObjectAdapter burnFuelObjectAdapter = new BurnFuelObjectAdapter(spaceShip);

        // do
        BurnFuelCommand burnFuelCommand = new BurnFuelCommand(burnFuelObjectAdapter);
        burnFuelCommand.execute();

        // assert
        assertEquals(8, (int) spaceShip.getProperty("fuel"));
    }

}
