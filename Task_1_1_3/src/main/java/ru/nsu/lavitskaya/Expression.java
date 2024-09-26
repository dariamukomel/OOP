package ru.nsu.lavitskaya;


import java.util.Map;

public abstract class Expression {
    public abstract String print();
    public abstract double eval(Map<String, Double> variables);
    public abstract Expression derivative(String var);
}

class Number extends Expression {
    private double value;

    public Number (double value) {
        this.value = value;
    }

    @Override
    public String print() {
        if(value % 1 == 0){
            return String.valueOf((int) value);
        }
        return String.valueOf(value);
    }

    @Override
    public double eval(Map<String, Double> variables) {
        return value;
    }

    @Override
    public Expression derivative(String var) {
        return new Number(0);
    }

}

class Variable extends Expression {
    private String name;

    public Variable (String name) {
        this.name = name;
    }

    @Override
    public String print() {
        return name;
    }

    @Override
    public double eval(Map<String, Double> variables) {
        return variables.get(name);
    }

    @Override
    public Expression derivative(String var) {
        return new Number(var.equals(name) ? 1 : 0);
    }
}

class Add extends Expression {
    private Expression left;
    private Expression right;

    public Add (Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String print() {
        if (left.print().equals("0.0")){
            return "(" + right.print() + ")";
        }
        return "(" + left.print() + "+" + right.print() + ")";
    }

    @Override
    public double eval(Map<String, Double> variables) {
        return left.eval(variables) + right.eval(variables);
    }

    @Override
    public Expression derivative(String var) {
        return new Add(left.derivative(var), right.derivative(var));
    }
}

class Sub extends Expression {
    private Expression left;
    private Expression right;

    public Sub (Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String print() {
        if (left.print().equals("0.0")){
            return "(" + "-" + right.print() + ")";
        }
        return "(" + left.print() + "-" + right.print() + ")";
    }

    @Override
    public double eval(Map<String, Double> variables) {
        return left.eval(variables) - right.eval(variables);
    }

    @Override
    public Expression derivative(String var) {
        return new Sub(left.derivative(var), right.derivative(var));
    }
}

class Mul extends Expression {
    private Expression left;
    private Expression right;

    public Mul (Expression left, Expression right) {
        this.left = left;
        this.right =right;
    }

    @Override
    public String print() {
        return "(" + left.print() + "*" + right.print() + ")";
    }

    @Override
    public double eval(Map<String, Double> variables) {
        return left.eval(variables) * right.eval(variables);
    }

    @Override
    public Expression derivative(String var) {
        return new Add(
                new Mul(left.derivative(var), right),
                new Mul(left, right.derivative(var))
        );
    }
}

class Div extends Expression {
    private Expression left;
    private Expression right;

    public Div (Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String print() {
        return "(" + left.print() + "/" + right.print() + ")";
    }

    @Override
    public double eval(Map<String, Double> variables) {
        return left.eval(variables) / right.eval(variables);
    }

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



