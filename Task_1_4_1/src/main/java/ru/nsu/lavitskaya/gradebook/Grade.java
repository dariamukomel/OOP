package ru.nsu.lavitskaya.gradebook;

/**
 * Represents a grade awarded to a student for a specific course in a given semester.
 * This record includes information about the semester, type of assessment, course name,
 * and the grade value itself.
 *
 * @param semester the semester in which the grade was awarded
 * @param type the type of assessment (e.g., exam or credit)
 * @param course the name of the course associated with the grade
 * @param value the grade received, represented by an enumeration of grade values
 */
public record Grade(
        int semester,
        TypeEnum type,
        String course,
        GradeEnum value) {

    public int getGradeValue() {
        return value.getValue();
    }
}

