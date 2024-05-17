package org.example.task2;

import java.io.*;
import java.util.Random;

public class Generator {
    static int[][] generate(String filename){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, false))) {
            for (int i = 0; i < Multiply.LENGTH; i++) {
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < Multiply.LENGTH; j++) {
                    sb.append(new Random().nextInt(100) - 50).append(";");
                }
                sb.deleteCharAt(sb.length() - 1);
                bw.write(sb + System.lineSeparator());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return initialize(filename);
    }

    static int[][] initialize(String filename) {
        int[][] m = new int[Multiply.LENGTH][Multiply.LENGTH];
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            int i = 0;
            while ((line = br.readLine()) != null && i < Multiply.LENGTH) {
                String[] numbers = line.split(";");
                for (int j = 0; j < Multiply.LENGTH; j++) {
                    m[i][j] = Integer.parseInt(numbers[j]);
                }
                i++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return m;
    }
}
