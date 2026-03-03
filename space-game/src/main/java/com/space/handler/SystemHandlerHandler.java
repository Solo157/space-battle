package com.space.handler;

import com.space.entity.UObject;

import java.util.*;

/**
 * Интерфейс для обработчиков окрестностей. Нужен, например, чтобы обходить окрестности и собирать соседенй/объекты в пределах
 * окрестности, если объект туда попал.
 */
public interface SystemHandlerHandler {

    /**
     * Собрать список соседей в одной окрестности для object.
     */
    void handle(UObject object, List<UObject> neighbors);

    void setNext(SystemHandlerHandler next);

}
