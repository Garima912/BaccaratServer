package main.java;

import javafx.application.Platform;
import main.java.model.ClientInfo;
import main.java.model.Packet;

import java.io.*;
import java.net.Socket;

public class BaccaratGame implements Runnable {

    private Socket clientSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private ServerHomeController controller;
    ClientInfo clientInfo;

    public BaccaratGame(Socket clientSocket, ServerHomeController controller) throws IOException {
        this.clientSocket = clientSocket;
        this.controller = controller;
        in = new ObjectInputStream(this.clientSocket.getInputStream());
        out = new ObjectOutputStream((this.clientSocket.getOutputStream()));
    }

    @Override
    public void run() {
        System.out.println("[BACCARAT_GAME]: Game started, player -> ");
        try {
            Packet packet = (Packet)in.readObject();
            clientInfo = new ClientInfo(packet);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    controller.updateListView(clientInfo);
                    // TODO: Remaining game logic here, to update anything, update clientInfo


                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            System.out.println("... Gonna run now ...");
                            clientInfo.setStatus(false);
                            controller.updateListView(clientInfo);
                        }
                    });


                }
            });



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