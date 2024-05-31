#include "mpi.h"
#include <stdio.h>
#include <stdlib.h>
#define N 1500 /* number of rows in matrix A */
#define MASTER 0 /* taskid of first task */
#define FROM_MASTER 1 /* setting a message type */
#define FROM_WORKER 2 /* setting a message type */

double **initMatrix() {
    double *items = (double *) malloc(N * N * sizeof(double));
    double **matrix = (double **) malloc(N * sizeof(double *));
    matrix[0] = items;
    for (int i = 1; i < N; i++) {
        matrix[i] = matrix[i - 1] + N;
    }
    return matrix;
}


void multiplyBlocking(int numtasks, int taskid) {
    int numworkers,
            rows,
            averow,
            extra,
            offset;

    double **a = initMatrix();
    double **b = initMatrix();
    double **c = initMatrix();
    double start;
    MPI_Status status;

    numworkers = numtasks - 1;
    if (taskid == MASTER) {
        start = MPI_Wtime();
        printf("Main has started with %d tasks.\n", numtasks);
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                a[i][j] = 10;
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                b[i][j] = 10;

        averow = N / numworkers;
        extra = N % numworkers;
        offset = 0;
        for (int dest = 1; dest <= numworkers; dest++) {
            rows = (dest <= extra) ? averow + 1 : averow;
            printf("Sending %d rows to task %d, offset: %d\n", rows, dest, offset);
            MPI_Send(&offset, 1, MPI_INT, dest, FROM_MASTER,MPI_COMM_WORLD);
            MPI_Send(&rows, 1, MPI_INT, dest, FROM_MASTER,MPI_COMM_WORLD);
            MPI_Send(a[offset], rows * N, MPI_DOUBLE, dest,FROM_MASTER,MPI_COMM_WORLD);
            MPI_Send(b[0], N * N, MPI_DOUBLE, dest, FROM_MASTER,MPI_COMM_WORLD);
            offset = offset + rows;
        }
        /* Receive results from worker tasks */
        for (int source = 1; source <= numworkers; source++) {
            MPI_Recv(&offset, 1, MPI_INT, source, FROM_WORKER,MPI_COMM_WORLD, &status);
            MPI_Recv(&rows, 1, MPI_INT, source, FROM_WORKER,MPI_COMM_WORLD, &status);
            MPI_Recv(c[offset], rows * N, MPI_DOUBLE, source,FROM_WORKER,MPI_COMM_WORLD, &status);
            printf("Received results from task %d\n", source);
        }
        /* Print results */
        printf("\n****\n");
        printf("First 10:");
        printf("\n");
        for (int i = 0; i < 10; i++)
            printf("%6.2f ", c[0][i]);
        printf("\n********\n");
        printf("Done.\n");

        printf("Time Took: %d ms\n", (int) ((MPI_Wtime() - start) * 1000));
    }
    /******** worker task *****************/
    else {
        /* if (taskid > MASTER) */
        MPI_Recv(&offset, 1, MPI_INT, MASTER, FROM_MASTER,MPI_COMM_WORLD, &status);
        MPI_Recv(&rows, 1, MPI_INT, MASTER, FROM_MASTER, MPI_COMM_WORLD, &status);
        MPI_Recv(a[0], rows * N, MPI_DOUBLE, MASTER, FROM_MASTER, MPI_COMM_WORLD, &status);
        MPI_Recv(b[0], N * N, MPI_DOUBLE, MASTER, FROM_MASTER,MPI_COMM_WORLD, &status);
        for (int k = 0; k < N; k++) {
            for (int i = 0; i < rows; i++) {
                c[i][k] = 0.0;
                for (int j = 0; j < N; j++)
                    c[i][k] = c[i][k] + a[i][j] * b[j][k];
            }
        }
        MPI_Send(&offset, 1, MPI_INT, MASTER, FROM_WORKER, MPI_COMM_WORLD);
        MPI_Send(&rows, 1, MPI_INT, MASTER, FROM_WORKER, MPI_COMM_WORLD);
        MPI_Send(c[0], rows * N, MPI_DOUBLE, MASTER, FROM_WORKER, MPI_COMM_WORLD);
    }
    MPI_Finalize();
}

void multiplyNonBlocking(int numtasks, int taskid) {
    int numworkers,
            rows,
            averow,
            extra,
            offset;

    double **a = initMatrix();
    double **b = initMatrix();
    double **c = initMatrix();
    double start;
    MPI_Status status;
    MPI_Request send_request, r_r_offset, r_r_rows;

    numworkers = numtasks - 1;
    if (taskid == MASTER) {
        start = MPI_Wtime();
        printf("Non-blocking says: main has started with %d tasks.\n", numtasks);
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                a[i][j] = 10;
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                b[i][j] = 10;

        averow = N / numworkers;
        extra = N % numworkers;
        offset = 0;
        for (int dest = 1; dest <= numworkers; dest++) {
            rows = (dest <= extra) ? averow + 1 : averow;
            printf("Sending %d rows to task %d, offset: %d\n", rows, dest, offset);
            MPI_Isend(&offset, 1, MPI_INT, dest, FROM_MASTER,MPI_COMM_WORLD, &send_request);
            MPI_Isend(&rows, 1, MPI_INT, dest, FROM_MASTER,MPI_COMM_WORLD, &send_request);
            MPI_Isend(a[offset], rows * N, MPI_DOUBLE, dest,FROM_MASTER,MPI_COMM_WORLD, &send_request);
            MPI_Isend(b[0], N * N, MPI_DOUBLE, dest, FROM_MASTER,MPI_COMM_WORLD, &send_request);
            offset = offset + rows;
        }

        /* Receive results from worker tasks */
        MPI_Request receive_request[numworkers];
        for (int source = 1; source <= numworkers; source++) {
            MPI_Irecv(&offset, 1, MPI_INT, source, FROM_WORKER,MPI_COMM_WORLD, &r_r_offset);
            MPI_Irecv(&rows, 1, MPI_INT, source, FROM_WORKER,MPI_COMM_WORLD, &r_r_rows);
            MPI_Wait(&r_r_offset, MPI_STATUS_IGNORE);
            MPI_Wait(&r_r_rows, MPI_STATUS_IGNORE);
            MPI_Irecv(c[offset], rows * N, MPI_DOUBLE, source,FROM_WORKER,MPI_COMM_WORLD, &receive_request[source - 1]);
            // MPI_Wait(&r_r_final, MPI_STATUS_IGNORE);
            printf("Received results from task %d and offset %d\n", source, offset);
        }

        MPI_Waitall(numworkers, &receive_request[0], MPI_STATUS_IGNORE);

        /* Print results */
        printf("\n****\n");
        printf("First 10:");
        printf("\n");
        for (int i = 0; i < 10; i++)
            printf("%6.2f ", c[0][i]);
        printf("\n********\n");
        printf("Done.\n");

        printf("Time Took: %d ms\n", (int) ((MPI_Wtime() - start) * 1000));
    }
    /******** worker task *****************/
    else {
        /* if (taskid > MASTER) */
        MPI_Recv(&offset, 1, MPI_INT, MASTER, FROM_MASTER,MPI_COMM_WORLD, &status);
        MPI_Recv(&rows, 1, MPI_INT, MASTER, FROM_MASTER, MPI_COMM_WORLD, &status);
        MPI_Recv(a[0], rows * N, MPI_DOUBLE, MASTER, FROM_MASTER, MPI_COMM_WORLD, &status);
        MPI_Recv(b[0], N * N, MPI_DOUBLE, MASTER, FROM_MASTER,MPI_COMM_WORLD, &status);
        for (int k = 0; k < N; k++) {
            for (int i = 0; i < rows; i++) {
                c[i][k] = 0.0;
                for (int j = 0; j < N; j++)
                    c[i][k] = c[i][k] + a[i][j] * b[j][k];
            }
        }

        MPI_Isend(&offset, 1, MPI_INT, MASTER, FROM_WORKER, MPI_COMM_WORLD, &send_request);
        MPI_Isend(&rows, 1, MPI_INT, MASTER, FROM_WORKER, MPI_COMM_WORLD, &send_request);
        MPI_Isend(c[0], rows * N, MPI_DOUBLE, MASTER, FROM_WORKER, MPI_COMM_WORLD, &send_request);
    }
    MPI_Finalize();
}

int main(int argc, char *argv[]) {
    int numtasks,
            taskid,
            rc;

    MPI_Init(&argc, &argv);
    MPI_Comm_size(MPI_COMM_WORLD, &numtasks);
    MPI_Comm_rank(MPI_COMM_WORLD, &taskid);
    if (numtasks < 2) {
        printf("Need at least two MPI tasks. Quitting...\n");
        MPI_Abort(MPI_COMM_WORLD, rc);
        exit(1);
    }

    // multiplyBlocking(numtasks, taskid);
    multiplyNonBlocking(numtasks, taskid);
}
