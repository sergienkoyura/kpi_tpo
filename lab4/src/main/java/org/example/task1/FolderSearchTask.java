package org.example.task1;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class FolderSearchTask extends RecursiveTask<WordLength> {
    private final Folder folder;

    FolderSearchTask(Folder folder) {
        super();
        this.folder = folder;
    }

    @Override
    protected WordLength compute() {
        WordLength wordLengthResult = new WordLength();
        List<RecursiveTask<WordLength>> forks = new LinkedList<>();
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
        for (RecursiveTask<WordLength> task : forks) {
            wordLengthResult.addAvg(task.join());
        }
        return wordLengthResult;
    }
}