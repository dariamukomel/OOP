package ru.nsu.lavitskaya.gradebook;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a GradeBook that manages grades for a student.
 * This class allows for adding grades, checking scholarship eligibility,
 * determining eligibility for a Red Diploma, and checking the possibility
 * of transferring to a budgetary form of education.
 */
public class GradeBook {
    private String name;
    private boolean isPaid;
    private List<Grade> grades;
    private GradeEnum qualificationWorkGrade;
    private int currSemester;

    /**
     * Constructs a GradeBook for a student.
     *
     * @param name the name of the student
     * @param isPaid indicates if the student's education is paid or not
     */
    public GradeBook(String name, boolean isPaid) {
        this.name = name;
        this.isPaid = isPaid;
        this.grades = new ArrayList<>();
        this.qualificationWorkGrade = null;
        this.currSemester = 1;
    }

    /**
     * Adds a grade to the current or next semester.
     * If the grade's semester is greater than the current semester,
     * it also updates the current semester.
     *
     * @param grade the grade to be added
     */
    public void addGrade(Grade grade) {
        if (grade.semester() == currSemester) {
            grades.add(grade);
        }
        if (grade.semester() > currSemester) {
            grades.add(grade);
            currSemester = grade.semester();
        }
    }


    /**
     * Sets the qualification work grade.
     *
     * @param grade the qualification work grade to set
     */
    public void setQualificationWorkGrade(GradeEnum grade) {
        this.qualificationWorkGrade = grade;
    }

    /**
     * Gets the current semester number.
     *
     * @return the current semester number
     */
    public int getCurrSemester() {
        return currSemester;
    }


    /**
     * Calculates the average score of all grades.
     *
     * @return the average score as a double; returns 0.0 if no grades are present
     */
    public double calculateAverageScore() {
        return grades.stream()
                .mapToDouble(Grade::getGradeValue)
                .average()
                .orElse(0.0);
    }

    /**
     * Determines if the student can transfer to a budgetary form of education.
     * The student can transfer if there are no "Satisfactory" grades in the last
     * two exam sessions or if they paid but have no grades.
     *
     * @return true if the student can transfer to budget; false otherwise
     */
    public boolean canTransferToBudget() {
        if (isPaid) {
            List<Integer> semesters = grades.stream()
                    .map(Grade::semester)
                    .distinct()
                    .sorted()
                    .toList();

            if (semesters.isEmpty()) {
                return true;
            }

            if (semesters.size() == 1) {
                return grades.stream()
                        .filter(grade -> grade.semester() == semesters.getLast()
                                && grade.type() == TypeEnum.EXAM)
                        .noneMatch(grade -> grade.value() == GradeEnum.SATISFACTORY);
            }

            return grades.stream()
                    .filter(grade -> (grade.semester() == semesters.getLast()
                            || grade.semester() == semesters.get(semesters.size() - 2))
                            && grade.type() == TypeEnum.EXAM)
                    .noneMatch(grade -> grade.value() == GradeEnum.SATISFACTORY);

        }
        return false;
    }


    /**
     * Determines if the student is eligible for a Red Diploma.
     * The student is eligible if:
     * - The qualification work grade is either null or EXCELLENT.
     * - At least 75% of the grades from each course are EXCELLENT, and there are no "Satisfactory"
     *     grades.
     *
     * @return true if eligible for Red Diploma; false otherwise
     */
    public boolean eligibleForRedDiploma() {
        if (qualificationWorkGrade != null && qualificationWorkGrade != GradeEnum.EXCELLENT) {
            return false;
        }
        List<Grade> finalGrades = grades.stream()
                .collect(Collectors.groupingBy(Grade::course))
                .values().stream()
                .map(List::getLast)
                .toList();

        if (finalGrades.isEmpty()) {
            return true;
        }
        if (finalGrades.stream().anyMatch(grade -> grade.value() == GradeEnum.SATISFACTORY)) {
            return false;
        }

        long totalGrades = finalGrades.size();
        long excellentCount = finalGrades.stream()
                .filter(grade -> grade.value() == GradeEnum.EXCELLENT)
                .count();

        return (double) excellentCount / totalGrades >= 0.75;

    }


    /**
     * Determines if the student is eligible for a scholarship.
     * The student is eligible only if all grades in the current semester are EXCELLENT.
     *
     * @return true if eligible for scholarship; false otherwise
     */
    public boolean eligibleForScholarship() {
        List<Grade> currentSemesterGrades = grades.stream()
                .filter(grade -> grade.semester() == currSemester)
                .toList();
        if (currentSemesterGrades.isEmpty()) {
            return true;
        }
        return currentSemesterGrades.stream()
                .allMatch(grade -> grade.value() == GradeEnum.EXCELLENT);
    }

}
