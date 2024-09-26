package ru.nsu.lavitskaya;

import java.util.Stack;

public class ExpressionParser {

    public static Expression parse(String expression) {
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
                while (i < tokens.length && (Character.isDigit(tokens[i]) || Character.isLetter(tokens[i]) || tokens[i] == '_')) {
                    variable += tokens[i++];
                }
                i--;
                values.push(new Variable(variable));
                Main.variables.put(variable, null);
            }

            else if (token == '('){
                operators.push(token);
            }

            else if (token == ')'){ // 2+2) () (-)
                if (!operators.contains('(') || operators.peek() == '(' || values.isEmpty()) {
                    return null;
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
                            return null;
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
                if(i<tokens.length && (Character.isDigit(tokens[i]) || Character.isLetter(tokens[i]) || tokens[i] == '_')) {
                    i--;
                }
                else{
                    return null;
                }
            }

            else{
                return null;
            }
        }

        while (!operators.isEmpty()){
            if (operators.contains('(') || values.isEmpty()){
                return null;
            }
            char op = operators.pop();
            Expression right = values.pop();
            Expression left;
            if(values.isEmpty()){// -2
                if(op == '-' || op == '+'){
                    left = new Number(0);
                }
                else {
                    return null;
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
            default -> null;
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


