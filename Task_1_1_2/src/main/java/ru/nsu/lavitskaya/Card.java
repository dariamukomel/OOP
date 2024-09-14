package ru.nsu.lavitskaya;

/**
 * Represents a playing card in the game.
 * Each card has a suit, a rank, and a value. The value is determined based on
 * the rank of the card, with special handling for face cards and aces.
 */

public class Card {
    private String suit;
    private String rank;
    private int value;

    public Card(String suit, String rank) {
        this.suit = suit;
        this.rank = rank;
        this.value = determineValue(rank);
    }

    private int determineValue(String rank) {
        switch (rank) {
            case "J":
            case "Q":
            case "K":
                return 10;
            case "A":
                return 11; // Ace value will be adjusted later
            default:
                return Integer.parseInt(rank);
        }
    }

    public int getValue() {
        return value;
    }

    public String toString() {
        return rank + " of " + suit + " (" + getValue() + ")";
    }
}
