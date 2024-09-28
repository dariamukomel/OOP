package ru.nsu.lavitskaya;

import java.util.Map;

public class Add extends Expression {
    private Expression left;
    private Expression right;

    public Add (Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        if (left.toString().equals("0.0")){
            return "(" + right.toString() + ")";
        }
        return "(" + left.toString() + "+" + right.toString() + ")";
    }

    @Override
    public double eval(Map<String, Double> variables) {
        return left.eval(variables) + right.eval(variables);
    }

    @Override
    public double eval() {
        return left.eval() + right.eval();
    }

    @Override
    public Expression derivative(String var) {
        return new Add(left.derivative(var), right.derivative(var));
    }
}
