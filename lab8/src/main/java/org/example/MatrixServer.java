package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ForkJoinPool;

import static org.example.MatrixService.breakIntoBlocks;

public class MatrixServer {
    public static void main(String[] args)  {
        if (args.length != 1) {
            System.err.println("Usage: java MatrixServer <port number>");
            System.exit(1);
        }

        int[][] serverMatrix1 = MatrixService.initialize("m1.txt");
        int[][] serverMatrix2 = MatrixService.initialize("m2.txt");

        int portNumber = Integer.parseInt(args[0]);
        try (
                ServerSocket serverSocket = new ServerSocket(portNumber);
                Socket clientSocket = serverSocket.accept();
                PrintWriter out =  new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        ) {
            String type;
            if ((type = in.readLine()) != null) {
                int[][] m1;
                int[][] m2;

                if (type.equals("1")) {
                    System.out.println("Matrices will be received from the client.\nWaiting for the matrices..");

                    m1 = MatrixService.initialize(in);
                    m2 = MatrixService.initialize(in);
                } else if (type.equals("2")) {
                    System.out.println("Matrices will be set now.\nSetting matrices on the server..");

                    m1 = serverMatrix1;
                    m2 = serverMatrix2;

                } else {
                    System.out.println("Server got wrong input, exiting..");
                    System.exit(1);
                    return;
                }
                System.out.println("Matrices are set!");

                int q = 2; // blocks in diagonal
                int m = MatrixService.LENGTH / q; // items in a block

                Matrix[][] matrix1 = breakIntoBlocks(new Matrix(m1), q, m);
                Matrix[][] matrix2 = breakIntoBlocks(new Matrix(m2), q, m);

                ForkJoinPool pool = new ForkJoinPool();
                long start = System.currentTimeMillis();
                Matrix res = pool.invoke(new MultiplyMatrixTask(matrix1, matrix2, q, m));
                System.out.printf("Multiplying -> time took: %d ms%n", (System.currentTimeMillis() - start));

                MatrixService.send(res, out);
            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}
