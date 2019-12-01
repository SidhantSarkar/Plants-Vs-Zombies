package sample;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.Lighting;
import javafx.scene.effect.Shadow;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.nio.file.*;
public class ChooseLevel implements Initializable {
    public static int userSelectedLevel=-1;

    @FXML
    private Pane level1;

    @FXML
    private Pane level2;

    @FXML
    private Pane level3;

    @FXML
    private Pane level4;

    @FXML
    private Pane level5;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        int level=1;
        try{
            String filePath = new File("").getAbsolutePath().concat("\\src\\sample\\level.txt");
//            System.out.println(filePath);
            File file = new File(filePath);
            BufferedReader br = new BufferedReader(new FileReader(file));
            level = Integer.parseInt(br.readLine());

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        switch (level){
            case 1:
                level2.setDisable(true);
                level3.setDisable(true);
                level4.setDisable(true);
                level5.setDisable(true);
                break;
            case 2:
                level2.setEffect(null);

                level3.setDisable(true);
                level4.setDisable(true);
                level5.setDisable(true);
                break;
            case 3:
                level2.setEffect(null);
                level3.setEffect(null);

                level4.setDisable(true);
                level5.setDisable(true);
                break;
            case 4:
                level2.setEffect(null);
                level3.setEffect(null);
                level4.setEffect(null);

                level5.setDisable(true);

                break;
            case 5:
                level2.setEffect(null);
                level3.setEffect(null);
                level4.setEffect(null);
                level5.setEffect(null);
                break;
        }
    }
    @FXML
    public void goToMainMenu(Event mouseEvent) throws IOException {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        stage.setScene(new Scene(root));
    }

    @FXML
    public void level1Game(Event mouseEvent) throws IOException {
        userSelectedLevel=1;
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("Game.fxml"));
        stage.setScene(new Scene(root));
    }

    @FXML
    public void level2Game(Event mouseEvent) throws IOException {
        userSelectedLevel=2;
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("Game.fxml"));
        stage.setScene(new Scene(root));
//          System.out.println("LEVEL 2");

    }

    @FXML
    public void level3Game(Event mouseEvent) throws IOException {
        userSelectedLevel=3;
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("Game.fxml"));
        stage.setScene(new Scene(root));
        System.out.println("LEVEL 3");

    }

    @FXML
    public void level4Game(Event mouseEvent) throws IOException {
        userSelectedLevel=4;
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("Game.fxml"));
        stage.setScene(new Scene(root));
        System.out.println("LEVEL 4");

    }

    @FXML
    public void level5Game(Event mouseEvent) throws IOException {
        userSelectedLevel=5;
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("Game.fxml"));
        stage.setScene(new Scene(root));
        System.out.println("LEVEL 5");
    }
}
