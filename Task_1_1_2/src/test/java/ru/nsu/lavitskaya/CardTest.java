package ru.nsu.lavitskaya;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the functionalities of the Card class,
 * including the value of cards and their string representation.
 */

class CardTest {
    private Card card;

    @BeforeEach
    void setUp() {
        card = new Card("Hearts", "A");
    }

    @Test
    void testCardValue() {
        assertEquals(11, card.getValue(), "The value of Ace should be 11");
    }

    @Test
    void testCardToString() {
        assertEquals("A of Hearts (11)", card.toString(), "The toString method should return the correct format");
    }

}