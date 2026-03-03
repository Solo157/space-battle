package com.space.event;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Ивент для ожидания выполнения всех команд вызывающим потоком.
 */
public class ManualResetEvent {

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    /**
     * Метод ожидания. Нужен, чтобы вызывающй поток ждал поток выполнения очередей. Когда команды выполнятся, тогда
     * вызывающий поток должен продолжить работу. Т.е. после того, как выполнится unlock.
     */
    public void waitOne() throws InterruptedException {
        lock.lock();
        try {
            condition.await();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Использует поток сервера, который выполняет команды, чтобы отпустить вызывающий поток.
     */
    public void unlock() {
        lock.lock();
        try {
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }
}
