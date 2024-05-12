package org.example.task1;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Random;

public class Ball {
    private Component canvas;
    private static final int XSIZE = 20;
    private static final int YSIZE = 20;
    private int x = 20;
    private int y = 20;
    private int dx = 2;
    private int dy = 2;
    public Ball(Component c) {
        this.canvas = c;
        if (Math.random() < 0.5) {
            x = new Random().nextInt(this.canvas.getWidth() - 60) + 30;
            y = 20;
        } else {
            x = 20;
            y = new Random().nextInt(this.canvas.getHeight() - 60) + 30;
        }
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.darkGray);
        g2.fill(new Ellipse2D.Double(x, y, XSIZE, YSIZE));
    }

    public void move() {
        x += dx;
        y += dy;
        if (x < 20) {
            x = 20;
            dx = -dx;
        }
        if (x + XSIZE > this.canvas.getWidth() - 20) {
            x = this.canvas.getWidth() - 20 - XSIZE;
            dx = -dx;
        }

        if (y < 20) {
            y = 20;
            dy = -dy;
        }
        if (y + YSIZE > this.canvas.getHeight() - 20) {
            y = this.canvas.getHeight() - 20 - YSIZE;
            dy = -dy;
        }
        repaintCanvas();
    }

    public boolean isInPocket() {
        int border = 20;
        int width = this.canvas.getWidth();
        int height = this.canvas.getHeight();

        boolean isLeftTopPocket = (x == border || x == border + 1) && (y == border || y == border + 1);
        boolean isLeftBottomPocket = (x == border || x == border + 1) && (y == height - border - YSIZE || y == height - border - YSIZE - 1);
        boolean isRightTopPocket = (x == width - border - XSIZE || x == width - border - XSIZE - 1) && (y == border || y == border + 1);
        boolean isRightBottomPocket = (x == width - border - XSIZE || x == width - border - XSIZE - 1) && (y == height - border - YSIZE || y == height - border - YSIZE - 1);

        boolean isCenterTopPocket = Math.abs((x - width / 2) + XSIZE / 2) <= 1 && y == border;
        boolean isCenterBottomPocket = Math.abs((x - width / 2) + XSIZE / 2) <= 1 && y == height - border - YSIZE;

        return isLeftTopPocket || isLeftBottomPocket || isRightTopPocket || isRightBottomPocket || isCenterTopPocket || isCenterBottomPocket;
    }

    private void incrementCounter(){
        ((BallCanvas) canvas).updateCounter();
    }

    public void remove(){
        incrementCounter();
        ((BallCanvas) canvas).remove(this);
        repaintCanvas();
    }

    private void repaintCanvas() {
        canvas.repaint();
    }
}
