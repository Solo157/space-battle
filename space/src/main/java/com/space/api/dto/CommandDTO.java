package com.space.api.dto;

import lombok.Data;

@Data
public class CommandDTO {

    /**
     * Идентификатор игры, в рамках игры будет выполняться команда.
     */
    private String gameId;

    /**
     * Идентификатор объекта игры.
     */
    private String gameObjectId;


    /**
     * Идентификатор команды (по сути это название команды).
     */
    private String commandId;

    /**
     * Дополнительные аргументы для команды.
     */
    private String args;

}
