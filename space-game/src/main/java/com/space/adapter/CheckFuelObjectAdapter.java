package com.space.adapter;

import com.space.entity.UObject;

public class CheckFuelObjectAdapter implements ICheckFuelObject {

    private UObject object;

    public CheckFuelObjectAdapter(UObject object) {
        this.object = object;
    }

    @Override
    public int getFuel() {
        return (int) object.getProperty("fuel");
    }

}
