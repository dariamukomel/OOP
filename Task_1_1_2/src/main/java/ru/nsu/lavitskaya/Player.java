package ru.nsu.lavitskaya;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player in the Blackjack game.
 * Each player has a name and a hand consisting of cards.
 * The player can add cards to their hand, calculate their score,
 * and check if they have gone bust.
 */

public class Player {
    private String name;
    private List<Card> hand;

    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
    }

    public void addCard(Card card) {
        hand.add(card);
    }

    public void clearHand() {
        hand.clear();
    }

    public Card getFirstCardOfHand() { return hand.getFirst(); }

    public Card getLastCardOfHand() { return hand.getLast(); }

    public int getHandSize() { return hand.size(); }

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

    public boolean isBust() {
        return getScore() > BlackjackGame.BLACKJACK_SCORE ;
    }

    public String toString() {
        return hand.toString() + " > " + String.valueOf(this.getScore());
    }
}

