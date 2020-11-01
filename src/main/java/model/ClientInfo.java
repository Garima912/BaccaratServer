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
    private Label wins = new Label("");
    private Label status = new Label("");
    private Label playing = new Label("");
    private ServerHomeController controller;

    public ClientInfo(Packet packet, ServerHomeController controller) {
        this.controller = controller;
        this.name.setText(packet.getPlayerDetails().getPlayerName());
        this.address.setText(packet.getIpAddress());
        this.updateClient(packet);

        HBox nameLine= new HBox(new Label("Name: "));
        nameLine.setSpacing(100);
        nameLine.getChildren().add(this.name);

        HBox addressLine= new HBox(new Label("IP Address: "));
        addressLine.setSpacing(100);
        addressLine.getChildren().add(this.address);

        HBox resultsLine = new HBox(new Label("Results: "));
        resultsLine.getChildren().add(this.result);
        resultsLine.setSpacing(100);

        HBox currentBetLine = new HBox(new Label("Current bet: "));
        currentBetLine.getChildren().add(this.currentBid);

        HBox winLine = new HBox(new Label("Wins: "));
        winLine.getChildren().add(this.wins);
        winLine.setSpacing(100);

        HBox statusLine = new HBox(new Label("Status: "));
        statusLine.getChildren().add(this.status);
        statusLine.setSpacing(100);

        HBox currentlyPlayingLine = new HBox(new Label("Wins: "));
        currentBetLine.getChildren().add(this.playing);
        currentBetLine.setSpacing(100);

        container.setPadding(new Insets(0, 0, 20, 0));
        container.getChildren().addAll(nameLine, addressLine, resultsLine, currentBetLine, winLine, statusLine, currentlyPlayingLine);
        notifyController();
    }

    public void updateClient(Packet packet){
        setCurrentBid(packet.getPlayerDetails().getBidAmount());
        setStatus(packet.getPlayerDetails().isOnline());
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
            resultText = this.result.getText()+", "+resultText;
        }
        this.result.setText(resultText);
        notifyController();
    }

    public Label getCurrentBid() {
        return currentBid;
    }

    public void setCurrentBid(int currentBetText) {
        this.currentBid.setText("$"+String.valueOf(currentBetText));
        notifyController();
    }

    public Label getWins() {
        return wins;
    }

    public void setWins(String winText) {
        String prev = this.wins.getText();
        // don't prefix with comma, the first time
        if (!prev.equals("")){
            winText = this.result.getText()+", $"+winText;
        }
        this.result.setText("$"+winText);
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
        notifyController();
    }

    public Label getPlaying() {
        return playing;
    }

    public void setPlaying(String playingText) {
        this.playing.setText(playingText);
        notifyController();
    }

    public VBox getContainer() {
        return container;
    }


    public void notifyController(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                controller.updateListView(ClientInfo.this);
            }
        });
    }
}