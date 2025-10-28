package com.space.module;

public interface IMovingObject {

    Point getLocation();
    Vector getVelocity();
    void setPosition(Point newValue);
    boolean isStatic();
    void setStatic(boolean isStatic);

}
