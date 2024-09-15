package ru.nsu.lavitskaya;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for checking the logic of the BlackjackGame.
 * This class uses JUnit 5 to perform the tests
 * and Mockito to create mock objects used in the tests.
 */

class BlackjackGameTest {
    private final PrintStream originalOut = System.out; //originalOut preserves the original standard output so that it can be restored if necessary
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); //outputStream is used to collect data output to standard stream (instead of console)

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStream)); //Here the standard output is redirected to outputStream so that we can capture whatever is output to the console while the test is running
    }

    @Test
    void testPlayerWinsWithBlackjack() {

        String simulatedInput = "no\n";
        InputStream in = new ByteArrayInputStream(simulatedInput.getBytes()); //ByteArrayInputStream is used to create an input data stream from a string
        Scanner scanner = new Scanner(in);


        BlackjackGame game = new BlackjackGame();
        game.setScanner(scanner);


        Deck mockDeck = mock(Deck.class);
        when(mockDeck.deal()).thenReturn(new Card("Hearts", "A"), new Card("Diamonds", "K"));
        game.setDeck(mockDeck);

        game.start();

        String output = outputStream.toString();
        assertTrue(output.contains("You have a blackjack! You win!"));
        System.setOut(originalOut);
    }

    @Test
    void testPlayerWinsWithoutBlackjack() {

        String simulatedInput = "0\nno\n";
        InputStream in = new ByteArrayInputStream(simulatedInput.getBytes()); //ByteArrayInputStream is used to create an input data stream from a string
        Scanner scanner = new Scanner(in);


        Deck mockDeck = mock(Deck.class);
        BlackjackGame game = new BlackjackGame();
        game.setScanner(scanner);
        game.setDeck(mockDeck);

        Card playerCard1 = new Card("Hearts", "10");
        Card playerCard2 = new Card("Diamonds", "K");
        Card dealerCard1 = new Card("Spades", "9");
        Card dealerCard2 = new Card("Clubs", "9");
        when(mockDeck.deal()).thenReturn(playerCard1, dealerCard1, playerCard2, dealerCard2);


        game.start();

        String output = outputStream.toString();
        assertTrue(output.contains("You won the round!"));
        System.setOut(originalOut);
    }
}
