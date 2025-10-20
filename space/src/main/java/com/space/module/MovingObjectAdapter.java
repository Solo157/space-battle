package com.space.module;

public class MovingObjectAdapter implements IMovingObject {

    private UObject obj;

    public MovingObjectAdapter(UObject obj) {
        this.obj = obj;
    }

    @Override
    public Point getLocation() {
        return (Point) obj.getProperty("location");
    }

    @Override
    public Vector getVelocity() {
        return (Vector) obj.getProperty("velocity");
    }

    @Override
    public void setPosition(Point newValue) {
        obj.setProperty("location", newValue);
    }

    @Override
    public boolean isStatic() {
        Boolean isStatic = (Boolean) obj.getProperty("static");
        if (isStatic == null) {
            return false;
        }

        return isStatic;
    }

}
