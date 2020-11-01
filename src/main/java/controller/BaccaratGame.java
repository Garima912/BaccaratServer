package controller;

import controller.ServerHomeController;
import javafx.application.Platform;
import model.Card;
import model.ClientInfo;
import model.Packet;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class BaccaratGame implements Runnable {

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
    Card playerThirdCard;
    Packet packet;

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

    @Override
    public void run() {
        System.out.println("[BACCARAT_GAME]: Game started, player -> ");
        try {
            while (true){
                System.out.println("received a packet");
                // regardless of what button is pressed on client side, update server UI first
                packet = (Packet) in.readObject();
                clientInfo = new ClientInfo(packet, controller);

                if (packet.getPlayerDetails().getBidAmount() !=0){      // draw button is pressed by client

                    // TODO: REMOVE hardcode to send hand to client
                    Card card_1 = new Card("diamond", 3);
                    Card card_2 = new Card("heart", 4);
                    ArrayList<Card> cards = new ArrayList<>();
                    cards.add(card_1); cards.add(card_2);
                    packet.getPlayerDetails().setPlayerHand(cards);
                    out.writeObject(packet);
                    // TODO: end hardcode to send hand to client

                }
                out.reset();
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}