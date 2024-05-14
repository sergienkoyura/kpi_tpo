package org.example.task3;

import java.util.*;

public class Main {

    private static final int GROUPS = 3;
    private static final int TEACHERS = GROUPS + 1;
    private static final int STUDENTS_GROUP = 100;
    private static final int STUDENTS = STUDENTS_GROUP * GROUPS;
    private static final int WEEKS = 20;
    private static final int MARK = 5;
    private static final int THRESHOLD = WEEKS * MARK;

    public static void main(String[] args) {
        System.out.println("Max: " + THRESHOLD);
        GradeBook gradeBook = new GradeBook(STUDENTS);

        Thread[] teachers = new Thread[TEACHERS];

        putMarks(gradeBook, teachers);
        gradeBook.print(STUDENTS, THRESHOLD);
    }

    static void putMarks(GradeBook gradeBook, Thread[] teachers) {
        List<Integer> lectorMarks = new ArrayList<>();
        for (int i = 0; i < STUDENTS; i++) {
            lectorMarks.add(i);
        }
        Collections.shuffle(lectorMarks);


        for (int t = 0; t < TEACHERS; t++) {
            if (t < GROUPS) {
                int group = t;
                teachers[t] = new Thread(() -> {
                    for (int week = 0; week < WEEKS / 2; week++) {
                        for (int student = group; student < STUDENTS; student += GROUPS) {
                            gradeBook.add(student, MARK);
                        }
                    }
                });
            } else {

                teachers[t] = new Thread(() -> {
                    for (int week = 0; week < WEEKS / 2; week++) {
                        for (int student : lectorMarks) {
                            gradeBook.add(student, MARK);
                        }
                    }
                });
            }
        }

        for (int t = 0; t < TEACHERS; t++) {
            teachers[t].start();
        }

        for (int t = 0; t < TEACHERS; t++) {
            try {
                teachers[t].join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
