package org.example.task3;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        WordsIterator wordCounter = new WordsIterator();
        Folder folder = Folder.fromDirectory(new File("sample"));
        System.out.println(wordCounter.countSameInParallel(folder));
    }
}
