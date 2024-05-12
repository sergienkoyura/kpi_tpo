package org.example.task1;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class BallCanvas extends JPanel {
    private ArrayList<Ball> balls = new ArrayList<>();
    private int ballsInPocketsCount = 0;
    private JTextField textField;


    public void add(Ball b) {
        this.balls.add(b);
    }

    public void remove(Ball b){
        this.balls.remove(b);
    }

    public void setTextField(JTextField textField) {
        this.textField = textField;
    }

    public void updateCounter() {
        textField.setText("Balls in Pocket: " + ++ballsInPocketsCount);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        this.setBackground(new Color(79, 64, 2));

        // Draw table border
        g2.setColor(new Color(32, 124, 0));
        g2.fillRect(20, 20, getWidth() - 40, getHeight() - 40);

        g2.setColor(Color.BLACK);
        g2.drawRect(20, 20, getWidth() - 40, getHeight() - 40);


        // Draw pockets
        drawPocket(g2, 20, 20);
        drawPocket(g2, getWidth() / 2, 20);
        drawPocket(g2, getWidth() - 20, 20);
        drawPocket(g2, 20, getHeight() - 20);
        drawPocket(g2, getWidth() / 2, getHeight() - 20);
        drawPocket(g2, getWidth() - 20, getHeight() - 20);

        for (Ball b : balls) {
            b.draw(g2);
        }
    }

    private void drawPocket(Graphics2D g2, int x, int y) {
        int pocketSize = 20;
        g2.fillOval(x - pocketSize / 2, y - pocketSize / 2, pocketSize, pocketSize);
    }
}
