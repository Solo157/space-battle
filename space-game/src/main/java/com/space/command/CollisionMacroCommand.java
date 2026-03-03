package com.space.command;

import lombok.Data;

import java.util.*;

/**
 * Макрокоманда для определения коллизий. Складываются команды, проверяющие коллизию, по каждому соседу окрестности для
 * объекта.
 */
@Data
public class CollisionMacroCommand implements ICommand {

    private List<ICommand> collisionCheckCommands;

    public CollisionMacroCommand(List<ICommand> collisionCheckCommands) {
        this.collisionCheckCommands = collisionCheckCommands;
    }

    @Override
    public void execute() {
        collisionCheckCommands.forEach(ICommand::execute);
    }

}
