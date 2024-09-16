package ru.nsu.lavitskaya;

/**
 * Represents a playing card with a suit, rank, and value.
 */

public class Card {
    private String suit;
    private String rank;
    private int value;

    /**
     * Constructs a new {@code Card} with the specified suit and rank.
     * @param suit the suit of the card (e.g., "Hearts")
     * @param rank the rank of the card (e.g., "K", "10")
     */
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

    /**
     * Returns the value of the card.
     * @return the value of the card as an integer
     */
    public int getValue() {
        return value;
    }

    /**
     * Returns a string representation of the card.
     * @return a string in the format "Rank of Suit (Value)"
     */
    public String toString() {
        return rank + " of " + suit + " (" + getValue() + ")";
    }
}
