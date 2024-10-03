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
     * @return The result of multiplying the evaluated left expression by the evaluated right
     *     expression.
     */
    @Override
    public double eval(Map<String, Double> variables) {
        return left.eval(variables) * right.eval(variables);
    }

    /**
     * Evaluates the multiplication expression without any variables.
     *
     * @return The result of multiplying the evaluated left expression by the evaluated right
     *     expression.
     */
    @Override
    public double eval() {
        return left.eval() * right.eval();
    }

    /**
     * Calculates the derivative of the multiplication expression with respect to the specified
     * variable.This follows the product rule: (u * v)' = u' * v + u * v'.
     *
     * @param var The name of the variable with respect to which the derivative is to be computed.
     * @return A new Add expression representing the sum of the derivatives according to the
     *     product rule.
     */
    @Override
    public Expression derivative(String var) {
        return new Add(
                new Mul(left.derivative(var), right),
                new Mul(left, right.derivative(var))
        );
    }

    /**
     * Simplifies the multiplication expression by evaluating constant operands.
     * If both the left and right expressions are instances of Number,
     * their values are multiplied, and a new Number instance is returned.
     * If the left expression is a constant and evaluates to 0, a new Number
     * instance representing zero is returned. If it evaluates to 1, the right
     * expression is returned (since multiplying by 1 has no effect).
     * Similarly, if the right expression is a constant and evaluates to 0,
     * a new Number instance representing zero is returned, and if it evaluates
     * to 1, the left expression is returned.
     *
     * @return A simplified multiplication expression, which may be a Number if both operands
     *     are constants, or a new Mul instance if at least one operand is not a constant
     *     or does not meet special simplification conditions.
     */
    @Override
    public Expression simplify() {
        Expression simplifiedLeft = left.simplify();
        Expression simplifiedRight = right.simplify();

        if (simplifiedLeft.getClass() == Number.class
                && simplifiedRight.getClass() == Number.class) {
            return new Number(simplifiedLeft.eval() * simplifiedRight.eval());
        }
        if (simplifiedLeft.getClass() == Number.class) {
            if (simplifiedLeft.eval() == 0) {
                return new Number(0);
            }
            if (simplifiedLeft.eval() == 1) {
                return simplifiedRight;
            }
        }
        if (simplifiedRight.getClass() == Number.class) {
            if (simplifiedRight.eval() == 0) {
                return new Number(0);
            }
            if (simplifiedRight.eval() == 1) {
                return simplifiedLeft;
            }
        }
        return new Mul(simplifiedLeft, simplifiedRight);
    }
}
