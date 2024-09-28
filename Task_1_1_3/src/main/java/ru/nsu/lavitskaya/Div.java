package ru.nsu.lavitskaya;

import java.util.Map;

/**
 * Represents a division operation between two expressions.
 * This class is part of the expression evaluation framework.
 */
public class Div extends Expression {
    private Expression left;
    private Expression right;

    /**
     * Creates a new division expression with the specified left and right operands.
     *
     * @param left  The numerator (left expression).
     * @param right The denominator (right expression).
     */
    public Div(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Returns a string representation of the division expression in the format:
     * (left / right).
     *
     * @return A string representing the division expression.
     */
    @Override
    public String toString() {
        return "(" + left.toString() + "/" + right.toString() + ")";
    }


    /**
     * Evaluates the division expression using the provided variable mapping.
     *
     * @param variables A map of variables with their corresponding values.
     * @return The result of dividing the evaluated left expression by the evaluated right
     * expression.
     */
    @Override
    public double eval(Map<String, Double> variables) {
        return left.eval(variables) / right.eval(variables);
    }

    /**
     * Evaluates the division expression without any variables.
     *
     * @return The result of dividing the evaluated left expression by the evaluated right
     * expression.
     */
    @Override
    public double eval() {
        return left.eval() / right.eval();
    }

    /**
     * Calculates the derivative of the division expression with respect to the specified variable.
     * This follows the quotient rule: (u / v)' = (u' * v - u * v') / (v^2).
     *
     * @param var The name of the variable with respect to which the derivative is to be computed.
     * @return A new Div expression representing the derivative of the division.
     */
    @Override
    public Expression derivative(String var) {
        return new Div(
                new Sub(
                        new Mul(left.derivative(var), right),
                        new Mul(left, right.derivative(var))
                ),
                new Mul(right, right)
        );
    }
}
