#include "mpi.h"
#include <stdio.h>
#include <stdlib.h>
#define N 500 /* number of rows in matrix A */
#define MASTER 0 /* taskid of first task */

double **initMatrix() {
    double *items = (double *) malloc(N * N * sizeof(double));
    double **matrix = (double **) malloc(N * sizeof(double *));
    matrix[0] = items;
    for (int i = 1; i < N; i++) {
        matrix[i] = matrix[i - 1] + N;
    }
    return matrix;
}


void multiplyCollectiveOM(int numtasks, int taskid) {
    double **a = initMatrix();
    double **a_p = initMatrix();
    double **b = initMatrix();
    double **temp = initMatrix();
    double **c = initMatrix();

    int sizeToProcess = N / numtasks;

    int sendSize = N * sizeToProcess;
    int getSize = sendSize;

    double start = 0;
    if (taskid == MASTER) {
        start = MPI_Wtime();
        printf("Main has started with %d tasks.\n", numtasks);
        printf("Initial Matrix size: %dx%d\n", N, N);

        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                a[i][j] = 10;
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                b[i][j] = 10;
    }

    MPI_Scatter(
        a[0], sendSize, MPI_DOUBLE,
        a_p[0], getSize, MPI_DOUBLE,
        0, MPI_COMM_WORLD
    );

    MPI_Bcast(
        b[0], N * N, MPI_DOUBLE,
        0, MPI_COMM_WORLD
    );

    for (int k = 0; k < N; k++) {
        for (int i = 0; i < sizeToProcess; i++) {
            temp[i][k] = 0.0;
            for (int j = 0; j < N; j++)
                temp[i][k] = temp[i][k] + a_p[i][j] * b[j][k];
        }
    }

    MPI_Gather(
        temp[0], sendSize, MPI_DOUBLE,
        c[0], getSize, MPI_DOUBLE,
        0, MPI_COMM_WORLD
    );


    if (taskid == MASTER) {
        printf("Time Took: %d ms\n", (int) ((MPI_Wtime() - start) * 1000));

        printf("\n****\n");
        printf("First 10:");
        printf("\n");
        for (int i = 0; i < 10; i++)
            printf("%6.2f ", c[0][i]);
        printf("\nLast 10:\n");
        for (int i = N - 10; i < N; i++)
            printf("%6.2f ", c[N - 1][i]);
        printf("\n********\n");
        printf("Done.\n");
    }

    MPI_Finalize();
}


void multiplyCollectiveOffsetsOM(int numtasks, int taskid) {
    int rows,
            averow,
            extra,
            offset;

    double **a = initMatrix();
    double **a_p = initMatrix();
    double **b = initMatrix();
    double **temp = initMatrix();
    double **c = initMatrix();

    int sizes[numtasks];
    int offsets[numtasks];

    averow = N / numtasks;
    extra = N % numtasks;
    offset = 0;
    for (int dest = 0; dest < numtasks; dest++) {
        rows = ((dest < extra) ? averow + 1 : averow) * N;
        sizes[dest] = rows;
        offsets[dest] = offset;
        offset = offset + rows;
    }

    double start = 0;
    if (taskid == MASTER) {
        start = MPI_Wtime();
        printf("Main has started with %d tasks.\n", numtasks);
        printf("Initial Matrix size: %dx%d\n", N, N);

        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                a[i][j] = 10;
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                b[i][j] = 10;
    }


    MPI_Scatterv(
        a[0], sizes, offsets, MPI_DOUBLE,
        a_p[0], sizes[taskid], MPI_DOUBLE,
        0, MPI_COMM_WORLD
    );

    MPI_Bcast(
        b[0], N * N, MPI_DOUBLE,
        0, MPI_COMM_WORLD
    );

    for (int k = 0; k < N; k++) {
        for (int i = 0; i < sizes[taskid] / N; i++) {
            temp[i][k] = 0.0;
            for (int j = 0; j < N; j++)
                temp[i][k] = temp[i][k] + a_p[i][j] * b[j][k];
        }
    }

    MPI_Gatherv(
        temp[0], sizes[taskid], MPI_DOUBLE,
        c[0], sizes, offsets, MPI_DOUBLE,
        0, MPI_COMM_WORLD
    );


    if (taskid == MASTER) {
        printf("Time Took: %d ms\n", (int) ((MPI_Wtime() - start) * 1000));

        printf("\n****\n");
        printf("First 10:");
        printf("\n");
        for (int i = 0; i < 10; i++)
            printf("%6.2f ", c[0][i]);
        printf("\nLast 10:\n");
        for (int i = N - 10; i < N; i++)
            printf("%6.2f ", c[N - 1][i]);
        printf("\n********\n");
        printf("Done.\n");


        // for (int i = 0; i < N; i++) {
        //     for (int j = 0; j < N; j++) {
        //         printf("%6.2f ", c[i][j]);
        //     }
        //     printf("\n");
        // }
    }

    MPI_Finalize();
}


void multiplyCollectiveOffsetMM(int numtasks, int taskid) {
    int rows,
            averow,
            extra,
            offset;

    double **a = initMatrix();
    double **a_p = initMatrix();
    double **b = initMatrix();
    double **temp = initMatrix();
    double **c = initMatrix();

    int sizes[numtasks];
    int offsets[numtasks];

    averow = N / numtasks;
    extra = N % numtasks;
    offset = 0;
    for (int dest = 0; dest < numtasks; dest++) {
        rows = ((dest < extra) ? averow + 1 : averow) * N;
        sizes[dest] = rows;
        offsets[dest] = offset;
        offset = offset + rows;
    }

    double start = 0;
    if (taskid == MASTER) {
        start = MPI_Wtime();
        printf("Main has started with %d tasks.\n", numtasks);
        printf("Initial Matrix size: %dx%d\n", N, N);

        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                a[i][j] = 10;
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                b[i][j] = 10;
    }

    MPI_Scatterv(
        a[0], sizes, offsets, MPI_DOUBLE,
        a_p[0], sizes[taskid], MPI_DOUBLE,
        0, MPI_COMM_WORLD
    );

    MPI_Bcast(
        b[0], N * N, MPI_DOUBLE,
        0, MPI_COMM_WORLD
    );

    for (int k = 0; k < N; k++) {
        for (int i = 0; i < sizes[taskid] / N; i++) {
            temp[i][k] = 0.0;
            for (int j = 0; j < N; j++)
                temp[i][k] = temp[i][k] + a_p[i][j] * b[j][k];
        }
    }

    MPI_Allgatherv(
        temp[0], sizes[taskid], MPI_DOUBLE,
        c[0], sizes, offsets, MPI_DOUBLE,
        MPI_COMM_WORLD
    );

    MPI_Barrier(MPI_COMM_WORLD);

    if (taskid == MASTER) {
        printf("Time Took: %d ms\n", (int) ((MPI_Wtime() - start) * 1000));
    }

    // printf("\n****\n");
    // printf("First 10:");
    // printf("\n");
    // for (int i = 0; i < 10; i++)
    //     printf("%6.2f ", c[0][i]);
    // printf("\nLast 10:\n");
    // for (int i = N - 10; i < N; i++)
    //     printf("%6.2f ", c[N - 1][i]);
    // printf("\n********\n");
    // printf("Done.\n");


    MPI_Finalize();
}


int main(int argc, char *argv[]) {
    int numtasks,
            taskid;

    MPI_Init(&argc, &argv);
    MPI_Comm_size(MPI_COMM_WORLD, &numtasks);
    MPI_Comm_rank(MPI_COMM_WORLD, &taskid);

    // multiplyCollectiveOM(numtasks, taskid);
    multiplyCollectiveOffsetsOM(numtasks, taskid);
    // multiplyCollectiveOffsetMM(numtasks, taskid);
    return 0;
}
