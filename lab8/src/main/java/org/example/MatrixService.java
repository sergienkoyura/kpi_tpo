package org.example;

import java.io.*;
import java.util.Arrays;
import java.util.Random;

public class MatrixService {
    static final int LENGTH = 500;
    public static void main(String[] args) {
        generate("m1.txt");
        generate("m2.txt");
    }

    static void generate(String filename){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, false))) {
            for (int i = 0; i < LENGTH; i++) {
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < LENGTH; j++) {
                    sb.append(new Random().nextInt(100) - 50).append(";");
                }
                sb.deleteCharAt(sb.length() - 1);
                bw.write(sb + System.lineSeparator());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static int[][] initialize(String filename) {
        int[][] m = new int[LENGTH][LENGTH];
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            int i = 0;
            while ((line = br.readLine()) != null && i < LENGTH) {
                String[] numbers = line.split(";");
                for (int j = 0; j < LENGTH; j++) {
                    m[i][j] = Integer.parseInt(numbers[j]);
                }
                i++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return m;
    }

    static void send(String filename, PrintWriter out) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                out.println(line);
            }
        }
    }

    static void send(Matrix m, PrintWriter out) {
        for (int i = 0; i < LENGTH; i++) {
            String matrixRow = Arrays.toString(m.getMatrix()[i]);
            matrixRow = matrixRow
                    .replaceAll(", ", ";")
                    .replaceAll("\\[", "")
                    .replaceAll("]", "");
            out.println(matrixRow);
        }
    }

    static int[][] initialize(BufferedReader in) throws IOException {
        int[][] m = new int[LENGTH][LENGTH];
        for (int i = 0; i < LENGTH; i++) {
            String line = in.readLine();
            String[] numbers = line.split(";");
            for (int j = 0; j < LENGTH; j++) {
                m[i][j] = Integer.parseInt(numbers[j]);
            }
        }
        return m;
    }



    static Matrix[][] breakIntoBlocks(Matrix matrix, int q, int m) {
        Matrix[][] result = new Matrix[q][q];

        // iterate through matrix struct
        for (int i = 0; i < q; i++) {
            for (int j = 0; j < q; j++) {
                int t = i * q + j; // iteration

                // calculate coordinates of each block
                int fromRow = ((int) (double) (t / q)) * m;
                int toRow = fromRow + m;
                int fromColumn = (t % q) * m;
                int toColumn = fromColumn + m;

                int[][] toSet = new int[m][m];
                int cI = 0;
                for (int fromR = fromRow; fromR < toRow; fromR++, cI++) {
                    toSet[cI] = Arrays.copyOfRange(matrix.getMatrix()[fromR], fromColumn, toColumn);
                }

                result[i][j] = new Matrix(toSet);
            }
        }
        return result;
    }


    static Matrix multiply(Matrix M1, Matrix M2, int m) {
        Matrix result = new Matrix(new int[m][m]);
        for (int f = 0; f < m; f++) {
            for (int i = 0; i < m; i++) {
                int tempResult = 0;
                for (int j = 0; j < m; j++) {
                    tempResult += M1.getMatrix()[f][j] * M2.getMatrix()[j][i];
                }
                result.getMatrix()[f][i] = tempResult;
            }
        }
        return result;
    }

    static Matrix multiply(int[][] m1, int[][] m2) {
        Matrix result = new Matrix(new int[LENGTH][LENGTH]);
        for (int f = 0; f < LENGTH; f++) {
            for (int i = 0; i < LENGTH; i++) {
                int tempResult = 0;
                for (int j = 0; j < LENGTH; j++) {
                    tempResult += m1[f][j] * m2[j][i];
                }
                result.getMatrix()[f][i] = tempResult;
            }
        }
        return result;
    }


    static Matrix add(Matrix M1, Matrix M2, int m) {
        Matrix result = new Matrix(new int[m][m]);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                result.getMatrix()[i][j] = M1.getMatrix()[i][j] + M2.getMatrix()[i][j];
            }
        }
        return result;
    }

    static Matrix combine(Matrix[][] matrix, int q, int m) {
        int[][] res = new int[m * q][m * q];

        // get block
        for (int i = 0; i < q; i++) {
            for (int j = 0; j < q; j++) {

                // each element of the block
                for (int k = 0; k < m; k++) {
                    System.arraycopy(matrix[i][j].getMatrix()[k], 0, res[k + i * m], j * m, m);
                }
            }
        }

        return new Matrix(res);
    }
}
