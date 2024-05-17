package org.example.task3;

import java.util.Map;
import java.util.concurrent.RecursiveTask;

public class DocumentSearchTask extends RecursiveTask<Map<String, Integer>> {
    private final Document document;

    DocumentSearchTask(Document document) {
        super();
        this.document = document;
    }

    @Override
    protected Map<String, Integer> compute() {
        return new WordsIterator().wordsIterate(document);
    }
}