package org.example.task3;

import java.util.ArrayList;
import java.util.List;

public class GradeBook {
    List<Student> students;

    public GradeBook(int students) {
        this.students = new ArrayList<>();
        for (int i = 0; i < students; i++) {
            this.students.add(new Student());
        }
    }

    public int getMark(int student) {
        return this.students.get(student).getMark();
    }

    public void add(int student, int mark) {
        this.students.get(student).add(mark);
    }

    public void print(int students, int threshold) {
        int count = 0;
        for (int i = 0; i < students; i++) {
            if (getMark(i) != threshold) {
                System.out.printf("student %d: %d%n", i, getMark(i));
                count++;

            }
        }
        System.out.printf("Got error %d times!%n", count);
    }
}
