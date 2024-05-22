package org.example.task2;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;

public class Multiply {

    // 2187 - 9, 2048 - 4, 16
    static final int LENGTH = 1000; // n
    static final int NUM_THREADS = 4; // p - processes, q * q
    static final int q = (int) Math.sqrt(NUM_THREADS); // blocks in diagonal / row
    static final int m = LENGTH / q; // items in the block

    public static void main(String[] args) {
        System.out.printf("%d items, %d threads%n", LENGTH, NUM_THREADS);
        int[][] m1 = Generator.generate("m1.txt");
        int[][] m2 = Generator.generate("m2.txt");

        int avg1 = 0;
        int avg2 = 0;

        for (int i = 0; i < 10; i++) {
            Matrix[][] matrix1 = breakIntoBlocks(new Matrix(m1), q, m);
            Matrix[][] matrix2 = breakIntoBlocks(new Matrix(m2), q, m);

            long start = System.currentTimeMillis();
            Matrix[][] res3 = foxMultiplication(matrix1, matrix2);
            Matrix combined3 = combine(res3, q, m);
            long end = System.currentTimeMillis();

            int t1 = (int) (end - start);
            avg1 = (avg1 * i + t1) / (i + 1);

            System.out.println("Fox: the process took " + t1 + " milliseconds");


            ForkJoinPool pool = new ForkJoinPool();
            start = System.currentTimeMillis();
            Matrix res4 = pool.invoke(new MultiplyMatrixTask(matrix1, matrix2, q, m));
            end = System.currentTimeMillis();

            int t2 = (int) (end - start);
            avg2 = (avg2 * i + t2) / (i + 1);
            System.out.println("FJ: the process took " + t2 + " milliseconds");
            System.out.println("Test: are same FJ and fox: " + res4.isSame(combined3));
        }

        System.out.printf("Size: %d, threads: %d, avg fox: %d ms, avg fj fox: %d ms%n", LENGTH, NUM_THREADS, avg1, avg2);

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

    static Matrix[][] foxMultiplication(Matrix[][] a, Matrix[][] b) {
        Matrix[][] result = new Matrix[q][q];

        Thread[] threads = new Thread[NUM_THREADS];

        // iterate through matrix struct
        for (int q1 = 0; q1 < q; q1++) {
            for (int q2 = 0; q2 < q; q2++) {
                int t = q1 * q + q2; // thread number
                int finalRow = q1;
                int finalCol = q2;
                threads[t] = new Thread(() -> {

                    int i = finalRow;
                    int countI = 0;
                    while (countI < q) { // iterations == number of blocks in the diagonal
                        if (countI == 0) {
                            result[finalRow][finalCol] = new Matrix(new int[m][m]);
                        }

                        if (i == q)
                            i = 0;
                        result[finalRow][finalCol] = add(result[finalRow][finalCol], multiply(a[finalRow][i], b[i][finalCol], m), m); // C[i][j] = C[i][j] + A[i][i+1] * B[i+1][j]
                        i++;
                        countI++;
                    }
                });

                threads[t].start();

            }
        }

        for (int t = 0; t < NUM_THREADS; t++) {
            try {
                threads[t].join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
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