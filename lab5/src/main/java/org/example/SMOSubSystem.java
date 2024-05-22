package org.example;

import java.util.concurrent.*;

public class SMOSubSystem implements Callable<Values> {
    private static final int CONSUMERS = 5;
    private final int timeLimit;

    public SMOSubSystem(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    @Override
    public Values call() throws ExecutionException, InterruptedException {
        SMOService SMOService = new SMOService();

        ExecutorService executorService = Executors.newFixedThreadPool(CONSUMERS + 2);

        executorService.submit(new Producer(SMOService));

        for (int i = 0; i < CONSUMERS; i++) {
            executorService.submit(new Consumer(SMOService));
        }

        Values v = executorService.submit(new Monitor(SMOService, timeLimit)).get();

        executorService.shutdown();

        if (!executorService.awaitTermination(0, TimeUnit.MILLISECONDS)) {
            executorService.shutdownNow();
        }

        return v;
    }
}