package sample;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenu {

    @FXML
    void exit() {
        System.exit(0);
    }

    @FXML
    public void chooseLevel(Event mouseEvent) throws IOException {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("ChooseLevel.fxml"));
        stage.setScene(new Scene(root));
    }

    @FXML
    public void loadGame(Event mouseEvent) throws IOException {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("LoadGame.fxml"));
        stage.setScene(new Scene(root));
    }
}
