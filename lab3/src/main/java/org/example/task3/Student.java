package org.example.task3;

import java.util.*;

public class Student {
    private List<Integer> marks;

    public Student() {
        this.marks = new ArrayList<>();
    }

    public int getMark() {
        int sum = 0;
        for (int mark : marks) {
            sum += mark;
        }
        return sum;
    }

    public void add(int mark){
        marks.add(mark);
    }

    public int size() {
        return marks.size();
    }

    @Override
    public String toString() {
        return "Student{" +
                "marks=" + marks +
                '}';
    }
}
