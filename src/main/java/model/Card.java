package model;

import java.io.Serializable;

public class Card implements Serializable {
    String suite;
    int value;

    public Card(String suite, int value) {
        this.suite = suite;
        this.value = value;
    }

    public String getSuite() {
        return suite;
    }


    public int getValue() {
        return value;

    }
}
