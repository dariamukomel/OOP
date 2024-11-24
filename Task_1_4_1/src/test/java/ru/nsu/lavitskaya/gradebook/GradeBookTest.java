package ru.nsu.lavitskaya.gradebook;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * The GradeBookTest class contains unit tests for the GradeBook class.
 * These tests verify the functionality of the GradeBook methods
 * related to student grades, eligibility for scholarships,
 * and transfer options.
 */
class GradeBookTest {
    private GradeBook gradeBook;

    @Test
    public void testAddingGrades() {
        gradeBook = new GradeBook("ы", true);
        Grade grade0 = new Grade(1, TypeEnum.EXAM, "hh", GradeEnum.EXCELLENT);
        gradeBook.addGrade(grade0);
        assertEquals(1, gradeBook.getCurrSemester());

        Grade grade1 = new Grade(5, TypeEnum.EXAM, "hh", GradeEnum.EXCELLENT);
        gradeBook.addGrade(grade1);
        assertEquals(5, gradeBook.getCurrSemester());

        Grade grade2 = new Grade(4, TypeEnum.EXAM, "hh", GradeEnum.EXCELLENT);
        gradeBook.addGrade(grade2);
        assertEquals(5, gradeBook.getCurrSemester());
    }

    @Test
    public void testCalculateAverageScore() {
        gradeBook = new GradeBook("ы", true);
        assertEquals(0.0, gradeBook.calculateAverageScore());

        Grade grade1 = new Grade(1, TypeEnum.EXAM, "hh", GradeEnum.EXCELLENT);
        gradeBook.addGrade(grade1);
        Grade grade2 = new Grade(2, TypeEnum.CREDIT, "ho", GradeEnum.GOOD);
        gradeBook.addGrade(grade2);
        assertEquals(4.5, gradeBook.calculateAverageScore());
    }

    @Test
    public void testCanTransferToBudgetOneSemester() {
        gradeBook = new GradeBook("ы", false);
        assertFalse(gradeBook.canTransferToBudget());

        gradeBook = new GradeBook("ы", true);
        assertTrue(gradeBook.canTransferToBudget());

        Grade grade1 = new Grade(1, TypeEnum.EXAM, "hh", GradeEnum.GOOD);
        gradeBook.addGrade(grade1);
        Grade grade2 = new Grade(1, TypeEnum.CREDIT, "hf", GradeEnum.SATISFACTORY);
        gradeBook.addGrade(grade2);
        assertTrue(gradeBook.canTransferToBudget());

        Grade grade3 = new Grade(1, TypeEnum.EXAM, "df", GradeEnum.SATISFACTORY);
        gradeBook.addGrade(grade3);
        assertFalse(gradeBook.canTransferToBudget());
    }

    @Test
    public void testCanTransferToBudgetTwoSemesters() {
        gradeBook = new GradeBook("ы", true);
        Grade grade1 = new Grade(1, TypeEnum.EXAM, "hh", GradeEnum.SATISFACTORY);
        gradeBook.addGrade(grade1);
        Grade grade2 = new Grade(2, TypeEnum.EXAM, "hf", GradeEnum.GOOD);
        gradeBook.addGrade(grade2);
        assertFalse(gradeBook.canTransferToBudget());

        Grade grade3 = new Grade(3, TypeEnum.EXAM, "hl", GradeEnum.GOOD);
        gradeBook.addGrade(grade3);
        assertTrue(gradeBook.canTransferToBudget());
    }

    @Test
    public void testEligibleForRedDiploma() {
        gradeBook = new GradeBook("ы", true);
        gradeBook.setQualificationWorkGrade(GradeEnum.GOOD);
        assertFalse(gradeBook.eligibleForRedDiploma());

        gradeBook.setQualificationWorkGrade(GradeEnum.EXCELLENT);
        Grade grade1 = new Grade(1, TypeEnum.EXAM, "hh", GradeEnum.EXCELLENT);
        gradeBook.addGrade(grade1);
        Grade grade2 = new Grade(1, TypeEnum.CREDIT, "hg", GradeEnum.EXCELLENT);
        gradeBook.addGrade(grade2);
        Grade grade3 = new Grade(2, TypeEnum.CREDIT, "hf", GradeEnum.EXCELLENT);
        gradeBook.addGrade(grade3);
        Grade grade4 = new Grade(2, TypeEnum.CREDIT, "hs", GradeEnum.GOOD);
        gradeBook.addGrade(grade4);
        assertTrue(gradeBook.eligibleForRedDiploma());

        Grade grade5 = new Grade(3, TypeEnum.CREDIT, "hf", GradeEnum.GOOD);
        gradeBook.addGrade(grade5);
        assertFalse(gradeBook.eligibleForRedDiploma());

        Grade grade6 = new Grade(4, TypeEnum.CREDIT, "hf", GradeEnum.EXCELLENT);
        gradeBook.addGrade(grade6);
        assertTrue(gradeBook.eligibleForRedDiploma());

        Grade grade7 = new Grade(4, TypeEnum.CREDIT, "tr", GradeEnum.SATISFACTORY);
        gradeBook.addGrade(grade7);
        assertFalse(gradeBook.eligibleForRedDiploma());

    }

    @Test
    public void testEligibleForScholarship() {
        gradeBook = new GradeBook("ы", true);
        assertTrue(gradeBook.eligibleForScholarship());

        Grade grade1 = new Grade(1, TypeEnum.CREDIT, "hh", GradeEnum.EXCELLENT);
        gradeBook.addGrade(grade1);
        assertTrue(gradeBook.eligibleForScholarship());

        Grade grade2 = new Grade(1, TypeEnum.CREDIT, "hf", GradeEnum.GOOD);
        gradeBook.addGrade(grade2);
        assertFalse(gradeBook.eligibleForScholarship());
    }

}