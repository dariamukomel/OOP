package ru.nsu.lavitskaya;


import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public abstract class Expression {
    public abstract double eval(Map<String, Double> variables);
    public abstract double eval();
    public abstract Expression derivative(String var);

    public static Map<String, Double> variables = new HashMap<>();

    public double eval(String stringOfVars) {
        String[] pairs = stringOfVars.split(";");

        for (String pair : pairs) {
            String[] keyValue = pair.split("=");

            if (keyValue.length != 2) {
                throw new IllegalArgumentException("invalid format for '" + pair + "'. " +
                        "Expected 'key=value'");
            }

            String key = keyValue[0].trim();
            String valueStr = keyValue[1].trim();

            if (!variables.containsKey(key)) {
                throw new IllegalArgumentException("variable '" + key + "' doesn't exist.");

            }

            try {
                double value = Double.parseDouble(valueStr);
                variables.put(key, value);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("value '" + valueStr + "' for variable '"
                        + key + "' isn't a number.");

            }
        }
        return eval(variables);
    }


    public static Expression create(String expression) {
        char[] tokens = expression.toCharArray();
        Stack<Expression> values = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for(int i=0; i< tokens.length; i++){
            char token = tokens[i];

            if(token == ' '){
                continue;
            }
            if (Character.isDigit(token)) {
                String number = String.valueOf(tokens[i++]);
                while (i < tokens.length && (Character.isDigit(tokens[i]) || tokens[i] == '.')) {
                    number += tokens[i++];
                }
                i--;
                values.push(new Number(Double.parseDouble(number)));
            }

            else if (Character.isLetter(token) || token == '_'){
                String variable =  String.valueOf(tokens[i++]);
                while (i < tokens.length && (Character.isDigit(tokens[i]) ||
                        Character.isLetter(tokens[i]) || tokens[i] == '_')) {
                    variable += tokens[i++];
                }
                i--;
                values.push(new Variable(variable));
                variables.put(variable, null);
            }

            else if (token == '('){
                operators.push(token);
            }

            else if (token == ')'){ // 2+2) () (-)
                if (!operators.contains('(') || operators.peek() == '(' || values.isEmpty()) {
                    throw new IllegalArgumentException("invalid expression.");
                }

                while (operators.peek() != '(' ) { // (5+8+9)
                    char op = operators.pop();
                    Expression right = values.pop();
                    Expression left;
                    if(values.isEmpty()) { // (-2)
                        if (op == '-' || op == '+') {
                            left = new Number(0);
                        }
                        else { // (*2)
                            throw new IllegalArgumentException("invalid expression.");
                        }
                    }
                    else{
                        left = values.pop();
                    }
                    values.push(createExpression(op, left, right));
                }
                operators.pop();
            }

            else if ("+-*/".indexOf(token) != -1) {
                while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(token)) {
                    char op = operators.pop();
                    Expression right = values.pop();
                    Expression left = values.pop();
                    values.push(createExpression(op, left, right));
                }
                operators.push(tokens[i++]);
                while (i<tokens.length && tokens[i] == ' '){
                    i++;
                }
                if(i<tokens.length && (Character.isDigit(tokens[i]) || Character.isLetter(tokens[i])
                        || tokens[i] == '_' || tokens[i] == '(')) {
                    i--;
                }
                else{
                    throw new IllegalArgumentException("invalid expression.");
                }
            }

            else{
                throw new IllegalArgumentException("invalid expression.");
            }
        }

        while (!operators.isEmpty()){
            if (operators.contains('(') || values.isEmpty()){
                throw new IllegalArgumentException("invalid expression.");
            }
            char op = operators.pop();
            Expression right = values.pop();
            Expression left;
            if(values.isEmpty()){// -2
                if(op == '-' || op == '+'){
                    left = new Number(0);
                }
                else {
                    throw new IllegalArgumentException("invalid expression.");
                }
            }
            else{
                left = values.pop();
            }
            values.push(createExpression(op, left, right));
        }

        return values.pop();
    }

    private static Expression createExpression (char op, Expression left, Expression right) {
        return switch (op) {
            case '+' -> new Add(left, right);
            case '-' -> new Sub(left, right);
            case '*' -> new Mul(left, right);
            case '/' -> new Div(left, right);
            default -> throw new IllegalArgumentException("invalid expression.");
        };
    }

    private static int precedence(char operator) {
        return switch (operator) {
            case '+', '-' -> 1;
            case '*', '/' -> 2;
            default -> 0;
        };
    }

}




