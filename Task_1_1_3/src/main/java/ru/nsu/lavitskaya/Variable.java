package ru.nsu.lavitskaya;

import java.util.Map;

/**
 * Represents a variable in an expression.
 * This class is part of the expression evaluation framework.
 */
public class Variable extends Expression {
    private String name;

    /**
     * Creates a new variable object with the specified name.
     *
     * @param name The name of the variable.
     */
    public Variable(String name) {
        this.name = name;
    }

    /**
     * Returns a string representation of the variable.
     *
     * @return The name of the variable.
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Evaluates the value of the variable using the provided set of variables.
     *
     * @param variables A map of variables with their corresponding values.
     * @return The value of the variable.
     * @throws IllegalArgumentException If the value of the variable is not set.
     */
    @Override
    public double eval(Map<String, Double> variables) {
        if (variables.get(name) != null) {
            return variables.get(name);
        } else {
            throw new IllegalArgumentException("value not set for variable '" + name + "'.");
        }
    }

    /**
     * Throws an exception, as the value of the variable must be set in a specific context.
     *
     * @return Nothing, as an exception is always thrown.
     * @throws IllegalArgumentException If the value of the variable is not set.
     */
    @Override
    public double eval() {
        throw new IllegalArgumentException("value not set for variable '" + name + "'.");
    }

    /**
     * Computes the derivative of the variable.
     *
     * @param var The name of the variable with respect to which the derivative should be computed.
     * @return A Number object, equal to 1 if the variable matches, and 0 otherwise.
     */
    @Override
    public Expression derivative(String var) {
        return new Number(var.equals(name) ? 1 : 0);
    }

    /**
     * Simplifies the variable expression. Since variables are already in their simplest form,
     * the simplified result is the same variable.
     *
     * @return A new Variable instance with the same name.
     */
    @Override
    public Expression simplify() {
        return new Variable(name);
    }
}
