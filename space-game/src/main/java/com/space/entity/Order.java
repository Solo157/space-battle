package com.space.entity;

import lombok.Data;

import java.util.*;

/**
 * Сущность, которую отправляет пользователь. Это некоторый ордер на выполнение какой-то команды в игре.
 * Данный ордер интерпретируется в команду и команда выполняется.
 */
@Data
public class Order implements UObject {

    // храним свойства/поля объекта
    private Map<String, Object> map = new HashMap<>();

    @Override
    public Object getProperty(String property) {
        return map.get(property);
    }

    @Override
    public void setProperty(String key, Object newValue) {
        map.put(key, newValue);
    }

}
