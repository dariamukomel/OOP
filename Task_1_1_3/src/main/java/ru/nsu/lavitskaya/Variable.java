package ru.nsu.lavitskaya;

import java.util.Map;

public class Variable extends Expression {
    private String name;

    public Variable (String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public double eval(Map<String, Double> variables) {
        if (variables.get(name) != null) {
            return variables.get(name);
        }
        else{
            throw new IllegalArgumentException("value not set for variable '" + name + "'.");
        }
    }

    @Override
    public double eval(){
        throw new IllegalArgumentException("value not set for variable '" + name + "'.");
    }

    @Override
    public Expression derivative(String var) {
        return new Number(var.equals(name) ? 1 : 0);
    }
}
