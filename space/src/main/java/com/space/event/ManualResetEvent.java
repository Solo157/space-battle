package com.space.event;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ManualResetEvent {

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private boolean isLocked = true;

    public void waitOne() throws InterruptedException {
        lock.lock();
        try {
            while (isLocked) {
                condition.await();
            }
        } finally {
            lock.unlock();
        }
    }

    public void unlock() {
        lock.lock();
        try {
            isLocked = false;
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }
}
