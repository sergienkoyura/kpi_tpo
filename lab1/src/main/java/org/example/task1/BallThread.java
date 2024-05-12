package org.example.task1;

public class BallThread extends Thread {
    private Ball b;

    public BallThread(Ball b) {
        this.b = b;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i < 10000; i++) {
                b.move();
                System.out.println("Thread name = " + Thread.currentThread().getName());
                if (b.isInPocket()){
                    System.out.println("Ball is in the pocket! Stopping Thread " + Thread.currentThread().getName());
                    b.remove();
                    break;
                }
                Thread.sleep(5);
            }
        } catch (InterruptedException e) {

        }
    }
}
