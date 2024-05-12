package org.example.task5.lock;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Synchronization using locks:");
        LockCounter counter = new LockCounter();
        Thread thread1 = new Thread(counter::increment);
        Thread thread2 = new Thread(counter::decrement);
        thread1.start();
        thread2.start();
        System.out.println("RESULT: " + counter.getCounter());
    }
}
