package controller;

import model.Card;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class BaccaratDealer {

    public ArrayList<Card> deck;
    public ArrayList<String> deckSuite = new ArrayList<>(Arrays.asList("HEARTS", "SPADES","DIAMONDS","CLUBS"));


    public BaccaratDealer() {
        deck =  new ArrayList<>();
        deck.clear();
        generateDeck();
    }

    public void generateDeck(){

        for(int suite =0; suite < 4; suite++){
            for(int cardNum = 1; cardNum < 14; cardNum++){
                Card playCard = new Card(deckSuite.get(suite),cardNum);
                playCard.setValue(cardNum);
                playCard.setSuite(deckSuite.get(suite));
                deck.add(playCard);

            }
        }
        System.out.println("Generated a new deck");

    }
    public ArrayList<Card> dealHand(){
        shuffleDeck();

        ArrayList<Card> hand =  new ArrayList<>();
        Card firstCard = deck.get(0);
        Card secondCard = deck.get(1);
        deck.remove(firstCard);
        deck.remove(secondCard);
        hand.add(firstCard);
        hand.add(secondCard);
        System.out.println(firstCard.getValue() + firstCard.getSuite());
        System.out.println(secondCard.getValue() + secondCard.getSuite());
        return hand;
    }

    public Card drawOne(){

        shuffleDeck();
        Card firstCard = deck.get(15);
        deck.remove(firstCard);
        return firstCard;
    }

    public void shuffleDeck(){
        Collections.shuffle(deck);
    }

    public int deckSize(){
        return deck.size();
    }

}
