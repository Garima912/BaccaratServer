package controller;

import model.Card;
import model.ClientInfo;
import model.BaccaratInfo;
import util.Util;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class BaccaratGame {

    private ArrayList<Card> playerHand;
    private ArrayList<Card> bankerHand;
    private BaccaratDealer theDealer;
    private double currentBet;
    private double totalWinnings;
    String clientBetChoice;
    String winner;

    private Socket clientSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    ClientInfo clientInfo;
    Card playerThirdCard;
    BaccaratInfo baccaratInfo;

    public BaccaratGame(Socket clientSocket, ClientInfo clientInfo, ObjectInputStream in, ObjectOutputStream out) throws IOException {
        // we have received bid amount and bet choice.
        // dealer distributes 2 cards each to banker and player
        theDealer = new BaccaratDealer();
        this.playerHand = theDealer.dealHand();
        this.bankerHand = theDealer.dealHand();
        this.clientInfo = clientInfo;

        this.clientSocket = clientSocket;
        this.in = in;
        this.out = out;
        gameStart();
    }

    public Socket getSocket() {

        return clientSocket;
    }

    // this function checks if the user wins or loses and returns the total winning  accordingly
    public double evaluateWinnings(){
        winner = BaccaratGameLogic.whoWon(playerHand,bankerHand);
//        packet.setWinnerMsg(winner);
        if(winner.equals(clientBetChoice)){

            if(winner.equals("Player") || winner.equals("Tie")) {
                totalWinnings += currentBet;
            }
            else{   // bank takes 5% commission
                totalWinnings += (0.95)*currentBet;
            }
            return totalWinnings;
        }

        return totalWinnings-= currentBet;
    }


    public void gameStart(){
        System.out.println("[BACCARAT_GAME]: Game started, player -> ");
        try {
            while (true){
                System.out.println("received a packet");
                baccaratInfo = (BaccaratInfo) in.readObject();
                if (baccaratInfo.actionRequest.equals(Util.ACTION_REQUEST_DRAW)){     // client clicked draw
                    System.out.println("Client drew");
                    currentBet = baccaratInfo.getPlayerDetails().getBidAmount();
                    clientBetChoice = baccaratInfo.getPlayerDetails().getBetChoice();
                    if (BaccaratGameLogic.evaluatePlayerDraw(playerHand)) {  //check if player needs to draw 3rd card
                        playerThirdCard = theDealer.drawOne();
                        playerHand.add(playerThirdCard);
                        System.out.println("player 3rd card: " + playerThirdCard.getValue());
                    }
                    if(BaccaratGameLogic.evaluateBankerDraw(bankerHand,playerThirdCard)){    //check if banker needs to draw 3rd card
                        Card bankerThirdCard = theDealer.drawOne();
                        bankerHand.add(bankerThirdCard);
                        System.out.println("banker 3rd card: " + bankerHand.get(2).getValue());
                    }

                    double winValue = evaluateWinnings();  // evaluate the results and calculate the total winnings, after hands are updated
                    double prevTotalWinnings = baccaratInfo.getPlayerDetails().getTotalWinnings();
                    baccaratInfo.getPlayerDetails().setTotalWinnings(winValue+prevTotalWinnings);  // set total winnings
                    baccaratInfo.setWinnerMsg(winner);        // set the winner
                    clientInfo.updateClient(baccaratInfo);
                    baccaratInfo.getPlayerDetails().setPlayerHand(playerHand);
                    baccaratInfo.getPlayerDetails().setBankerHand(bankerHand);
                    out.reset();
                    out.writeObject(baccaratInfo);
                    System.out.println("packet sent to client");
                    System.out.println("size is "+ baccaratInfo.getPlayerDetails().getBankerHand().size());
                }

                else if (baccaratInfo.actionRequest.equals(Util.ACTION_REQUEST_GAME_OVER)){
                    return;
                }
                else if (baccaratInfo.actionRequest.equals(Util.ACTION_REQUEST_QUIT)){    // client presses quit
                    baccaratInfo.setServerStatus(false);
                    clientInfo.updateClient(baccaratInfo);
                    out.reset();
                    out.writeObject(baccaratInfo);    // send packet back as is. With actionRequest being the same
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }

}