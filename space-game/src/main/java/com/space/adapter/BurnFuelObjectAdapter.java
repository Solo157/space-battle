package com.space.adapter;

import com.space.entity.UObject;

/**
 * Уменьшает количество топлива на скорость расхода топлива.
 */
public class BurnFuelObjectAdapter implements IBurnFuelObject {

    private UObject object;

    public BurnFuelObjectAdapter(UObject object) {
        this.object = object;
    }

    @Override
    public int getFuel() {
        return (int) object.getProperty("fuel");
    }

    @Override
    public void setFuel(int newFuelCount) {
        object.setProperty("fuel", newFuelCount);
    }

    @Override
    public int getFuelFlowRate() {
        return (int) object.getProperty("fuelFlowRate");
    }

}
