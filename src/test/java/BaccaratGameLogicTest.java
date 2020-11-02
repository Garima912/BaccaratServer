
import controller.BaccaratDealer;
import controller.BaccaratGame;
import controller.BaccaratGameLogic;
import javafx.scene.control.MenuBar;
import model.Card;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BaccaratGameLogicTest {
    BaccaratGameLogic gameLogic;
    ArrayList<Card> hand1;
    ArrayList<Card> hand2;

    @BeforeAll
    static void setup() {
    }

    @BeforeEach
    void init(){
        hand1 = new ArrayList<>();
        hand2 = new ArrayList<>();

        hand1.add(new Card("diamond", 2));
        hand1.add(new Card("diamond", 4));

        hand2.add(new Card("diamond", 6));
        hand2.add(new Card("diamond", 2));
        gameLogic = new BaccaratGameLogic(hand1, hand2);
    }

    @Test
    void WhoWonTest() {
        gameLogic = new BaccaratGameLogic(hand1, hand2);
        assertNotNull(BaccaratGameLogic.whoWon(hand1, hand2), "constructor error");
    }

    @Test
    void whoWonTest() {
        gameLogic = new BaccaratGameLogic(hand1, hand2);
        assertEquals(BaccaratGameLogic.whoWon(hand1, hand2), "Banker", "constructor error");
    }

    @Test
    void handTotalTest() {
        assertEquals(BaccaratGameLogic.handTotal(hand1), 6, "wrong deck size");
    }

    @Test
    void handTotal_2Test() {
        hand2.add(new Card("spade", 5));
        assertEquals(BaccaratGameLogic.handTotal(hand2), 3, "wrong deck size");
    }

    @Test
    void evaluateBankerDrawTest() {
        boolean res = BaccaratGameLogic.evaluateBankerDraw(hand1, hand2.get(0));
        assertEquals(res, true, "banker draw not working");

    }
    @Test
    void evaluateBankerDraw_2Test() {
        boolean res = BaccaratGameLogic.evaluateBankerDraw(hand2, hand1.get(1));
        assertEquals(res, false, "banker draw is not working");

    }

    @Test
    void evaluatePlayerDrawTest() {
        boolean res = BaccaratGameLogic.evaluatePlayerDraw(hand1);
        assertEquals(res, false, "player draw not working");

    }
    @Test
    void evaluatePlayerDraw_2Test() {
        boolean res = BaccaratGameLogic.evaluatePlayerDraw(hand2);
        assertEquals(res, false, "player draw is not working");

    }







}