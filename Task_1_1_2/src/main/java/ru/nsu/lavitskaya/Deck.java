package ru.nsu.lavitskaya;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a deck of cards for the Blackjack game.
 * The deck is capable of shuffling and dealing cards.
 * Once the deck is empty, a new deck is automatically created
 * and shuffled for continued play.
 */
public class Deck {
    private List<Card> cards;

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

    public Card deal() {
        if (cards.isEmpty()) {
            System.out.println("The deck is empty. Creating a new deck.");
            createNewDeck();
            shuffle();
        }
        return cards.removeLast();
    }

}
