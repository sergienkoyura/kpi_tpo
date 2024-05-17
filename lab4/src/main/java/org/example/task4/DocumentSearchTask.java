package org.example.task4;

import java.util.List;
import java.util.concurrent.RecursiveTask;

public class DocumentSearchTask extends RecursiveTask<List<String>> {
    private final Document document;
    private final List<String> words;

    DocumentSearchTask(Document document, List<String> words) {
        super();
        this.document = document;
        this.words = words;
    }

    @Override
    protected List<String> compute() {
        return new WordCounter().occurrencesCount(document, words);
    }
}