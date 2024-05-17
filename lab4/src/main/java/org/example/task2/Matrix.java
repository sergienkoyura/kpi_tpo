package org.example.task2;

public class Matrix {
    private int[][] matrix;

    public Matrix(int[][] matrix) {
        this.matrix = matrix;
    }

    public int[][] getMatrix() {
        return matrix;
    }


    public void print() {
        for (int[] row : matrix) {
            for (int i : row) {
                System.out.print(i + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public boolean isSame(Matrix res){
        for (int i = 0; i < res.getMatrix().length; i++) {
            for (int j = 0; j < res.getMatrix().length; j++) {
                if (this.matrix[i][j] != res.getMatrix()[i][j])
                    return false;
            }
        }
        return true;
    }

}
