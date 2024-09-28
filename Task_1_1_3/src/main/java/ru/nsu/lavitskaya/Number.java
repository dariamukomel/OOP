package ru.nsu.lavitskaya;

import java.util.Map;

public class Number extends Expression {

    private double value;

    public Number (double value) {
        this.value = value;
    }

    @Override
    public String toString() {
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
    public double eval() {
        return value;
    }

    @Override
    public Expression derivative(String var) {
        return new Number(0);
    }

}
