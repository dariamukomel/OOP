package ru.nsu.lavitskaya;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * This class tests the functionalities of the Deck class,
 * including dealing cards and shuffling the deck.
 */

class DeckTest {
    private Deck deck;

    @BeforeEach
    void setUp() {
        deck = new Deck();
    }

    @Test
    void testDealCard() {
        Card dealtCard = deck.deal();
        assertNotNull(dealtCard, "Dealt card should not be null");
    }

    @Test
    void testShuffleDeck() {
        Card firstCardBeforeShuffle = deck.deal();
        deck.shuffle();
        Card firstCardAfterShuffle = deck.deal();
        assertNotEquals(firstCardBeforeShuffle, firstCardAfterShuffle, "The first card after shuffle should be different");
    }
}