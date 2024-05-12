package org.example.task5.plain;

public class Main {
    public static void main(String[] args) {
        System.out.println("Plain counting:");
        PlainCounter counter = new PlainCounter();
        int a = 0;
        Thread thread1 = new Thread(() -> {
            counter.increment();
            System.out.println("RESULT: " + counter.getCounter());
        });
        Thread thread2 = new Thread(() -> {
            counter.decrement();
            System.out.println("RESULT: " + counter.getCounter());
        });
        thread1.start();
        thread2.start();
    }
}
