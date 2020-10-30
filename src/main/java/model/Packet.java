package main.java.model;

import main.java.Card;

import java.io.Serializable;
import java.util.ArrayList;

public class Packet implements Serializable {

    private String name;
    private String ipAddress;
    private int portNum;
    private PlayerDetails playerDetails;
    private String winnerMsg;

    public Packet(String ipAddress, int portNum, String name) {
        this.ipAddress = ipAddress;
        this.portNum = portNum;
        this.name = name;
    }

    public ArrayList<Card> giveHand(ArrayList<Card> hand){
        return null;
    }

    public String getIpAddress(){
        return ipAddress;
    }

    public int getPortNum() {
        return portNum;
    }

    public String getName(){
        return this.name;
    }


    public PlayerDetails getPlayerDetails() {
        return playerDetails;
    }

    public void setPlayerDetails(PlayerDetails playerDetails) {
        this.playerDetails = playerDetails;
    }

        /* Beginning of inner class */
    public static class PlayerDetails {

        private String playerType;
        private ArrayList<Card> hand;
        private int bidAmount;
        private GameResults betChoice;

        enum GameResults
        {
            PLAYER, BANKER, DRAW;
        }

        public PlayerDetails(String playerType) {
            this.playerType = playerType;
        }

        public String getPlayerType() {
            return playerType;
        }

        public ArrayList<Card> getHand() {
            return hand;
        }

        public void setHand(ArrayList<Card> hand) {
            this.hand = hand;
        }

        public int getBidAmount() {
            return bidAmount;
        }

        public void setBidAmount(int bidAmount) {
            this.bidAmount = bidAmount;
        }

        public GameResults getBetChoice() {
            return betChoice;
        }

        public void setBetChoice(GameResults betChoice) {
            this.betChoice = betChoice;
        }
    }

}
