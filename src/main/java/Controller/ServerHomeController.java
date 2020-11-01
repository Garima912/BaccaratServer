package Controller;

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

import java.io.IOException;
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
    ArrayList<ClientInfo> clientsInfo = new ArrayList<>();
    ArrayList<BaccaratGame> baccaratGames = new ArrayList<>();

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
        clientCount.setText(String.valueOf(clientsInfo.size()));
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
                BaccaratGame baccaratGame = new BaccaratGame(clientSocket, this);
                baccaratGames.add(baccaratGame);
                executor.execute(baccaratGame.runnable);
                System.out.println("DONE");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void updateListView(ClientInfo clientInfo){      // first time connecting, add
        clientsList.getItems().clear();
        int position = posInServer(clientInfo);
        if (position == -1){
            System.out.println("First time client: "+ clientInfo.getAddress());
            clientsInfo.add(clientInfo);
        }
        else{       // update clientInfo at that position
            System.out.println("existing client "+ clientInfo.getAddress());
            clientsInfo.set(position, clientInfo);
        }
        // platform.runLater enables syncing. since they're in different threads
        Platform.runLater(new Runnable() {
            @Override public void run() {
                for (ClientInfo item: clientsInfo){
                    clientsList.getItems().add(item.getContainer());
                }
                clientCount.setText(String.valueOf(clientsInfo.size()));
            }
        });
    }



    int posInServer(ClientInfo clientInfo){
        String ipAddress = clientInfo.getAddress().toString();
        for (int i = 0; i< clientsInfo.size(); i++){
            if (clientsInfo.get(i).getAddress().toString().contains(ipAddress)){
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
                // TODO: loop through all games close their sockets
                executor.shutdownNow();
            }
            Platform.exit();
        }
    }
}
