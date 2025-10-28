package com.space.adapter;

public interface IBurnFuelObject {

    /**
     * Получить количество топлива.
     */
    int getFuel();

    /**
     * Установить новое значение топлива.
     */
    void setFuel(int newFuelCount);

    /**
     * Скорость расхода топлива.
     */
    int getFuelFlowRate();

}
