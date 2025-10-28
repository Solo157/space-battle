package com.space.adapter;

import com.space.entity.*;

public class MovingObjectAdapter implements IMovingObject {

    private UObject object;

    public MovingObjectAdapter(UObject object) {
        this.object = object;
    }

    @Override
    public Point getLocation() {
        return (Point) object.getProperty("location");
    }

    @Override
    public Vector getVelocity() {
        return (Vector) object.getProperty("velocity");
    }

    @Override
    public void setPosition(Point newValue) {
        object.setProperty("location", newValue);
    }

    @Override
    public boolean isStatic() {
        Boolean isStatic = (Boolean) object.getProperty("static");
        if (isStatic == null) {
            return false;
        }

        return isStatic;
    }

}
