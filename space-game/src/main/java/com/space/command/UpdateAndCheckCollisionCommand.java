package com.space.command;

import com.space.entity.UObject;
import com.space.handler.SystemHandlerHandler;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Команда для обновления макрокоманды объекта, в которой находится список команд проверок коллизий для каждого объекта
 * окрестности (соседей объекта)
 */
public class UpdateAndCheckCollisionCommand implements ICommand {

    private final UObject object;
    private final SystemHandlerHandler systemHandler;

    public UpdateAndCheckCollisionCommand(UObject object, SystemHandlerHandler systemHandler) {
        this.object = object;
        this.systemHandler = systemHandler;
    }

    @Override
    public void execute() {
        List<UObject> allNeighbors = new ArrayList<>();

        // Запускаем цепочку систем. Она наполнит список allNeighbors
        systemHandler.handle(object, allNeighbors);
        // Убираем самого себя из списка соседей
        allNeighbors.remove(object);

        // Генерируем НОВУЮ макрокоманду из актуальных соседей
        List<ICommand> checks = allNeighbors.stream()
                .map(neighbor -> new CollisionCheckCommand(object, neighbor))
                .collect(Collectors.toList());

        // Записываем НА МЕСТО старой
        object.setProperty("macroCommand", new CollisionMacroCommand(checks));
    }

}
