package ru.nsu.lavitskaya;

import java.util.Map;

/**
 * Represents a multiplication operation between two expressions.
 * This class is part of the expression evaluation framework.
 */
public class Mul extends Expression {
    private Expression left;
    private Expression right;

    /**
     * Creates a new multiplication expression with the specified left and right operands.
     *
     * @param left  The left expression to be multiplied.
     * @param right The right expression to be multiplied.
     */
    public Mul(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Returns a string representation of the multiplication expression in the format:
     * (left * right).
     *
     * @return A string representing the multiplication expression.
     */
    @Override
    public String toString() {
        return "(" + left.toString() + "*" + right.toString() + ")";
    }

    /**
     * Evaluates the multiplication expression using the provided variable mapping.
     *
     * @param variables A map of variables with their corresponding values.
     * @return The result of multiplying the evaluated left expression by the evaluated right expression.
     */
    @Override
    public double eval(Map<String, Double> variables) {
        return left.eval(variables) * right.eval(variables);
    }

    /**
     * Evaluates the multiplication expression without any variables.
     *
     * @return The result of multiplying the evaluated left expression by the evaluated right expression.
     */
    @Override
    public double eval() {
        return left.eval() * right.eval();
    }

    /**
     * Calculates the derivative of the multiplication expression with respect to the specified variable.
     * This follows the product rule: (u * v)' = u' * v + u * v'.
     *
     * @param var The name of the variable with respect to which the derivative is to be computed.
     * @return A new Add expression representing the sum of the derivatives according to the product rule.
     */
    @Override
    public Expression derivative(String var) {
        return new Add(
                new Mul(left.derivative(var), right),
                new Mul(left, right.derivative(var))
        );
    }
}
