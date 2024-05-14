package org.example.task1;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BankAtomic {
    public static final int N_LOG = 1000000;
    private final AtomicInteger[] accounts;
    private AtomicLong transactions;

    private Lock locker;

    public BankAtomic(int n, int initialBalance) {
        accounts = new AtomicInteger[n];
        int i;
        for (i = 0; i < accounts.length; i++) {
            accounts[i] = new AtomicInteger(initialBalance);
        }
        transactions = new AtomicLong(0);
        locker = new ReentrantLock();
    }


    public void syncAtomic(int from, int to, AtomicInteger amount) {
        accounts[from].addAndGet(-amount.get());
        accounts[to].addAndGet(amount.get());
        locker.lock();
        try {
            if (transactions.incrementAndGet() % N_LOG == 0) {
                getTotalAmount();
            }
        } finally {
            locker.unlock();
        }
    }

    public void getTotalAmount() {
        int sum = 0;
        for (AtomicInteger account : accounts) {
            sum += account.get();
        }
        System.out.println("Transactions:" + transactions + " Sum: " + sum);
    }

}
