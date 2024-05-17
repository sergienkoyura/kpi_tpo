package org.example.task4;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class WordCounter {


    private final ForkJoinPool forkJoinPool = new ForkJoinPool();

    String[] wordsIn(String line) {
        return line.trim().split("(\\s|\\p{Punct})+");
    }

    List<String> occurrencesCount(Document document, List<String> words) {
        for (String line : document.getLines()) {
            for (String word : wordsIn(line)) {
                if (words.contains(word.toLowerCase())){
                    return List.of(document.getName());
                }
            }
        }
        return new ArrayList<>();
    }
    List<String> findOccurrencesInParallel(Folder folder, List<String> words) {
        return forkJoinPool.invoke(new FolderSearchTask(folder, words));
    }
}