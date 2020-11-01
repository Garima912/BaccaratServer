package controller;

import model.Card;
import model.ClientInfo;
import model.Packet;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class BaccaratGame implements Runnable {

    private Socket clientSocket;
    private ServerHomeController controller;
    ClientInfo clientInfo;
    ObjectInputStream in;
    ObjectOutputStream out;
    Packet packet;


    public BaccaratGame(Socket clientSocket, ServerHomeController controller) throws IOException {
        this.clientSocket = clientSocket;
        this.controller = controller;

        in = new ObjectInputStream(this.clientSocket.getInputStream());
        out = new ObjectOutputStream((this.clientSocket.getOutputStream()));
    }

    public void startGame(){


    }

    // test this in a runlater
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

    public Socket getSocket() {
        return clientSocket;
    }

    public void closeConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }
}
