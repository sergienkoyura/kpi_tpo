package org.example.task5.syncMethod;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Synchronization using synchronized methods:");
        Thread thread1 = new Thread(Counter::increment);
        Thread thread2 = new Thread(Counter::decrement);
        thread1.start();
        thread2.start();
        System.out.println("RESULT: " + Counter.getCounter());
    }
}
