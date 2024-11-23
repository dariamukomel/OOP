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
    public void testGoodBudgetStudent() {
        gradeBook = new GradeBook("Иванов", false);
        assertEquals(0.0, gradeBook.calculateAverageScore());
        assertFalse(gradeBook.canTransferToBudget());
        assertTrue(gradeBook.eligibleForRedDiploma());
        assertTrue(gradeBook.eligibleForScholarship());

        Semester semester1 = new Semester();
        gradeBook.addSemester(semester1);
        semester1.addGrade(5);
        semester1.addExamResult(4);
        assertEquals(4.5, gradeBook.calculateAverageScore());
        assertFalse(gradeBook.eligibleForRedDiploma());
        assertFalse(gradeBook.eligibleForScholarship());

        Semester semester2 = new Semester();
        gradeBook.addSemester(semester2);
        semester1.addGrade(5);
        semester1.addExamResult(5);
        assertTrue(gradeBook.eligibleForScholarship());
        gradeBook.setQualificationWorkGrade(4);
        assertFalse(gradeBook.eligibleForRedDiploma());
        gradeBook.setQualificationWorkGrade(5);
        assertTrue(gradeBook.eligibleForRedDiploma());
    }

    @Test
    public void testAveragePaidStudent() {
        gradeBook = new GradeBook("Петров", true);
        assertTrue(gradeBook.canTransferToBudget());

        Semester semester1 = new Semester();
        gradeBook.addSemester(semester1);
        semester1.addGrade(3);
        semester1.addExamResult(4);
        assertTrue(gradeBook.canTransferToBudget());


        Semester semester2 = new Semester();
        gradeBook.addSemester(semester2);
        semester2.addExamResult(3);
        assertFalse(gradeBook.canTransferToBudget());
        assertFalse(gradeBook.eligibleForRedDiploma());

    }

}