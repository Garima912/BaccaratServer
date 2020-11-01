package Controller;

import model.Card;

import java.util.ArrayList;

public class BaccaratGameLogic  {

    ArrayList<Card> hand1 ;
    ArrayList<Card> hand2;

    public BaccaratGameLogic(ArrayList<Card> hand1, ArrayList<Card> hand2) {
        this.hand1 = hand1;
        this.hand2 = hand2;
    }

    public static String whoWon(ArrayList<Card> hand1, ArrayList<Card>hand2){

        if(handTotal(hand1) > handTotal(hand2)){
            return "Player";
        }
        else if(handTotal(hand1) < handTotal(hand2)){
            return "Banker";
        }
        else{

            return "Tie";
        }
    }

    public static int handTotal(ArrayList<Card> hand){
        int total = 0;

        for(Card card: hand){
            int cardValue = card.getValue();
            if(total <= 9) {
                if (cardValue > 10) {
                    total += 0;
                } else {
                    total += cardValue;
                }
            }
            if(total > 9){
                total -= 10;
            }
        }
        return total;
    }
    public static boolean evaluateBankerDraw(ArrayList<Card> hand, Card playerCard){

        int bankerTotal = handTotal(hand);
        if(playerCard == null && bankerTotal < 6){return true;}
        int playerVal = playerCard.getValue();


        if(bankerTotal >= 7){
            return false;
        }
        if(bankerTotal < 3){
            return true;
        }


        if(((playerVal < 2 || playerVal == 9) && bankerTotal == 3)|| ((playerVal == 2 || playerVal == 3) && bankerTotal< 5)
           || ((playerVal == 4 || playerVal == 5) && bankerTotal <6)
           || ((playerVal == 6 || playerVal == 7) && bankerTotal < 7)) {
            return true;
        }

        return false;

    }
    public static boolean evaluatePlayerDraw(ArrayList<Card> hand){
        if(handTotal(hand) < 6 ){
            return true;
        }
        return false;
    }

}
