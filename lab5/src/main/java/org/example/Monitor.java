package org.example;

import java.util.concurrent.Callable;

public class Monitor implements Callable<Values> {

    private SMOService smoService;
    private final int WAITING_TIME = 2000;
    private int timeLimit;
    private int rejectionSum;
    private int queueSum;

    public Monitor(SMOService SMOService, int timeLimit) {
        this.smoService = SMOService;
        rejectionSum = 0;
        queueSum = 0;
        this.timeLimit = timeLimit;
    }


    @Override
    public Values call() {
        int iterations = timeLimit / WAITING_TIME;

        for (int i = 0; i < iterations; i++) {
            try {
                Thread.sleep(WAITING_TIME);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

                System.out.println("\nMonitor " + i);

            int rejectionProbability = smoService.getRejectionProbability();
            System.out.println("Rejection Probability: " + rejectionProbability + '%');
            rejectionSum += rejectionProbability;

            int queueSize = smoService.getQueueSize();
            System.out.println("Queue size: " + queueSize);
            queueSum += queueSize;
        }

        return new Values((double) queueSum / iterations, (double) rejectionSum / iterations);
    }
}
