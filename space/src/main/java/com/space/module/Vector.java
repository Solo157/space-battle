package com.space.module;

import com.space.exception.exception.IncorrectArgumentException;

public class Vector {

    private int dx;
    private int dy;

    public Vector(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public int getX() {
        return this.dx;
    }

    public int getY() {
        return this.dy;
    }

    public void setX(int dx) {
        this.dx = dx;
    }

    public void setY(int dy) {
        this.dy = dy;
    }

    public static Point plus(Point location, Vector velocity) {
        if (location == null || velocity == null) {
            throw new IncorrectArgumentException("Incorrect argument for plus command");
        }

        location.setX(location.getX() + velocity.dx);
        location.setY(location.getY() + velocity.dy);
        return location;
    }

}
