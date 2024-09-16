package ru.nsu.lavitskaya;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 *The DeckTest class tests the functionalities of the Deck class.
 * It ensures that the operations for dealing cards work as expected.
 * testDealCard() - Tests that a card can be dealt from a non-empty deck.
 * testDealLastCard() - Tests that dealing the last card from the deck correctly creates a new deck and returns a non-null card.
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
    void testDealLastCard() {
        for (int i = 0; i < 52; i++) {
            deck.deal();
        }

        Card newCard = deck.deal();
        assertNotNull(newCard, "Dealing from an empty deck should return a non-null card");
    }
}