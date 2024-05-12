package org.example.task4;

public class Main {
    static final Object lock = new Object();
    private static char nextSymbol = '-';
    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            generate('-');
        });
        Thread thread2 = new Thread(() -> {
            generate('|');
        });
        thread1.start();
        thread2.start();

    }
    static void generate(char symbol){
        for (int i = 0; i < 100; i++) {
            synchronized (lock) {
                while (symbol != nextSymbol) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (symbol == '-'){
                    System.out.print(symbol);
                    nextSymbol = '|';
                } else {
                    System.out.println(symbol);
                    nextSymbol = '-';
                }
                lock.notify();
            }
        }
    }
}
