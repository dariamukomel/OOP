package ru.nsu.lavitskaya;

import java.util.Map;

/**
 * Represents an addition operation between two expressions.
 * This class is part of the expression evaluation framework.
 */
public class Add extends Expression {
    private Expression left;
    private Expression right;

    /**
     * Creates a new addition expression with the specified left and right operands.
     *
     * @param left  The left expression to be added.
     * @param right The right expression to be added.
     */
    public Add(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Returns a string representation of the addition expression in the format:
     * (left + right). If the left expression is equivalent to 0.0, it returns:
     * (right).
     *
     * @return A string representing the addition expression.
     */
    @Override
    public String toString() {
        if (left.toString().equals("0.0")) {
            return "(" + right.toString() + ")";
        }
        return "(" + left.toString() + "+" + right.toString() + ")";
    }

    /**
     * Evaluates the addition expression using the provided variable mapping.
     *
     * @param variables A map of variables with their corresponding values.
     * @return The result of adding the evaluated left and right expressions.
     */
    @Override
    public double eval(Map<String, Double> variables) {
        return left.eval(variables) + right.eval(variables);
    }

    /**
     * Evaluates the addition expression without any variables.
     *
     * @return The result of adding the evaluated left and right expressions.
     */
    @Override
    public double eval() {
        return left.eval() + right.eval();
    }

    /**
     * Calculates the derivative of the addition expression with respect to the specified variable.
     *
     * @param var The name of the variable with respect to which the derivative is to be computed.
     * @return A new Add expression representing the sum of the derivatives of the left and right
     *     expressions.
     */
    @Override
    public Expression derivative(String var) {
        return new Add(left.derivative(var), right.derivative(var));
    }
}
