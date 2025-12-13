package com.space.command;

import com.space.exception.exception.StaticObjectException;
import com.space.adapter.IMovingObject;
import com.space.entity.Vector;

public class MoveCommand implements ICommand {

    private final IMovingObject object;

    public MoveCommand(IMovingObject object) {
        this.object = object;
    }

    public void execute() {
        if (object.isStatic()) {
            throw new StaticObjectException("Object must not be static");
        }

        object.setPosition(Vector.plus(object.getLocation(), object.getVelocity()));
    }

}
