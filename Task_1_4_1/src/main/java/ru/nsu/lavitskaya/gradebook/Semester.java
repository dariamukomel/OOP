package ru.nsu.lavitskaya.gradebook;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The Semester class represents an academic semester for a student.
 * It holds the grades and exam results for that semester, allowing
 * the addition of new grades and the retrieval of the grades and exam results.
 */
public class Semester {
    private List<Integer> grades; // Оценки за семестр
    private List<Integer> examResults;

    /**
     * Constructs a new Semester object.
     */
    public Semester() {
        this.grades = new ArrayList<>();
        this.examResults = new ArrayList<>();
    }

    /**
     * Adds a grade to the semester.
     *
     * @param grade the grade to be added, typically on a scale from 2 to 5.
     */
    public void addGrade(int grade) {
        grades.add(grade);
    }

    /**
     * Adds an exam result to the semester.
     *
     * @param result the exam result to be added, typically on a scale from 2 to 5.
     */
    public void addExamResult(int result) {
        examResults.add(result);
    }

    /**
     * Retrieves an unmodifiable list of grades for the semester.
     *
     * @return an unmodifiable list of grades, which cannot be modified from outside this class.
     */
    public List<Integer> getGrades() {
        return Collections.unmodifiableList(grades);
    }

    /**
     * Retrieves an unmodifiable list of exam results for the semester.
     *
     * @return an unmodifiable list of exam results, which cannot be modified from outside
     *     this class.
     */
    public List<Integer> getExamResults() {
        return Collections.unmodifiableList(examResults);
    }
}
