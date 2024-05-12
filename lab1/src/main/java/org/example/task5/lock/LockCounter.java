package org.example.task5.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockCounter {
    private final Lock lock = new ReentrantLock();
    private int counter = 0;

    public int getCounter() {
        return counter;
    }

    public void increment(){
        lock.lock();
        try{
            for (int i = 0; i < 100000; i++){
                counter++;
            }
        } finally {
            lock.unlock();
        }
    }
    public void decrement(){
        lock.lock();
        try{
            for (int i = 0; i < 100000; i++){
                counter--;
            }
        } finally {
            lock.unlock();
        }
    }
}
