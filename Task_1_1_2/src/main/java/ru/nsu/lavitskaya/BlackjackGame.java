package ru.nsu.lavitskaya;

import java.util.Scanner;

/**
 * The main class for the Blackjack game.
 * This class is responsible for managing the game lifecycle,
 * including initializing the game components, handling player
 * interactions, and determining the outcomes of each round
 * of Blackjack.
 */


public class BlackjackGame {
    private Deck deck;
    private Player player;
    private Player dealer;
    private int roundNumber;
    private Scanner scanner;

    public BlackjackGame() {
        deck = new Deck();
        player = new Player("Player");
        dealer = new Player("Dealer");
        roundNumber = 0;
        scanner = new Scanner(System.in);
    }

    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

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

        System.out.println("Your cards: " + player + " > " + player.getScore());
        System.out.println("Dealer's cards: [" + dealer.getHand().get(0) + ", <hidden card>]");

        if (player.getScore() == 21) {
            System.out.println("You have a blackjack! You win!");
            return;
        }

        playerTurn(scanner);

        if (player.isBust()) {
            System.out.println("You lost! Your score exceeds 21.");
            return;
        }

        dealerTurn();

        if (dealer.isBust()) {
            System.out.println("Dealer score exceeds 21! You win!");
        }
        else {
            determineWinner();
        }


    }

    private void playerTurn(Scanner scanner) {
        System.out.println("\nYour turn\n");
        while (true) {
            System.out.print("Enter \"1\" to take a card, and \"0\" to stop: ");
            int choice = scanner.nextInt();
            System.out.print("\n");

            if (choice == 1) {
                player.addCard(deck.deal());
                System.out.println("You drew: " + player.getHand().get(player.getHand().size() - 1));
                System.out.println("Your cards: " + player + " > " + player.getScore());
                System.out.println("Dealer's cards: [" + dealer.getHand().get(0) + ", <hidden card>]");

                if (player.isBust()) {
                    break;
                }
            } else if (choice == 0) {
                break;
            }
        }
    }

    private void dealerTurn() {
        System.out.println("Dealer's turn\n");
        System.out.println("Dealer reveals the hidden card: " + dealer.getHand().get(1));

        while (dealer.getScore() < 17) {
            dealer.addCard(deck.deal());
            System.out.println("Dealer's cards: " + dealer + " > " + dealer.getScore());
        }
    }

    private void determineWinner() {
        if (player.getScore() > dealer.getScore()) {
            System.out.println("You won the round!");
        } else if (player.getScore() < dealer.getScore()) {
            System.out.println("Dealer won the round!");
        } else {
            System.out.println("It's a tie!");
        }
    }

    public static void main(String[] args) {
        BlackjackGame game = new BlackjackGame();
        game.start();
    }
}
