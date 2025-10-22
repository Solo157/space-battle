package com.space.module;

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

    @Override
    public void setStatic(boolean isStatic) {
        this.object.setProperty("static", isStatic);
    }

}
