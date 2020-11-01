package controller;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.ClientInfo;
import model.Packet;
import util.Util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerHomeController extends Thread implements EventHandler {
    VBox parent;
    int port;
    Label clientCount = new Label();
    Button powerBtn =  new Button();
    ListView clientsList;
    ServerSocket listener;
    private ObjectInputStream in;
    ArrayList<ClientInfo> clientData = new ArrayList<>();
    ArrayList<BaccaratGame> baccaratGames = new ArrayList<>();
    ArrayList<Thread> runningThreads = new ArrayList<>();

    static ExecutorService executor = Executors.newFixedThreadPool(10);
    public ServerHomeController(VBox parent, String port) {
        this.parent = parent;
        this.port = Integer.valueOf(port);
        setUpScene();

    }

    public void setUpScene(){
        parent.setPadding(new Insets(20, 20, 20, 20));
        parent.setSpacing(20);

        clientsList = new ListView();
        clientsList.setPlaceholder(new Label("No one is logged in yet"));
        clientsList.setMaxHeight(400);

        HBox clientCountBox = new HBox();
        clientCountBox.getChildren().add(new Label("Number of clients: "));
        clientCount.setText(String.valueOf(clientData.size()));
        clientCountBox.getChildren().add(clientCount);

        powerBtn.setText("Turn off Server");
        powerBtn.setOnAction(this);

        parent.getChildren().addAll(clientCountBox, clientsList, powerBtn);

    }

    @Override
    public void run() {
        try {
            listener = new ServerSocket(port);
            while (true){
                System.out.println("[SERVER]: Listening for connections...");
                Socket clientSocket = listener.accept();
                System.out.println("[SERVER]: Connected to client");

                // get packet from client send to client info for ui updates
                in = new ObjectInputStream(clientSocket.getInputStream());
                Packet packet = (Packet) in.readObject();
                if (packet.actionRequest.equals(Util.ACTION_REQUEST_CONNECT)){
                    new ClientInfo(packet, this);
                    clientPlayPressed(clientSocket).start();

                }


            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Thread clientPlayPressed(final Socket clientSocket){
        // a thread that waits for 1 more response from user
        return new Thread(){
            @Override
            public void run() {
                try {
                    // start a game for the client, add game to list of games
                    System.out.println("reading input...");
                    Packet packet = (Packet) in.readObject();
                    if (packet.actionRequest.equals(Util.ACTION_REQUEST_PLAY)){
                        System.out.println("amount trynna bet is "+packet.getPlayerDetails().getBidAmount());
                        BaccaratGame baccaratGame = new BaccaratGame(clientSocket, in,  ServerHomeController.this);
                        baccaratGames.add(baccaratGame);
                        runningThreads.add(this);
                        System.out.println("DONE");
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                System.out.println("done reading input....");
            }
        };

    }


    public void updateListView(ClientInfo clientInfo){      // first time connecting, add
        clientsList.getItems().clear();
        int position = posInServer(clientInfo);
        if (position == -1){
            System.out.println("First time client: "+ clientInfo.getAddress().getText());
            clientData.add(clientInfo);
        }
        else{       // update clientInfo at that position
            System.out.println("existing client "+ clientInfo.getAddress().getText());
            clientData.set(position, clientInfo);
        }
        // platform.runLater enables syncing. since they're in different threads
        Platform.runLater(new Runnable() {
            @Override public void run() {
                for (ClientInfo item: clientData){
                    clientsList.getItems().add(item.getContainer());
                }
                clientCount.setText(String.valueOf(clientData.size()));
            }
        });
    }



    int posInServer(ClientInfo clientInfo){
        String ipAddress = clientInfo.getAddress().getText();
        for (int i=0; i<clientData.size(); i++){
            if (clientData.get(i).getAddress().getText().contains(ipAddress)){
                return i;
            }
        }
        return -1;
    }

    @Override
    public void handle(Event event) {
        if (event.getSource() == powerBtn){     // close all sockets and then exit
            for (BaccaratGame game: baccaratGames){
                try {
                    game.closeConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            for (BaccaratGame game: baccaratGames){
                try {
                    game.closeConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            executor.shutdownNow();
            Platform.exit();
            System.exit(0);
        }
    }
}
