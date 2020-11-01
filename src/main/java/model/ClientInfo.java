package model;

/**
 * This class is the GUI representation of a client.
 * so fields are not primitive types. They're javaFx components
 */
import controller.ServerHomeController;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class ClientInfo{
    private VBox container = new VBox();
    private Label address = new Label("");
    private Label name = new Label("");
    private Label result = new Label("");
    private Label currentBid = new Label();
    private Label totalWins = new Label("");
    private Label status = new Label("");
    private Label playing = new Label("");
    private ServerHomeController controller;

    public ClientInfo(Packet packet, ServerHomeController controller) {
        this.controller = controller;
        this.name.setText(packet.getPlayerDetails().getPlayerName());
        this.address.setText(packet.getIpAddress());
        this.updateClient(packet);

        final HBox nameLine = new HBox(new Label("Name: "));
        nameLine.setSpacing(100);
        nameLine.getChildren().add(this.name);

        final HBox addressLine= new HBox(new Label("IP Address: "));
        addressLine.setSpacing(100);
        addressLine.getChildren().add(this.address);

        final HBox resultsLine = new HBox(new Label("Results: "));
        resultsLine.getChildren().add(this.result);
        resultsLine.setSpacing(100);

        final HBox currentBetLine = new HBox(new Label("Current bet: "));
        currentBetLine.getChildren().add(this.currentBid);
        currentBetLine.setSpacing(100);

        final HBox winLine = new HBox(new Label("Total Wins: "));
        winLine.getChildren().add(this.totalWins);
        winLine.setSpacing(100);

        final HBox statusLine = new HBox(new Label("Status: "));
        statusLine.getChildren().add(this.status);
        statusLine.setSpacing(100);

        final HBox currentlyPlayingLine = new HBox(new Label("Currently playing: "));
        currentlyPlayingLine.getChildren().add(this.playing);
        currentlyPlayingLine.setSpacing(100);

        container.setPadding(new Insets(0, 0, 20, 0));
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                container.getChildren().addAll(nameLine, addressLine, resultsLine, currentBetLine, winLine, statusLine, currentlyPlayingLine);
            }
        });
        System.out.println("constructor called ");
        notifyController();
    }

    public void updateClient(final Packet packet){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                setCurrentBid(packet.getPlayerDetails().getBidAmount());
                setStatus(packet.getPlayerDetails().isOnline());
                setPlaying(packet.getClientPlaying());
                setStatus(packet.isServerStatus());
            }
        });
    }

    public Label getName() {
        return name;
    }

    public Label getAddress(){
        return address;
    }

    public Label getResult() {
        return result;
    }

    public void setResult(String resultText) {
        String prev = this.result.getText();
        // don't prefix with comma, the first time
        if (!prev.equals("")){
            resultText = this.result.getText()+", "+ resultText;
        }
        this.result.setText(resultText);
        System.out.println("set result called");
        notifyController();
    }

    public Label getCurrentBid() {
        return currentBid;
    }

    public void setCurrentBid(int currentBetText) {
        this.currentBid.setText("$"+String.valueOf(currentBetText));
        System.out.println("set current bid called");
        notifyController();
    }

    public Label getTotalWins() {
        return totalWins;
    }

    public void setTotalWins(String winText) {
        String prev = this.totalWins.getText();
        // don't prefix with comma, the first time
        if (!prev.equals("")){
            winText = this.result.getText()+", $"+winText;
        }
        this.result.setText("$"+winText);
        System.out.println("set wins called");
        notifyController();
    }

    public Label getStatus() {
        return status;
    }

    public void setStatus(boolean statusBool) {
        if (statusBool){
            this.status.setText("Online");
            this.status.setTextFill(Color.GREEN);
        }
        else{
            this.status.setText("Offline");
            this.status.setTextFill(Color.INDIANRED);
        }
        System.out.println("set status called");
        notifyController();
    }

    public Label getPlaying() {
        return playing;
    }

    public void setPlaying(int playCount) {
        if (playCount<0){
            this.playing.setText("Not Playing");
            this.playing.setTextFill(Color.INDIANRED);
        }
        else {
            this.playing.setText(String.valueOf(playCount));
            this.playing.setTextFill(Color.BLACK);
        }
        System.out.println("setPlaying called");
        notifyController();
    }

    public VBox getContainer() {
        return container;
    }


    public void notifyController(){
        controller.updateListView(this);
//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//                controller.updateListView(ClientInfo.this);
//            }
//        });
    }
}