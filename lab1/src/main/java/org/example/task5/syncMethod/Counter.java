package org.example.task5.syncMethod;

public class Counter {
    private static int counter = 0;

    public static int getCounter() {
        return counter;
    }

    public synchronized static void increment(){
        for (int i = 0; i < 100000; i++){
            counter++;
        }
    }
    public synchronized static void decrement(){
        for (int i = 0; i < 100000; i++){
            counter--;
        }
    }
}
