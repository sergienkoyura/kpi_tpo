package org.example;

import java.util.Random;

public class Producer implements Runnable {
    private SMOService SMOService;
    private final int WAITING_TIME = 100;

    public Producer(SMOService SMOService) {
        this.SMOService = SMOService;
    }

    public void run() {
        while (true){
//            System.out.println("--------------\nSending request...\n--------------");
            SMOService.put("msg");
            try {
                Thread.sleep(new Random().nextInt(50) + WAITING_TIME);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}