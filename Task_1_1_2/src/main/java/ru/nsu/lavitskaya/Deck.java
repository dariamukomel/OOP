package ru.nsu.lavitskaya;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a deck of playing cards.
 * The deck can create a new set of cards, shuffle them, and deal cards to players.
 */

public class Deck {
    private List<Card> cards;

    /**
     * Constructs a new {@code Deck} and initializes it with a full
     * set of 52 cards. The deck is shuffled upon creation.
     */
    public Deck() {
        createNewDeck();
        shuffle();
    }

    private void createNewDeck() {
        cards = new ArrayList<>();
        String[] suits = {"Spades", "Hearts", "Diamonds", "Clubs"};
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};

        for (String suit : suits) {
            for (String rank : ranks) {
                cards.add(new Card(suit, rank));
            }
        }
    }

    private void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * Deals a card from the deck. If the deck is empty, a new deck
     * is created and shuffled before dealing the card.
     *
     * @return the dealt card
     */
    public Card deal() {
        if (cards.isEmpty()) {
            System.out.println("The deck is empty. Creating a new deck.");
            createNewDeck();
            shuffle();
        }
        return cards.removeLast();
    }

}
