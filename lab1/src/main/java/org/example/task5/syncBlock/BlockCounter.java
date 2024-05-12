package org.example.task5.syncBlock;

public class BlockCounter {
    private final Object lock = new Object();
    private int counter = 0;

    public int getCounter() {
        return counter;
    }

    public void increment(){
        synchronized (lock){
            for (int i = 0; i < 100000; i++){
                counter++;
            }
        }
    }
    public void decrement(){
            synchronized (lock){
            for (int i = 0; i < 100000; i++){
                counter--;
            }
        }
    }
}
