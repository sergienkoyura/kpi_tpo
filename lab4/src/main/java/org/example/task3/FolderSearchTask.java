package org.example.task3;

import java.util.*;
import java.util.concurrent.RecursiveTask;

public class FolderSearchTask extends RecursiveTask<Map<String, Integer>> {
    private final Folder folder;

    FolderSearchTask(Folder folder) {
        super();
        this.folder = folder;
    }

    @Override
    protected Map<String, Integer> compute() {
        Map<String, Integer> result = new HashMap<>();
        List<RecursiveTask<Map<String, Integer>>> forks = new LinkedList<>();
        for (Folder subFolder : folder.getSubFolders()) {
            FolderSearchTask task = new FolderSearchTask(subFolder);
            forks.add(task);
            task.fork();
        }
        for (Document document : folder.getDocuments()) {
            DocumentSearchTask task = new DocumentSearchTask(document);
            forks.add(task);
            task.fork();
        }
        for (RecursiveTask<Map<String, Integer>> task : forks) {
            result = new WordsIterator().wordsIterate(task.join(), result);
        }
        return result;
    }
}