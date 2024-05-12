package org.example.task3;

import javax.swing.*;

public class Bounce {
    public static void main(String[] args) {
        JoinBounceFrame frame = new JoinBounceFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setVisible(true);
        System.out.println("Thread name = " + Thread.currentThread().getName());
    }
}