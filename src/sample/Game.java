package sample;

import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class Game implements Initializable {

    @FXML
    private Circle pea_1;

    @FXML
    private Label countDown;

    @FXML
    private Circle pea_2;

    @FXML
    private ImageView peaShooter2;

    @FXML
    private ImageView peaShooter1;

    @FXML
    private ImageView zombie1;

    @FXML
    private ImageView zombie2;

    @FXML
    private AnchorPane inGameMenu;

    private int i = 300;
    private ParallelTransition seqT;
    @Override
    public void initialize(URL url, ResourceBundle rb){
        // Animation 1
        TranslateTransition peaTransition= new TranslateTransition();
        peaTransition.setDuration(Duration.millis(3500));
        peaTransition.setNode(pea_1);
        peaTransition.setToX(1000);
        peaTransition.setCycleCount(TranslateTransition.INDEFINITE);

        // Animation 2
        TranslateTransition peaTransition2= new TranslateTransition();
        peaTransition2.setDuration(Duration.millis(3500));
        peaTransition2.setNode(pea_2);
        peaTransition2.setToX(1000);
        peaTransition2.setCycleCount(TranslateTransition.INDEFINITE);


        // Animation 3
        TranslateTransition zombieTransition= new TranslateTransition();
        zombieTransition.setDuration(Duration.seconds(30));
        zombieTransition.setNode(zombie1);
        zombieTransition.setToX(-500);
        zombieTransition.setCycleCount(TranslateTransition.INDEFINITE);

        // Animation 4
        TranslateTransition zombieTransition2= new TranslateTransition();
        zombieTransition2.setDuration(Duration.seconds(20));
        zombieTransition2.setNode(zombie2);
        zombieTransition2.setToX(-500);
        zombieTransition2.setCycleCount(TranslateTransition.INDEFINITE);



        seqT = new ParallelTransition(peaTransition,peaTransition2,zombieTransition,zombieTransition2);
        seqT.play();

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                javafx.application.Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        countDown.setText(returnTime(i--));
                    }
                });
            }
        }, 0, 1000);
    }


    public void inGameMenu(){
        seqT.pause();
        inGameMenu.setVisible(true);

    }
    public void returnToMainMenu(Event mouseEvent) throws IOException{

        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        stage.setScene(new Scene(root));

    }
    public void resumeGame(){
        seqT.play();
        inGameMenu.setVisible(false);
    }



    public String returnTime(int totalSecs) {

        int minutes = (totalSecs % 3600) / 60;
        int seconds = totalSecs % 60;

        return String.format("%02d:%02d", minutes, seconds);
    }

}
