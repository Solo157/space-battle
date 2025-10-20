package com.space.module;

import java.util.*;

public class SpaceShip implements UObject {

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
