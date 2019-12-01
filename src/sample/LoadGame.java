package sample;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javafx.scene.control.Label;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoadGame implements Initializable {
    public static String loadGameData = "";
    @FXML
    private Label label1;

    @FXML
    private Label label3;

    @FXML
    private Label label2;

    @FXML
    private Pane pane1;

    @FXML
    private Pane pane2;

    @FXML
    private Pane pane3;

    @FXML
    public void goToMainMenu(Event mouseEvent) throws IOException {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        stage.setScene(new Scene(root));
    }
    @FXML
    public void loadGame1(Event mouseEvent) throws IOException {
        loadGameData =label1.getText();
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("Game.fxml"));
        stage.setScene(new Scene(root));
    }
    @FXML
    public void loadGame2(Event mouseEvent) throws IOException {
        loadGameData =label2.getText();
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("Game.fxml"));
        stage.setScene(new Scene(root));
    }
    @FXML
    public void loadGame3(Event mouseEvent) throws IOException {
        loadGameData =label3.getText();
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("Game.fxml"));
        stage.setScene(new Scene(root));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        String filePath = new File("").getAbsolutePath().concat("\\src\\sample\\bin");
        File folder = new File(filePath);
        File[] listOfFiles = folder.listFiles();

        if(listOfFiles.length==0){
            pane1.setDisable(true);
            pane2.setDisable(true);
            pane3.setDisable(true);
        }

        if(listOfFiles.length==1){
            pane2.setDisable(true);
            pane3.setDisable(true);
        }

        else if(listOfFiles.length == 2){
            pane3.setDisable(true);
        }

        try {
            label1.setText(listOfFiles[0].getName());
            label2.setText(listOfFiles[1].getName());
            label3.setText(listOfFiles[2].getName());
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
