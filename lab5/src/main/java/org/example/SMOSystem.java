package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class SMOSystem {
    private static final int ITERATIONS = 20;
    private static final int TIME_LIMIT = 40000;

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(ITERATIONS);
        double avgQueue = 0;
        double avgRejections = 0;
        List<Callable<Values>> subSystems = new ArrayList<>();
        for (int i = 0; i < ITERATIONS; i++) {
            subSystems.add(new SMOSubSystem(TIME_LIMIT));
        }
        List<Future<Values>> values = executorService.invokeAll(subSystems);

        for (Future<Values> value : values) {
            avgQueue += value.get().avgQueue();
            avgRejections += value.get().avgRejections();
        }

        System.out.println("Processing time: " + TIME_LIMIT + " ms");
        System.out.println("Iterations: " + ITERATIONS);
        System.out.printf("Avg queue: %.2f%n", avgQueue / ITERATIONS);
        System.out.printf("Avg rejections: %.2f%%%n", avgRejections / ITERATIONS);

        executorService.shutdown();
    }
}