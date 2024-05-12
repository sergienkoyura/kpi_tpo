public class Multiply {

    static final int LENGTH = 3000; // n
    static final int NUM_THREADS = 16; // p - processes, q * q
    static final int q = (int) Math.sqrt(NUM_THREADS); // blocks in diagonal / row
    static final int m = LENGTH / q; // items in the block

    public static void main(String[] args) {
        int[][] m1 = Generator.generate("m1.txt");
        int[][] m2 = Generator.generate("m2.txt");

        if (m1[0].length != m2.length) {
            System.out.println("First matrix should have the same number of columns as the number of rows in the second one!");
        } else {

            long start = System.currentTimeMillis();
            Matrix res = new Matrix(multiply(m1, m2));
            long end = System.currentTimeMillis();

            System.out.println("Plain: the process took " + (end - start) + " milliseconds");

            start = System.currentTimeMillis();
            Matrix res2 = new Matrix(multiplyMatrixStripe(m1, m2));
            end = System.currentTimeMillis();

            System.out.println("Stripe: the process took " + (end - start) + " milliseconds");

            Matrix[][] matrix1 = breakIntoBlocks(m1);
            Matrix[][] matrix2 = breakIntoBlocks(m2);

            start = System.currentTimeMillis();
            Matrix[][] res3 = foxMultiplication(matrix1, matrix2);
            end = System.currentTimeMillis();

            System.out.println("Fox: the process took " + (end - start) + " milliseconds");

            Matrix combined3 = combine(res3);

            System.out.println("Test: are same parallel and plain: " + res.isSame(res2));
            System.out.println("Test: are same fox and plain: " + combined3.isSame(res));
        }
    }

    static int[][] multiply(int[][] m1, int[][] m2) {
        int[][] result = new int[LENGTH][LENGTH];

        for (int f = 0; f < LENGTH; f++) {
            for (int i = 0; i < LENGTH; i++) {
                int tempResult = 0;
                for (int j = 0; j < LENGTH; j++) {
                    tempResult += m1[f][j] * m2[j][i];
                }
                result[f][i] = tempResult;
            }
        }
        return result;
    }

    static int[][] multiplyMatrixStripe(int[][] M1, int[][] M2) {
        int[][] result = new int[LENGTH][LENGTH];
        Thread[] threads = new Thread[NUM_THREADS];
        for (int t = 0; t < NUM_THREADS; t++) {
            int finalT = t;
            threads[t] = new Thread(() -> {
                // each thread work independently jumping over other threads
                for (int i = finalT; i < LENGTH; i += NUM_THREADS) {
                    int count = 0;
                    int j = i;
                    while (count++ < LENGTH) { // each item in a row
                        int sum = 0;
                        for (int k = 0; k < LENGTH; k++) {
                            sum += M1[i][k] * M2[k][j];
                        }
                        result[i][j] = sum;
                        if (--j < 0) // jump to the end when it comes to the start
                            j = LENGTH - 1;
                    }
                }
            });
            threads[t].start();
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


    static Matrix[][] breakIntoBlocks(int[][] matrix) {
        Matrix[][] result = new Matrix[q][q];

        // iterate through matrix struct
        for (int i = 0; i < q; i++) {
            for (int j = 0; j < q; j++) {
                int t = i * q + j; // thread number

                // calculate coordinates of each block
                int fromRow = ((int) (double) (t / q)) * m;
                int toRow = fromRow + m;
                int fromColumn = (t % q) * m;
                int toColumn = fromColumn + m;

                int[][] toSet = new int[m][m];
                int cI = 0;
                int cJ;
                for (int fromR = fromRow; fromR < toRow; fromR++, cI++) {
                    cJ = 0;
                    for (int fromC = fromColumn; fromC < toColumn; fromC++, cJ++) {
                        toSet[cI][cJ] = matrix[fromR][fromC];
                    }
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
                        result[finalRow][finalCol] = add(result[finalRow][finalCol], multiply(a[finalRow][i], b[i][finalCol])); // C[i][j] = C[i][j] + A[i][i+1] * B[i+1][j]
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


    static Matrix multiply(Matrix M1, Matrix M2) {
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


    static Matrix add(Matrix M1, Matrix M2) {
        Matrix result = new Matrix(new int[m][m]);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                result.getMatrix()[i][j] = M1.getMatrix()[i][j] + M2.getMatrix()[i][j];
            }
        }
        return result;
    }

    private static Matrix combine(Matrix[][] matrix) {
        int[][] res = new int[LENGTH][LENGTH];

        // get block
        for (int i = 0; i < q; i++) {
            for (int j = 0; j < q; j++) {

                // each element of the block
                for (int k = 0; k < m; k++) {
                    for (int l = 0; l < m; l++) {
                        res[i * m + k][j * m + l] = matrix[i][j].getMatrix()[k][l];
                    }
                }
            }
        }

        return new Matrix(res);
    }
}