package com.space.module;

public class Move {

    private final IMovingObject object;

    public Move(IMovingObject object) {
        this.object = object;
    }

    public void execute() {
        if (object.isStatic()) {
            throw new RuntimeException();
        }

        object.setPosition(Vector.plus(object.getLocation(), object.getVelocity()));
    }

}
