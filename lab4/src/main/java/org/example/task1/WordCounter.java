package org.example.task1;

import java.util.concurrent.ForkJoinPool;

public class WordCounter {


    private final ForkJoinPool forkJoinPool = new ForkJoinPool();

    String[] wordsIn(String line) {
        return line.trim().split("(\\s|\\p{Punct})+");
    }

    WordLength wordLength(Document document) {
        WordLength wordLengthResult = new WordLength();
        for (String line : document.getLines()) {
            for (String word : wordsIn(line)) {
                // occurs on an empty line
                if (!word.isEmpty())
                    wordLengthResult.increaseAverage(word.length());
            }
        }
        return wordLengthResult;
    }

    WordLength countOccurrencesOnSingleThread(Folder folder) {
        WordLength wordLengthResult = new WordLength();
        for (Folder subFolder : folder.getSubFolders()) {
            wordLengthResult.addAvg(countOccurrencesOnSingleThread(subFolder));
        }
        for (Document document : folder.getDocuments()) {
            wordLengthResult.addAvg(wordLength(document));
        }
        return wordLengthResult;
    }

    WordLength countOccurrencesInParallel(Folder folder) {
        return forkJoinPool.invoke(new FolderSearchTask(folder));
    }
}