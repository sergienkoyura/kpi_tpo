package org.example.task1;

import java.util.concurrent.RecursiveTask;

public class DocumentSearchTask extends RecursiveTask<WordLength> {
    private final Document document;

    DocumentSearchTask(Document document) {
        super();
        this.document = document;
    }

    @Override
    protected WordLength compute() {
        return new WordCounter().wordLength(document);
    }
}