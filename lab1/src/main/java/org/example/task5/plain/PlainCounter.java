package org.example.task5.plain;

public class PlainCounter {
    private static int counter = 0;

    public int getCounter() {
        return counter;
    }

    public void increment(){
        for (int i = 0; i < 100000; i++){
            counter++;
        }
    }
    public void decrement(){
        for (int i = 0; i < 100000; i++){
            counter--;
        }
    }
}
