package org.example.task1;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
//        testData();
        realData();
    }

    static void testData() throws IOException {
        WordCounter wordCounter = new WordCounter();
        Folder folder = Folder.fromDirectory(new File("sample0"));
//        WordLength result = wordCounter.countOccurrencesOnSingleThread(folder);
        WordLength result = wordCounter.countOccurrencesInParallel(folder);

        System.out.printf("There are %d words with %.5f average length!%n", result.getCount(), result.getAvg());
    }

    static void realData() throws IOException {
        WordCounter wordCounter = new WordCounter();
        Folder folder = Folder.fromDirectory(new File("sample"));

        useSingle(wordCounter, folder);
        useParallel(wordCounter, folder);
    }

    static void useSingle(WordCounter wordCounter, Folder folder) {
        System.out.println("Single");
        System.out.println("i\titems\tavg\t\tms");
        long avgTime = 0;
        for (int i = 0; i < 10; i++) {
            long start = System.currentTimeMillis();
            WordLength result = wordCounter.countOccurrencesOnSingleThread(folder);
            long end = System.currentTimeMillis();
            long timeTook = end - start;
            avgTime = (avgTime * i + timeTook) / (i + 1);
            System.out.printf("%d\t%d\t%.5f\t%d%n", i + 1, result.getCount(), result.getAvg(), end - start);
        }
        System.out.printf("Average time took: %d ms!%n", avgTime);
    }

    static void useParallel(WordCounter wordCounter, Folder folder) {
        System.out.println("Parallel");
        System.out.println("i\titems\tavg\t\tms");
        long avgTime = 0;
        for (int i = 0; i < 10; i++) {
            long start = System.currentTimeMillis();
            WordLength result = wordCounter.countOccurrencesInParallel(folder);
            long end = System.currentTimeMillis();
            long timeTook = end - start;
            avgTime = (avgTime * i + timeTook) / (i + 1);
            System.out.printf("%d\t%d\t%.5f\t%d%n", i + 1, result.getCount(), result.getAvg(), end - start);
        }
        System.out.printf("Average time took: %d ms!%n", avgTime);
    }
}
