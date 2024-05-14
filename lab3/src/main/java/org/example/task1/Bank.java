package org.example.task1;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Bank {
    public static final int N_LOG = 1000;
    private final int[] accounts;
    private long transactions;

    private final Object lock = new Object();
    private Lock locker;
    private Condition waitNegative;

    public Bank(int n, int initialBalance) {
        accounts = new int[n];
        int i;
        for (i = 0; i < accounts.length; i++) {
            accounts[i] = initialBalance;
        }
        transactions = 0;
        locker = new ReentrantLock();
        waitNegative = locker.newCondition();
    }


    public synchronized void syncMethod(int from, int to, int amount) {
        if (accounts[from] < amount) {
            return;
        }
        accounts[from] -= amount;
        accounts[to] += amount;
        transactions++;
        if (transactions % N_LOG == 0) {
            getTotalAmount();
        }
    }

    public void syncLock(int from, int to, int amount) {
        locker.lock();
        try {
            while (accounts[from] < amount) {
                waitNegative.await();
            }
            accounts[from] -= amount;
            accounts[to] += amount;
            transactions++;
            if (transactions % N_LOG == 0) {
                getTotalAmount();
            }
            waitNegative.signalAll();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            locker.unlock();
        }
    }

    public void syncBlock(int from, int to, int amount) {
        synchronized (lock) {
            if (accounts[from] < amount) {
                return;
            }
            accounts[from] -= amount;
            accounts[to] += amount;
            transactions++;
            if (transactions % N_LOG == 0) {
                getTotalAmount();
            }
        }
    }

    public void syncBlockThis(int from, int to, int amount) {
        synchronized (this) {
            if (accounts[from] < amount) {
                return;
            }
            accounts[from] -= amount;
            accounts[to] += amount;
            transactions++;
            if (transactions % N_LOG == 0) {
                getTotalAmount();
            }
        }
    }


    public void getTotalAmount() {
        int sum = 0;
        for (int i = 0; i < accounts.length; i++) {
            sum += accounts[i];
        }
        System.out.println("Transactions:" + transactions + " Sum: " + sum);
    }

    public int accounts() {
        return accounts.length;
    }
}
