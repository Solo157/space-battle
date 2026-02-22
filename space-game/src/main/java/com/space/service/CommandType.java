package com.space.service;

/**
 * Перечень всех доступных команд в системе.
 */
public enum CommandType {

    /**
     * Просто вывод сообщения в консоль.
     */
    PRINT_COMMAND,
    /**
     * Команда, чтобы сдвинуть объект.
     */
    MOVE_COMMAND,
    /**
     * Команда для обновления макрокоманды по коллизиям для объекта, после его движения.
      */
    UPDATE_AND_CHECK_COLLISION_COMMAND;

}
