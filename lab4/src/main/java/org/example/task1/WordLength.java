package org.example.task1;

public class WordLength {
    private double avg = 0;
    private long count = 0;

    public double getAvg() {
        return avg;
    }

    public long getCount() {
        return count;
    }

    public void increaseAverage(int length) {
        avg = avg * count + length;
        avg /= ++count;
    }

    public void addAvg(WordLength wordLength) {
        avg = avg * count + wordLength.getAvg() * wordLength.getCount();
        avg /= (count = count + wordLength.getCount());
    }
}
