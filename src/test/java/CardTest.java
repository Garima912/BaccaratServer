
import controller.BaccaratDealer;
import javafx.scene.control.MenuBar;
import model.Card;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {
    Card card;

    @BeforeAll
    static void setup() {

    }

    @BeforeEach
    void init(){
        card = new Card("heart", 3);
    }

    @Test
    void ConstructorTest() {
        card = new Card("diamond", 5);
        assertNotNull(card, "constructor error");
    }

    @Test
    void Constructor_2Test() {
        card = new Card("diamond", 4);
        assertEquals(card.getSuite(),"diamond", "constructor error");
    }

    @Test
    void getSuiteTest() {
        assertEquals(card.getSuite(),"heart", "Wrong value");
    }

    @Test
    void getSuite_2Test() {
        card = new Card("diamond", 4);
        assertEquals(card.getSuite(),"diamond", "Wrong value");
    }

    @Test
    void getValueTest() {
        assertEquals(card.getValue(),3, "Wrong value");
    }

    @Test
    void getValue_2Test() {
        card = new Card("diamond", 4);
        assertEquals(card.getValue(),4, "Wrong value");
    }

    @Test
    void setValueTest() {
        card.setValue(9);
        assertEquals(card.getValue(),9, "Wrong value");
    }

    @Test
    void setValue_2Test() {
        card.setValue(1);
        assertEquals(card.getValue(),1, "Wrong value");
    }

    @Test
    void setSuiteTest() {
        card.setSuite("ace");
        assertEquals(card.getSuite(),"ace", "Wrong value");
    }

    @Test
    void setSuite_2Test() {
        card.setSuite("club");
        assertEquals(card.getSuite(),"club", "Wrong value");
    }
}