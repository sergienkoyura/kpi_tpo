package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class MatrixClient {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java MatrixClient <host name> <port number>");
            System.exit(1);
        }
        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);
        System.out.printf("Matrix size: %d%n%n", MatrixService.LENGTH);
        System.out.print("Choose the type:\n(1) get matrices from the client\n(2) get matrices from the server\n1/2: ");
        try (
                Socket echoSocket = new Socket(hostName, portNumber);
                PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))
        ) {
            String userInput;
            if ((userInput = stdIn.readLine()) != null) {
                long start = System.currentTimeMillis();

                if (userInput.equals("1")){
                    out.println(userInput);
                    MatrixService.send("m1.txt", out);
                    MatrixService.send("m2.txt", out);
                } else if (userInput.equals("2")){
                    out.println(userInput);
                } else {
                    System.out.println("Wrong input, exiting..");
                    System.exit(1);
                }

                Matrix result = new Matrix(MatrixService.initialize(in));
                System.out.printf("Time took: %d ms%n", System.currentTimeMillis() - start);


                Matrix testResult = MatrixService.multiply(
                        MatrixService.initialize("m1.txt"),
                        MatrixService.initialize("m2.txt")
                );

//                result.print();
//                testResult.print();
                System.out.println("Are same: " + result.isSame(testResult));

            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + hostName);
            System.exit(1);
        }
    }
}
