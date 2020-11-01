package model;

import controller.BaccaratGameLogic;

import java.io.Serializable;
import java.util.ArrayList;

public class Packet implements Serializable {

    private String ipAddress;
    private int portNum;
    private PlayerDetails playerDetails;
    private String winnerMsg;
    private boolean serverStatus;


    public Packet(String ipAddress, int portNum, String name) {
        this.ipAddress = ipAddress;
        this.portNum = portNum;
        this.playerDetails = new PlayerDetails(name);
        this.serverStatus = true;
    }

    public boolean isServerStatus() {
        return serverStatus;
    }

    public void setServerStatus(boolean serverStatus) {
        this.serverStatus = serverStatus;
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

    public PlayerDetails getPlayerDetails() {
        return playerDetails;
    }



    public String getWinnerMsg() {
        return winnerMsg;
    }

    public void setWinnerMsg(String winnerMsg) {
        this.winnerMsg = winnerMsg;
    }

    /* Player details implementation */
    public class PlayerDetails implements Serializable{
        private String playerName;
        private ArrayList<Card> playerHand;
        private ArrayList<Card> bankerHand;
        private int bidAmount;
        private String betChoice;
        private boolean isOnline;
        private double totalWinnings;

        public double getTotalWinnings() {
            return totalWinnings;
        }

        public void setTotalWinnings(double totalWinnings) {
            this.totalWinnings = totalWinnings;
        }

        public PlayerDetails(String playerName) {
            this.playerName = playerName;
            this.isOnline = true;
        }

        public String getPlayerName() {
            return this.playerName;
        }

        public void setPlayerName(String playerName) {
            this.playerName = playerName;
        }

        public ArrayList<Card> getPlayerHand() {
            return playerHand;
        }

        public void setPlayerHand(ArrayList<Card> playerHand) {
            this.playerHand = playerHand;
        }

        public ArrayList<Card> getBankerHand() {
            return bankerHand;
        }

        public void setBankerHand(ArrayList<Card> bankerHand) {
            this.bankerHand = bankerHand;
        }

        public int getBidAmount() {
            return this.bidAmount;
        }

        public void setBidAmount(int bidAmount) {
            this.bidAmount = bidAmount;
        }

        public String  getBetChoice() {
            return this.betChoice;
        }

        public void setBetChoice(String betChoice) {
            this.betChoice = betChoice;
        }

        public boolean isOnline() {
            return this.isOnline;
        }

        public void setOnline(boolean online) {
            this.isOnline = online;
        }
        public int getHandTotal(ArrayList<Card> hand){
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
    }


}