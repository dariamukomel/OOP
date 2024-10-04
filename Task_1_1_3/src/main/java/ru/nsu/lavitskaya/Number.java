package ru.nsu.lavitskaya;

import java.util.Map;

/**
 * Represents a constant numeric value.
 * This class is used to represent numbers in the expression evaluation framework.
 */
public class Number extends Expression {

    private final double value;

    /**
     * Creates a new Number instance with the specified value.
     *
     * @param value The numeric value to be represented.
     */
    public Number(double value) {
        this.value = value;
    }

    /**
     * Returns a string representation of the numeric value.
     * If the value is an integer (i.e., it has no fractional part), it will be
     * returned as an integer string; otherwise, it will be returned as a decimal string.
     *
     * @return A string representing the numeric value.
     */
    @Override
    public String toString() {
        if (value % 1 == 0) {
            return String.valueOf((int) value);
        }
        return String.valueOf(value);
    }

    /**
     * Evaluates the numeric value using the provided variable mapping.
     * Since this is a constant, the mapping has no effect.
     *
     * @param variables A map of variables with their corresponding values.
     * @return The constant numeric value.
     */
    @Override
    public double eval(Map<String, Double> variables) {
        return value;
    }

    /**
     * Evaluates the numeric value without any variables.
     *
     * @return The constant numeric value.
     */
    @Override
    public double eval() {
        return value;
    }

    /**
     * Calculates the derivative of the numeric value with respect to the specified variable.
     * The derivative of a constant is always zero.
     *
     * @param var The name of the variable with respect to which the derivative is to be computed.
     * @return A new Number instance representing zero.
     */
    @Override
    public Expression derivative(String var) {
        return new Number(0);
    }

    /**
     * Simplifies the expression. Since this class represents a constant numeric value,
     * the simplified result is the same constant.
     *
     * @return A new Number instance with the same numeric value.
     */
    @Override
    public Expression simplify() {
        return new Number(value);
    }

}
