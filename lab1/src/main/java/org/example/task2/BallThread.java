package org.example.task2;

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
                System.out.println("Running: " + Thread.currentThread().getName());
                if (b.isInPocket()){
                    b.remove();
                    break;
                }
                Thread.sleep(5);
            }
        } catch (InterruptedException e) {

        }
    }
}
