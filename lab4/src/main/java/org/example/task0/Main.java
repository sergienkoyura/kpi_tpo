package org.example.task0;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        WordCounter wordCounter = new WordCounter();
        Folder folder = Folder.fromDirectory(new File("sample"));
        System.out.println(wordCounter.countOccurrencesOnSingleThread(folder, "sorrowful"));
    }
}
