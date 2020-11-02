
import controller.BaccaratDealer;
import javafx.scene.control.MenuBar;
import model.Card;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BaccaratDealerTest {
    BaccaratDealer dealer;

    @BeforeAll
    static void setup() {
    }

    @BeforeEach
    void init(){
        dealer = new BaccaratDealer();
    }

    @Test
    void ConstructorTest() {
        dealer = new BaccaratDealer();
        assertNotNull(dealer.deck, "constructor error");
    }

    @Test
    void Constructor_2Test() {
        dealer = new BaccaratDealer();
        assertEquals(dealer.deck.size(), 52, "constructor error, deck size");
    }

    @Test
    void generateDeckTest() {
        dealer.generateDeck();
        assertEquals(dealer.deck.size(), 104, "wrong deck size");
    }

    @Test
    void generateDeck_2Test() {
        dealer.generateDeck();
        assertTrue(dealer.deck.get(0) instanceof Card, "wrong deck type");
    }
    @Test
    void dealHandTest() {
        dealer.dealHand();
        assertEquals(dealer.deck.size(), 50, "wrong deck size");
    }

    @Test
    void dealHand_2Test() {
        ArrayList<Card> hand = dealer.dealHand();
        assertEquals(hand.size(), 2, "wrong hand size");
    }
    @Test
    void drawOneTest() {
        assertTrue(dealer.drawOne() instanceof Card, "wrong card type");
    }

    @Test
    void drawOne_2Test() {
        Card card = dealer.drawOne();
        assertNotNull(card, "null card");
    }

    @Test
    void deckSizeTest() {
        assertEquals(dealer.deckSize(), 52,  "wrong deck size");
    }

    @Test
    void deckSize_2Test() {
        assertTrue(dealer.deckSize()>0,  "wrong deck size");
    }

    @Test
    void shuffleDeckTest() {
        dealer.shuffleDeck();
        assertEquals(dealer.deck.size(), 52,  "wrong deck size");
    }

    @Test
    void shuffleDeck_2Test() {
        Card[] cards = new Card[52];
        for (int i=0; i<52; i++){
            cards[i] = dealer.deck.get(i);
        }
        Card[] shuffledCards = new Card[52];
        dealer.shuffleDeck();
        for (int i=0; i<52; i++){
            shuffledCards[i] = dealer.deck.get(i);
        }
        assertNotEquals(cards, shuffledCards,  "shuffle not working");
    }





}