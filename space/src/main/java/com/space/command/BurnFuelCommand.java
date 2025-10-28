package com.space.command;

import com.space.adapter.IBurnFuelObject;

/**
 * Уменьшает количество топлива на скорость расхода топлива.
 */
public class BurnFuelCommand implements ICommand {

    private final IBurnFuelObject object;

    public BurnFuelCommand(IBurnFuelObject object) {
        this.object = object;
    }

    @Override
    public void execute() {
        int fuelCount = object.getFuel();
        int fuelFlowRate = object.getFuelFlowRate();

        int newFuelCount = fuelCount - fuelFlowRate;

        object.setFuel(newFuelCount);
    }

}
