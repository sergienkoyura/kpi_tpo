package org.example;

import java.util.concurrent.RecursiveTask;

import static org.example.MatrixService.*;

public class MultiplyMatrixTask extends RecursiveTask<Matrix> {
    private Matrix[][] m1;
    private Matrix[][] m2;
    private int q;
    private int m;

    public MultiplyMatrixTask(Matrix[][] m1, Matrix[][] m2, int q, int m) {
        this.m1 = m1;
        this.m2 = m2;
        this.q = q;
        this.m = m;
    }

    @Override
    protected Matrix compute() {
        Matrix[][] result = new Matrix[q][q];
        if (m % q != 0 || m < LENGTH / 30) {
            return multiply(combine(m1, q, m), combine(m2, q, m), m * q);
        } else {
            int newM = m / q;
            for (int q1 = 0; q1 < q; q1++) {
                for (int q2 = 0; q2 < q; q2++) {
                    int i = q1;
                    int countI = 0;
                    MultiplyMatrixTask[] tasks = new MultiplyMatrixTask[q * q];
                    while (countI < q) { // iterations == number of blocks in the diagonal
                        if (countI == 0) {
                            result[q1][q2] = new Matrix(new int[m][m]);
                        }

                        if (i == q)
                            i = 0;

                        tasks[countI] = new MultiplyMatrixTask(breakIntoBlocks(m1[q1][i], q, newM), breakIntoBlocks(m2[i][q2], q, newM), q, newM);
                        tasks[countI].fork();
                        i++;
                        countI++;
                    }

                    countI = 0;
                    i = q1;
                    while (countI < q) {
                        result[q1][q2] = add(result[q1][q2], tasks[countI].join(),newM * q);
                        i++;
                        countI++;
                    }

                }
            }
        }

        return combine(result, q, m);
    }
}
