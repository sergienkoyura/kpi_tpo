package org.example.task4;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        WordCounter wordCounter = new WordCounter();
        Folder folder = Folder.fromDirectory(new File("sample"));
        List<String> words = new ArrayList<>(
                Arrays.asList("database", "algorithm", "cloud", "cyber", "machine", "software",
                "network", "programming", "science", "security", "learning", "software", "development")
        );
        List<String> files = wordCounter.findOccurrencesInParallel(folder, words);
        System.out.println(String.join("\n", files));
    }
}
