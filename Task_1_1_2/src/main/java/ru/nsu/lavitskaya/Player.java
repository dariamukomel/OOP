package ru.nsu.lavitskaya;

import java.util.ArrayList;
import java.util.List;


/**
 * Represents a player in the game, holding a name and a hand of cards.
 */
public class Player {
    private String name;
    private List<Card> hand;

    /**
     * Constructs a new {@code Player} with the specified name.
     *
     * @param name the name of the player
     */
    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
    }

    /**
     * Adds a card to the player's hand.
     *
     * @param card the card to be added
     */
    public void addCard(Card card) {
        hand.add(card);
    }

    /**
     * Clears the player's hand, removing all cards.
     */
    public void clearHand() {
        hand.clear();
    }

    /**
     * Retrieves the first card in the player's hand.
     *
     * @return the first card
     */
    public Card getFirstCardOfHand() {
        return hand.getFirst();
    }

    /**
     * Retrieves the last card in the player's hand.
     *
     * @return the last card
     */
    public Card getLastCardOfHand() {
        return hand.getLast();
    }

    /**
     * Returns the number of cards in the player's hand.
     *
     * @return the size of the hand
     */
    public int getHandSize() {
        return hand.size();
    }

    /**
     * Calculates the player's score from the cards in hand.
     * Aces are counted as 11 unless the score exceeds
     * the maximum allowed score, in which case their value
     * is adjusted to 1.
     *
     * @return the total score of the player
     */
    public int getScore() {
        int score = 0;
        int aces = 0;
        for (Card card : hand) {
            score += card.getValue();
            if (card.getValue() == 11) {
                aces++;
            }
        }
        while (score > BlackjackGame.BLACKJACK_SCORE && aces > 0) {
            score -= 10;
            aces--;
        }
        return score;
    }


    /**
     * Determines if the player has bust (over the maximum score).
     *
     * @return true if the score exceeds the limit, false otherwise
     */
    public boolean isBust() {
        return getScore() > BlackjackGame.BLACKJACK_SCORE;
    }

    /**
     * Returns a string representation of the player's hand and score.
     *
     * @return a string describing the hand followed by the current score
     */
    public String toString() {
        return hand.toString() + " > " + String.valueOf(this.getScore());
    }
}

