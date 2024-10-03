package ru.nsu.lavitskaya;

import java.util.Map;
import java.util.Objects;

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
     *     expression.
     */
    @Override
    public double eval(Map<String, Double> variables) {
        return left.eval(variables) / right.eval(variables);
    }

    /**
     * Evaluates the division expression without any variables.
     *
     * @return The result of dividing the evaluated left expression by the evaluated right
     *     expression.
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

    /**
     * Simplifies the division expression by evaluating constant operands.
     * If both the left and right expressions are instances of Number,
     * their values are divided, and a new Number instance is returned.
     * If the right expression is a constant that evaluates to 1, the left
     * expression is returned (as dividing by 1 has no effect).
     * If the left expression and the right expression are identical,
     * a new Number instance representing one is returned, as A / A = 1 (for A ≠ 0).
     *
     * @return A simplified division expression, which may be a Number if both operands
     *     are constants, or a new Div instance if at least one operand is not a constant
     *     or does not meet special simplification conditions.
     */
    @Override
    public Expression simplify() {
        Expression simplifiedLeft = left.simplify();
        Expression simplifiedRight = right.simplify();

        if (simplifiedLeft.getClass() == Number.class
                && simplifiedRight.getClass() == Number.class){
            return new Number(simplifiedLeft.eval() / simplifiedRight.eval());
        }
        if (simplifiedRight.getClass() == Number.class && simplifiedRight.eval() == 1) {
            return simplifiedLeft;
        }
        if(Objects.equals(simplifiedRight.toString(), simplifiedLeft.toString())){
            return new Number(1);
        }

        return new Div(simplifiedLeft, simplifiedRight);
    }
}
