package ru.nsu.lavitskaya;

import java.util.Map;

/**
 * Represents a subtraction operation between two expressions.
 * This class is part of the expression evaluation framework.
 */
public class Sub extends Expression {
    private Expression left;
    private Expression right;

    /**
     * Creates a new subtraction expression with the specified left and right operands.
     *
     * @param left  The left expression to be subtracted from.
     * @param right The right expression that will be subtracted.
     */
    public Sub(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Returns a string representation of the subtraction expression in the format:
     * (left - right). If the left expression is equivalent to 0.0, it returns:
     * (-right).
     *
     * @return A string representing the subtraction expression.
     */
    @Override
    public String toString() {
        if (left.toString().equals("0.0")) {
            return "(" + "-" + right.toString() + ")";
        }
        return "(" + left.toString() + "-" + right.toString() + ")";
    }

    /**
     * Evaluates the subtraction expression using the provided variable mapping.
     *
     * @param variables A map of variables with their corresponding values.
     * @return The result of subtracting the evaluated right expression from the evaluated left expression.
     */
    @Override
    public double eval(Map<String, Double> variables) {
        return left.eval(variables) - right.eval(variables);
    }

    /**
     * Evaluates the subtraction expression without any variables.
     *
     * @return The result of subtracting the evaluated right expression from the evaluated left expression.
     */
    @Override
    public double eval() {
        return left.eval() - right.eval();
    }

    /**
     * Calculates the derivative of the subtraction expression with respect to the specified variable.
     *
     * @param var The name of the variable with respect to which the derivative is to be computed.
     * @return A new Sub expression representing the difference of the derivatives of the left and right expressions.
     */
    @Override
    public Expression derivative(String var) {
        return new Sub(left.derivative(var), right.derivative(var));
    }
}
