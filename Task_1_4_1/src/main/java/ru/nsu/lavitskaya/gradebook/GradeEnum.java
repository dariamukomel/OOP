package ru.nsu.lavitskaya.gradebook;

/**
 * This enum represents the different grades that can be assigned
 * to a student's performance. Each grade has an associated numerical value
 * used for calculations and evaluations within the grade book.
 */

public enum GradeEnum {
    SATISFACTORY(3),
    GOOD(4),
    EXCELLENT(5);

    private final int value;

    GradeEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
