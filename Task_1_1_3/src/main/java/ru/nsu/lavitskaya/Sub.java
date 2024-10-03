package ru.nsu.lavitskaya;

import java.util.Map;
import java.util.Objects;

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
        if (left.toString().equals("0")) {
            return "(" + "-" + right.toString() + ")";
        }
        return "(" + left.toString() + "-" + right.toString() + ")";
    }

    /**
     * Evaluates the subtraction expression using the provided variable mapping.
     *
     * @param variables A map of variables with their corresponding values.
     * @return The result of subtracting the evaluated right expression from the evaluated left
     *     expression.
     */
    @Override
    public double eval(Map<String, Double> variables) {
        return left.eval(variables) - right.eval(variables);
    }

    /**
     * Evaluates the subtraction expression without any variables.
     *
     * @return The result of subtracting the evaluated right expression from the evaluated left
     *     expression.
     */
    @Override
    public double eval() {
        return left.eval() - right.eval();
    }

    /**
     * Calculates the derivative of the subtraction expression with respect to the specified
     * variable.
     *
     * @param var The name of the variable with respect to which the derivative is to be computed.
     * @return A new Sub expression representing the difference of the derivatives of the left and
     *     right expressions.
     */
    @Override
    public Expression derivative(String var) {
        return new Sub(left.derivative(var), right.derivative(var));
    }

    /**
     * Simplifies the subtraction expression by evaluating constant operands.
     * If both the left and right expressions are instances of Number,
     * their values are subtracted, and a new Number instance is returned.
     * If the left expression evaluates to 0 and the right expression is an addition
     * of two identical terms, or if both expressions are the same, a new Number
     * instance representing zero is returned.
     * Otherwise, a new Sub expression containing the simplified left and right
     * expressions is returned.
     *
     * @return A simplified subtraction expression, which may be a Number if both operands
     *     are constants, or a new Sub instance if at least one operand is not a constant
     *     or if special simplification cases do not apply.
     */
    @Override
    public Expression simplify() {
        Expression simplifiedLeft = left.simplify();
        Expression simplifiedRight = right.simplify();

        if (simplifiedLeft.getClass() == Number.class
                && simplifiedRight.getClass() == Number.class) {
            return new Number(simplifiedLeft.eval() - simplifiedRight.eval());
        }
        if (simplifiedLeft.getClass() == Number.class && simplifiedLeft.eval() == 0
                && simplifiedRight.getClass() == Add.class) {
            String[] terms = simplifiedRight.toString().split("\\+");
            if (Objects.equals(terms[0].replaceAll("[\\s()]", ""),
                    terms[1].replaceAll("[\\s()]", ""))) {
                return new Number(0);
            }
        }

        if (Objects.equals(simplifiedLeft.toString(), simplifiedRight.toString())) {
            return new Number(0);
        }

        return new Sub(simplifiedLeft, simplifiedRight);
    }
}
