import Controller.ServerHomeController;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application implements EventHandler {

    private Stage primaryStage;
    private TextField ipTextField;
    private VBox networkDetails;
    private TextField portTextField;
    private StackPane networkParent;
    private Button startGameBtn;
    private Label errorText;

    @Override
    public void start(Stage primaryStage) throws Exception{
        // initializations
        this.primaryStage = primaryStage;
        ipTextField = new TextField();
        networkDetails = new VBox();
        portTextField = new TextField();
        networkParent = new StackPane();
        startGameBtn = new Button("Start Game");
        errorText = new Label("Please check port number again");
        errorText.setVisible(false);

        Label heading = new Label("Where would you like to host the game? ");
        ipTextField = new TextField();
        ipTextField.setPromptText("IP: 127.0.0.1"); //to set the hint text
        ipTextField.setDisable(true);
        portTextField = new TextField();
        portTextField.setPromptText("Port: e.g 9090 ");
        startGameBtn.setOnAction(this);
        errorText.setTextFill(Paint.valueOf("#FF0000"));

        networkDetails.getChildren().addAll(heading, ipTextField, portTextField, startGameBtn);
        networkDetails.setMaxWidth(300); networkDetails.setMaxHeight(300);
        networkParent.getChildren().addAll(networkDetails, errorText);
        networkParent.setAlignment(networkDetails, Pos.CENTER);

        portTextField.getParent().requestFocus();
        primaryStage.setTitle("Baccarrat Server");
        primaryStage.setScene(new Scene(networkParent, 700,550));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void handle(Event event) {
        if (event.getSource() == startGameBtn){

            try {
                validateNetworkInfo();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void validateNetworkInfo() throws IOException {
        VBox serverHome = new VBox();
        if (portTextField.getText().length() == 4){
            // go to new scene
            primaryStage.setScene(new Scene(serverHome, 700, 550));
            ServerHomeController sHController = new ServerHomeController(serverHome, portTextField.getText());
            sHController.start();
        }
        else{
            errorText.setVisible(true);
        }
    }
}
