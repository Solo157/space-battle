package com.space.event;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Ивент для ожидания выполнения всех команд вызывающим потоком.
 */
public class ManualResetEvent {

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    public void waitOne() throws InterruptedException {
        lock.lock();
        try {
            condition.await();
        } finally {
            lock.unlock();
        }
    }

    public void unlock() {
        lock.lock();
        try {
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }
}
