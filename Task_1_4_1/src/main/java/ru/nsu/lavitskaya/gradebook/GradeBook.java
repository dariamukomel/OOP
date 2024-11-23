package ru.nsu.lavitskaya.gradebook;

import java.util.ArrayList;
import java.util.List;

/**
 * The GradeBook class represents a student's academic record, managing
 * their semesters and providing methods to calculate various academic metrics.
 */
public class GradeBook {
    private String name;
    private boolean isPaid;
    private List<Semester> semesters;
    private Integer qualificationWorkGrade;

    /**
     * Constructs a new GradeBook for a student.
     *
     * @param name    the name of the student
     * @param isPaid  indicates if the student is on a paid or budget education plan
     */
    public GradeBook(String name, boolean isPaid) {
        this.name = name;
        this.isPaid = isPaid;
        this.semesters = new ArrayList<>();
        this.qualificationWorkGrade = null;
    }

    /**
     * Adds a Semester to the student's grade book.
     *
     * @param semester the Semester object to be added
     */
    public void addSemester(Semester semester) {
        semesters.add(semester);
    }

    /**
     * Sets the grade for the student's qualification work.
     *
     * @param grade the grade of the qualification work, typically on a scale from 2 to 5
     */
    public void setQualificationWorkGrade(int grade) {
        this.qualificationWorkGrade = grade;
    }

    /**
     * Calculates the average score across all semesters.
     *
     * @return the average score, or 0.0 if there are no grades.
     */
    public double calculateAverageScore() {
        if (semesters.isEmpty()) {
            return 0.0;
        }
        double totalSum = 0.0;
        int totalGrades = 0;
        for (Semester semester : semesters) {
            totalSum += semester.getGrades().stream().mapToInt(Integer::intValue).sum();
            totalSum += semester.getExamResults().stream().mapToInt(Integer::intValue).sum();
            totalGrades += semester.getGrades().size();
            totalGrades += semester.getExamResults().size();
        }
        return totalGrades == 0 ? 0.0 : totalSum / totalGrades;
    }

    /**
     * Checks if the student can transfer from a paid to a budget education plan.
     *
     * @return true if the student can transfer to budget, false otherwise.
     */
    public boolean canTransferToBudget() {
        if (isPaid) {
            if (semesters.isEmpty()) {
                return true;
            }
            if (semesters.size() == 1) {
                return !semesters.getLast().getExamResults().contains(3);
            }
            return !semesters.getLast().getExamResults().contains(3)
                    && !semesters.get(semesters.size() - 2).getExamResults().contains(3);

        }
        return false;
    }

    /**
     * Checks if the student is eligible for a "Red Diploma".
     *
     * @return true if the student qualifies for a Red Diploma, false otherwise.
     */
    public boolean eligibleForRedDiploma() {
        if (qualificationWorkGrade != null && qualificationWorkGrade != 5) {
            return false;
        }
        int excellentCount = 0;
        int totalGrades = 0;

        for (Semester semester : semesters) {
            for (int grade : semester.getGrades()) {
                if (grade == 3) {
                    return false;
                }
                if (grade == 5) {
                    excellentCount++;
                }
                totalGrades++;
            }
            for (int result : semester.getExamResults()) {
                if (result == 3) {
                    return false;
                }
                if (result == 5) {
                    excellentCount++;
                }
                totalGrades++;
            }
        }
        return totalGrades == 0 || (double) excellentCount / totalGrades >= 0.75;
    }

    /**
     * Checks if the student is eligible for a scholarship based on their
     * performance in the latest semester.
     *
     * @return true if the student is eligible for a scholarship, false otherwise.
     */
    public boolean eligibleForScholarship() {
        if (!semesters.isEmpty()) {
            for (int grade : semesters.getLast().getGrades()) {
                if (grade != 5) {
                    return false;
                }
            }
            for (int result : semesters.getLast().getExamResults()) {
                if (result != 5) {
                    return false;
                }
            }
        }
        return true;
    }

}
