package ru.nsu.lavitskaya;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link Expression} class, checking various behaviors
 * including evaluation, error handling, and derivatives.
 */
class ExpressionTest {

    @Test
    void testPrecedence() {
        Expression expression = Expression.create("5*(2+1)");
        assertEquals(15.0, expression.eval());
    }

    @Test
    void testUnaryMinus() {
        Expression expression = Expression.create("-2");
        assertEquals(-2.0, expression.eval());
    }

    @Test
    void testUnaryMultiplication() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Expression expression = Expression.create("*5");
            expression.eval();
        });

        assertTrue(exception.getMessage().contains("invalid expression."));
    }

    @Test
    void testUnclosedBrackets() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Expression expression = Expression.create("(2+2");
            expression.eval();
        });

        assertTrue(exception.getMessage().contains("invalid expression."));
    }

    @Test
    void testThereIsNoSecondMultiplier() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Expression expression = Expression.create("3*");
            expression.eval();
        });

        assertTrue(exception.getMessage().contains("invalid expression."));
    }

    @Test
    void testEvalWithVariable() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Expression expression = Expression.create("x + 5");
            expression.eval();
        });

        assertTrue(exception.getMessage().contains("value not set for variable 'x'."));
    }

    @Test
    void testEvalMapWithVariable() {
        Expression expression = Expression.create("x + 5");
        assertEquals(7.0, expression.eval("x=2"));
    }

    @Test
    void testEvalMapWithNonExistentVar() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Expression expression = Expression.create("x / 5");
            expression.eval("x=2; y=3");
        });

        assertTrue(exception.getMessage().contains("variable 'y' doesn't exist."));
    }

    @Test
    void testValueForVarIsNotNumber() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Expression expression = Expression.create("x / 5");
            expression.eval("x=a");
        });

        assertTrue(exception.getMessage().contains("value 'a' for variable 'x' isn't a number."));
    }

    @Test
    void testDerivativeForDivisionAndMultiplication() {
        Expression expression = Expression.create("2 * x + x/3");
        assertEquals("(((0*x)+(2*1))+(((1*3)-(x*0))/(3*3)))",
                expression.derivative("x").toString());
    }

    @Test
    void testSimplifyAdd() {
        Expression expression = Expression.create("2 + 2");
        assertEquals("4", expression.simplify().toString());
    }

    @Test
    void testSimplifyAddWithUnaryMinus() {
        Expression expression = Expression.create("-x+x");
        assertEquals("0", expression.simplify().toString());
    }

    @Test
    void testMulByZero() {
        Expression expression = Expression.create("0 * x");
        assertEquals("0", expression.simplify().toString());
    }

    @Test
    void testMulByOne() {
        Expression expression = Expression.create("1 * x");
        assertEquals("x", expression.simplify().toString());
    }

    @Test
    void testDivByOne() {
        Expression expression = Expression.create("x / 1");
        assertEquals("x", expression.simplify().toString());
    }

    @Test
    void testDivByTheSame() {
        Expression expression = Expression.create("x / x");
        assertEquals("1", expression.simplify().toString());
    }

}