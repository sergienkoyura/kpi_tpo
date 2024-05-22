package org.example.task1;

import java.util.concurrent.atomic.AtomicInteger;

public class TransferThread extends Thread {
    private Bank bank;
    private BankAtomic bankAtomic;
    private int fromAccount;
    private int maxAmount;
    private static final int REPS = 1000000;

    public TransferThread(Bank b, BankAtomic ba, int from, int max) {
        bank = b;
        bankAtomic = ba;
        fromAccount = from;
        maxAmount = max;
    }

    @Override
    public void run() {
        for (int i = 0; i < REPS; i++) {
            int toAccount = (int) (bank.accounts() * Math.random());
//                int amount = 2000;
            int amount = (int) (maxAmount * Math.random() / REPS);
//                bank.syncMethod(fromAccount, toAccount, amount);
//                bank.syncLock(fromAccount, toAccount, amount);
//            bank.syncBlock(fromAccount, toAccount, amount);
//                bank.syncBlockThis(fromAccount, toAccount, amount);
                bankAtomic.syncAtomic(fromAccount, toAccount, new AtomicInteger(amount));
        }
    }
}
