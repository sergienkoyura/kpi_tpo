package org.example.task3;

import javax.swing.*;
import java.awt.*;

public class JoinBounceFrame extends JFrame {
    private BallCanvas canvas;
    public static final int WIDTH = 600;
    public static final int HEIGHT = 450;
    static int count = 0;
    static BallThread lastThread = null;

    public JoinBounceFrame() {
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
            Ball b = new Ball(canvas, count % 5 != 0 ? count % 5 != 1 ? count % 5 != 2 ? count % 5 != 3 ? Color.PINK : Color.GREEN : Color.BLUE : Color.YELLOW : Color.RED);
            count++;
            canvas.add(b);
            BallThread ballThread = new BallThread(b);
            ballThread.start();
            if (lastThread != null){
                ballThread.setPrevThread(lastThread);
            }
            lastThread = ballThread;
        });

        buttonStop.addActionListener(e -> System.exit(0));

        canvas.setTextField(textField);
        buttonPanel.add(buttonStart);
        buttonPanel.add(buttonStop);
        buttonPanel.add(textField);

        content.add(buttonPanel, BorderLayout.SOUTH);
    }
}
