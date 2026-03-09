package com.space.command;

import com.space.entity.UObject;
import com.space.ioc.IoC;

/**
 * Команда для интерпретации входного ордера. Пользователь отправляет ордер с данным, затем из этого ордера достаются
 * элементы, резолвятся в IoC, как команда.
 */
public class OrderInterpretCommand implements ICommand {

    // ордер, где указано множество параметров на основе которых будет определена команда, которая зарегистрирована в IoC
    private final UObject order;

    public OrderInterpretCommand(UObject order) {
        this.order = order;
    }

    @Override
    public void execute() {
        String action = (String) order.getProperty("action");
        String objectId = (String) order.getProperty("objectId");
        String player = (String) order.getProperty("player");

        if (action == null || objectId == null) {
            throw new IllegalArgumentException("There are not action or objectId");
        }

        IoC.<ICommand>resolve(action, objectId, player, order).execute();
    }

}
