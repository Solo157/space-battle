package com.space.module;

import com.space.exception.exception.StaticObjectException;
import com.space.command.ICommand;

public class Move implements ICommand {

    private final IMovingObject object;

    public Move(IMovingObject object) {
        this.object = object;
    }

    public void execute() {
        if (object.isStatic()) {
            throw new StaticObjectException("Object must not be static");
        }

        object.setPosition(Vector.plus(object.getLocation(), object.getVelocity()));
    }

}
