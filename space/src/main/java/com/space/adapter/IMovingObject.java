package com.space.adapter;

import com.space.entity.Point;
import com.space.entity.Vector;

public interface IMovingObject {

    Point getLocation();
    Vector getVelocity();
    void setVelocity(Vector newValue);
    void setPosition(Point newValue);
    Boolean isStatic();

}
