package org.example;

import java.util.ArrayDeque;
import java.util.Queue;

public class SMOService {
    private final int LIMIT = 10;
    private int rejections = 0;
    private int iterations = 0;
    private Queue<String> queue = new ArrayDeque<>(LIMIT);

    public synchronized String take() {
        try {
            while (queue.isEmpty()) {
                wait();
            }

            return queue.poll();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void put(String message) {
        iterations++;
        if (queue.size() == LIMIT) {
            rejections++;
            return;
        }

        queue.add(message);
        notifyAll();
    }


    public int getRejectionProbability() {
        double probability = (double) rejections / iterations * 100;
        this.rejections = 0;
        this.iterations = 0;
        return (int) probability;
    }


    public int getQueueSize() {
        return queue.size();
    }

}