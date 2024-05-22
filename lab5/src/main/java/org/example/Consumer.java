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
//          System.out.printf("Consumer %d: started working with a message: '%s'%n", Thread.currentThread().getId(), message);
            try {
                Thread.sleep((long) (new Random().nextGaussian(800, 50)));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
//          System.out.printf("Consumer %d: finished%n", Thread.currentThread().getId());
        }
    }
}