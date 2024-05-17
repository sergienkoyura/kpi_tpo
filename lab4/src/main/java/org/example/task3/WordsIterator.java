package org.example.task3;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;

public class WordsIterator {

    private final ForkJoinPool forkJoinPool = new ForkJoinPool();

    String[] wordsIn(String line) {
        return line.trim().split("(\\s|\\p{Punct})+");
    }

    Map<String, Integer> wordsIterate(Document document) {
        Map<String, Integer> words = new HashMap<>();
        for (String line : document.getLines()) {
            for (String word : wordsIn(line)) {
                if (!word.isEmpty()) {
                    Integer value = words.get(word);
                    if (value == null) {
                        words.put(word, 1);
                    } else {
                        words.replace(word, value + 1);
                    }
                }
            }
        }
        return words;
    }

    Map<String, Integer> wordsIterate(Map<String, Integer> wordsLeft, Map<String, Integer> wordsRight) {
        Map<String, Integer> words = new HashMap<>();
        for (String word : wordsLeft.keySet()) {
            int valueRight = 0;
            if (wordsRight.get(word) != null) {
                valueRight = wordsRight.get(word);
            }
            words.put(word, wordsLeft.get(word) + valueRight);
            wordsRight.remove(word);
        }

        for (String word : wordsRight.keySet()) {
            words.put(word, wordsRight.get(word));
        }
        return words;
    }

    Map<String, Integer> countSameInParallel(Folder folder) {
        return forkJoinPool.invoke(new FolderSearchTask(folder));
    }
}