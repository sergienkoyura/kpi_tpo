package org.example;

import java.util.Random;

public class Consumer implements Runnable {
    private SMOService SMOService;

    public Consumer(SMOService SMOService) {
        this.SMOService = SMOService;
    }

    public void run() {
        while (true) {

            SMOService.take();
            try {
                Thread.sleep((long) (new Random().nextGaussian(800, 50)));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}