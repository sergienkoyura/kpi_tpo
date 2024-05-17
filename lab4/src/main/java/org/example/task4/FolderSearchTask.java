package org.example.task4;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class FolderSearchTask extends RecursiveTask<List<String>> {
    private final Folder folder;
    private final List<String> words;

    FolderSearchTask(Folder folder, List<String> words) {
        super();
        this.folder = folder;
        this.words = words;
    }

    @Override
    protected List<String> compute() {
        List<String> result = new ArrayList<>();
        List<RecursiveTask<List<String>>> forks = new LinkedList<>();
        for (Folder subFolder : folder.getSubFolders()) {
            FolderSearchTask task = new FolderSearchTask(subFolder, words);
            forks.add(task);
            task.fork();
        }
        for (Document document : folder.getDocuments()) {
            DocumentSearchTask task = new DocumentSearchTask(document, words);
            forks.add(task);
            task.fork();
        }
        for (RecursiveTask<List<String>> task : forks) {
            result.addAll(task.join());
        }
        return result;
    }
}