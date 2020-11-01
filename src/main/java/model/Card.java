package model;

import java.io.Serializable;

public class Card implements Serializable {
    String suite;
    int value;

    public String getSuite() {
        return suite;
    }

    public void setSuite(String suite) {
        this.suite = suite;
    }

    public Card(String suite, int value) {
        this.suite = suite;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    // values for face cards: 1:ACE; 11: QUEEN; 12:KING; JACK:13
}