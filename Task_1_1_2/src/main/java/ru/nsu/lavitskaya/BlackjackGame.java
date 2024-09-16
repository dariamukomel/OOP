package ru.nsu.lavitskaya;

import java.util.Scanner;

/**
 * The main class for the Blackjack game.
 * This class manages the game lifecycle, including initializing
 * the game components, handling player interactions, and
 * determining the outcomes of each round of Blackjack.
 */
public class BlackjackGame {
    public static final int BLACKJACK_SCORE = 21;
    private Deck deck;
    private Player player;
    private Player dealer;
    private int roundNumber;
    private Scanner scanner;

    /**
     * Constructs a new Blackjack game with a fresh deck and players.
     */
    public BlackjackGame() {
        deck = new Deck();
        player = new Player("Player");
        dealer = new Player("Dealer");
        roundNumber = 0;
        scanner = new Scanner(System.in);
    }

    /**
     * Sets the scanner for user input.
     *
     * @param scanner the scanner to be used for user input
     */
    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Sets the deck for the game.
     *
     * @param deck the deck to be used in the game
     */
    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    /**
     * Starts the Blackjack game, welcoming the player
     * and managing the game rounds.
     */
    public void start() {
        System.out.println("Welcome to Blackjack!");
        while (true) {
            playRound(scanner);
            System.out.print("\nDo you want to play again? (yes/no): ");
            String cont = scanner.next();
            if (!cont.equalsIgnoreCase("yes")) {
                break;
            }
        }
        scanner.close();
    }

    /**
     * Plays a single round of Blackjack, managing the interactions
     * between the player and the dealer, and determining the round outcome.
     *
     * @param scanner the scanner for user input
     */
    private void playRound(Scanner scanner) {
        roundNumber++;
        player.clearHand();
        dealer.clearHand();
        System.out.println("\nRound " + roundNumber + "\n");

        for (int i = 0; i < 2; i++) {
            player.addCard(deck.deal());
            dealer.addCard(deck.deal());
        }

        System.out.println("Dealer dealt cards.");

        System.out.println("Your cards: " + player);
        System.out.println("Dealer's cards: [" + dealer.getFirstCardOfHand()
                + ", <hidden card>]");

        if (player.getScore() == BLACKJACK_SCORE) {
            System.out.println("You have a blackjack! You win!");
            return;
        }

        playerTurn(scanner);

        if (player.isBust()) {
            System.out.println("You lost! Your score exceeds " + BLACKJACK_SCORE + ".");
            return;
        }

        dealerTurn();

        if (dealer.isBust()) {
            System.out.println("Dealer score exceeds " + BLACKJACK_SCORE + "! You win!");
        } else {
            determineWinner();
        }

    }

    /**
     * Manages the player's turn, allowing them to take cards or stop.
     *
     * @param scanner the scanner for user input
     */
    private void playerTurn(Scanner scanner) {
        System.out.println("\nYour turn\n");
        while (true) {
            System.out.print("Enter \"1\" to take a card, and \"0\" to stop: ");
            int choice = scanner.nextInt();
            System.out.print("\n");

            if (choice == 1) {
                player.addCard(deck.deal());
                System.out.println("You drew: " + player.getLastCardOfHand());
                System.out.println("Your cards: " + player);
                System.out.println("Dealer's cards: [" + dealer.getFirstCardOfHand()
                        + ", <hidden card>]");

                if (player.isBust()) {
                    break;
                }
            } else if (choice == 0) {
                break;
            }
        }
    }

    /**
     * Manages the dealer's turn, drawing cards according to the rules.
     */
    private void dealerTurn() {
        System.out.println("Dealer's turn\n");
        System.out.println("Dealer reveals the hidden card: " + dealer.getLastCardOfHand());
        System.out.println("Your cards: " + player);
        System.out.println("Dealer's cards: " + dealer);


        while (dealer.getScore() < 17) {
            dealer.addCard(deck.deal());
            System.out.println("\nDealer drew: " + dealer.getLastCardOfHand());
            System.out.println("Your cards: " + player);
            System.out.println("Dealer's cards: " + dealer);
        }
    }

    /**
     * Determines the winner of the round based on the scores of
     * the player and the dealer.
     */
    private void determineWinner() {
        if (player.getScore() > dealer.getScore()) {
            System.out.println("You won the round!");
        } else if (player.getScore() < dealer.getScore()) {
            System.out.println("Dealer won the round!");
        } else {
            System.out.println("It's a tie!");
        }
    }

    /**
     * The main method to launch the Blackjack game.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        BlackjackGame game = new BlackjackGame();
        game.start();
    }
}
