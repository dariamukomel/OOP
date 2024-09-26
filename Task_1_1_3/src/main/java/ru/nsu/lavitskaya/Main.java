package ru.nsu.lavitskaya;


import java.util.HashMap;
import java.util.Scanner;
import java.util.Map;

public class Main {
    public static Map<String, Double> variables = new HashMap<>();
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double result;

        System.out.println("Enter a mathematical expression:");
        String input = scanner.nextLine();

        Expression expression = ExpressionParser.parse(input);
        while (expression == null){
            System.out.println("Your expression is incorrect. Try again.");
            input = scanner.nextLine();
            expression = ExpressionParser.parse(input);
        }
        System.out.println("Your Expression: " + expression.print());

        if(variables.isEmpty()){
            result = expression.eval(variables);
            System.out.println("Equals: " + (result % 1 == 0 ? String.valueOf((int) result) : String.valueOf(result)));
            return;
        }

        System.out.print("Enter the variable to derivative:");
        input = scanner.nextLine();
        if(!variables.containsKey(input)){
            System.out.println("There is no such variable in the expression.");
        }
        Expression derivative = expression.derivative(input);
        System.out.println("Derivative: " + derivative.print());

        System.out.println("Enter the values of the variables (eg x = 10; y = 13):");
        input = scanner.nextLine();
        while (!parseVariables(input)) {
            input = scanner.nextLine();
        }
        result = expression.eval(variables);
        System.out.println("The result of calculating the value of the expression: " + (result % 1 == 0 ? String.valueOf((int) result) : String.valueOf(result)));

    }

    private static boolean parseVariables(String input) {
        String[] pairs = input.split(";");

        for (String pair : pairs) {
            String[] keyValue = pair.split("=");

            if (keyValue.length != 2) {
                System.out.println("Invalid format for '" + pair + "'. Expected 'key=value'");
                return false;
            }

            String key = keyValue[0].trim();
            String valueStr = keyValue[1].trim();

            if (!variables.containsKey(key)) {
                System.out.println("Variable '" + key + "' doesn't exist.");
                return false;
            }

            try {
                double value = Double.parseDouble(valueStr);
                variables.put(key, value);
            } catch (NumberFormatException e) {
                System.out.println("Value '" + valueStr + "' for variable '" + key + "' isn't a number.");
                return false;
            }
        }

        for (String variable : variables.keySet()) {
            if (variables.get(variable) == null) {
                System.out.println("Value not set for variable '" + variable + "'.");
                return false;
            }
        }
        return true;
    }
}

