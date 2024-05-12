package org.example.task5.syncBlock;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Synchronization using synchronized blocks:");
        BlockCounter counter = new BlockCounter();
        Thread thread1 = new Thread(counter::increment);
        Thread thread2 = new Thread(counter::decrement);
        thread1.start();
        thread2.start();
        System.out.println("RESULT: " + counter.getCounter());
    }
}
