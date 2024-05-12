package org.example.task2;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PriorityBounceFrame extends JFrame {
    private BallCanvas canvas;
    public static final int WIDTH = 600;
    public static final int HEIGHT = 450;

    public PriorityBounceFrame() {
        this.setSize(WIDTH, HEIGHT);
        this.setTitle("Priority bounce program");

        this.canvas = new BallCanvas();
        System.out.println("In Frame Thread name = " + Thread.currentThread().getName());
        Container content = this.getContentPane();
        content.add(this.canvas, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.lightGray);

        JButton buttonStart = new JButton("Start");
        JButton buttonStop = new JButton("Stop");
        JTextField textField = new JTextField("Balls in Pocket: 0", 12);
        textField.setEditable(false);
        textField.setHorizontalAlignment(JTextField.CENTER);

        buttonStart.addActionListener(e -> {
            int max = 500;
            for (int i = 0; i < max; i++) {
                Ball b = new Ball(canvas, i < max - 1 ? Color.BLUE : Color.RED);
                canvas.add(b);

                BallThread ballThread = new BallThread(b);
                ballThread.setPriority(i < max - 1 ? Thread.MIN_PRIORITY : Thread.MAX_PRIORITY);
                ballThread.start();
            }
        });

        buttonStop.addActionListener(e -> System.exit(0));

        canvas.setTextField(textField);
        buttonPanel.add(buttonStart);
        buttonPanel.add(buttonStop);
        buttonPanel.add(textField);

        content.add(buttonPanel, BorderLayout.SOUTH);
    }
}
