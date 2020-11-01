package Controller;

import javafx.application.Platform;
import model.Card;
import model.ClientInfo;
import model.Packet;

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
    String gameResult;

    private Socket clientSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private ServerHomeController controller;
    ClientInfo clientInfo;
    Runnable runnable;
    Card playerThirdCard;

    public BaccaratGame(Socket clientSocket, ServerHomeController controller) throws IOException {

        //dealer distributes 2 cards each to banker and player
        theDealer = new BaccaratDealer();
        this.playerHand = theDealer.dealHand();
        this.bankerHand = theDealer.dealHand();

        if (BaccaratGameLogic.evaluatePlayerDraw(playerHand)) {  //check if player needs to draw 3rd card
            playerThirdCard = theDealer.drawOne();
            playerHand.add(playerThirdCard);
        }
        if(BaccaratGameLogic.evaluateBankerDraw(bankerHand,playerThirdCard)){    //check if banker needs to draw 3rd card
            Card bankerThirdCard = theDealer.drawOne();
            bankerHand.add(bankerThirdCard);
        }
        evaluateWinnings();  // evaluate the results and calculate the total winnings, after hands are updated

        this.clientSocket = clientSocket;
        this.controller = controller;
        in = new ObjectInputStream(this.clientSocket.getInputStream());
        out = new ObjectOutputStream((this.clientSocket.getOutputStream()));
        starGame();
    }

public void starGame(){
        runnable = new Runnable() {
        @Override public void run() {

            System.out.println("[BACCARAT_GAME]: Game started, player -> ");
            try {
                Packet packet = (Packet)in.readObject();
                clientInfo = new ClientInfo(packet, controller);
                System.out.println(packet.getPlayerDetails().getBidAmount());

                if (packet.getPlayerDetails().getBidAmount() != 0){
                    System.out.println("user didn't input bid amount yet");
                }


                // read current bet, bet choice
                // write the gameResult as the winner msg into the packet


            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    };
    Platform.runLater(runnable);
}

    public Socket getSocket() {

        return clientSocket;
    }

    public void closeConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();

    }

    // this function checks if the user wins or loses and returns the total winning  accordingly
    public double evaluateWinnings(){
        String winner = BaccaratGameLogic.whoWon(playerHand,bankerHand);

        if(winner.equals(clientBetChoice)){

            if(winner.equals("Player") || winner.equals("Tie")) {
                totalWinnings += currentBet;
            }
            else{   // bank takes 5% commission
                totalWinnings += (0.95)*currentBet;
            }
            return totalWinnings;
        }

        return currentBet;
    }

}
