package org.example.task3;

public class BallThread extends Thread {
    private Ball b;
    private BallThread prevThread = null;
    public BallThread(Ball b) {
        this.b = b;
    }

    public void setPrevThread(BallThread prevThread) {
        this.prevThread = prevThread;
    }


    @Override
    public void run() {
        try {
            if (prevThread != null){
                prevThread.join();
            }
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
