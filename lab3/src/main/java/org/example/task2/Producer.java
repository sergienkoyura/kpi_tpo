package org.example.task2;

import java.util.Random;

public class Producer implements Runnable {
    private Drop drop;
    private final int SIZE = 100;

    public Producer(Drop drop) {
        this.drop = drop;
    }

    public void run() {
        int[] startArray = new int[SIZE];

        for (int i = 0; i < SIZE; i++) {
            startArray[i] = i;
            drop.put(String.valueOf(startArray[i]));
        }
        drop.put("finish");
    }
}