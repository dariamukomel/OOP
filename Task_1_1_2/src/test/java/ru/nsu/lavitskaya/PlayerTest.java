package ru.nsu.lavitskaya;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * This class tests the functionalities of the Player class,
 * including adding cards, calculating scores, and checking for busts.
 */

class PlayerTest {
    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player("Player");
    }

    @Test
    void testAddCard() {
        Card card = new Card("Hearts", "K");
        player.addCard(card);
        assertEquals(1, player.getHandSize(), "Player should have one card in hand");
    }

    @Test
    void testGetScore() {
        player.addCard(new Card("Hearts", "10"));
        player.addCard(new Card("Spades", "10"));
        player.addCard(new Card("Spades", "A"));
        assertEquals(21, player.getScore(), "The score should be 21 ");
    }

    @Test
    void testBust() {
        player.addCard(new Card("Hearts", "10"));
        player.addCard(new Card("Spades", "10"));
        player.addCard(new Card("Diamonds", "2"));
        assertTrue(player.isBust(), "Player should be bust when score exceeds 21");
    }
}