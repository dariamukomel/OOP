package ru.nsu.lavitskaya;

import java.util.Map;

public class Mul extends Expression {
    private Expression left;
    private Expression right;

    public Mul (Expression left, Expression right) {
        this.left = left;
        this.right =right;
    }

    @Override
    public String toString() {
        return "(" + left.toString() + "*" + right.toString() + ")";
    }

    @Override
    public double eval(Map<String, Double> variables) {
        return left.eval(variables) * right.eval(variables);
    }

    @Override
    public double eval() {
        return left.eval() * right.eval();
    }

    @Override
    public Expression derivative(String var) {
        return new Add(
                new Mul(left.derivative(var), right),
                new Mul(left, right.derivative(var))
        );
    }
}