package sample;

import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.net.URL;
import java.util.*;

public class Game implements Initializable {
    @FXML
    private Label countDown;

    @FXML
    private Circle pea_2;

    @FXML
    private AnchorPane inGameMenu;

    @FXML
    public AnchorPane gameOverMenu;

    @FXML
    public GridPane gameGrid;

    @FXML
    public Label sunTokenLabel;

    @FXML
    private ImageView sunFlowerPlant;

    @FXML
    private ImageView cherryBomb;

    @FXML
    private ImageView potatoBarrier;

    @FXML
    private ImageView doublePeaShooter;

    @FXML
    private ImageView peaShooterPlant;

    private int timerSeconds = 300;
    public boolean stopFlag;
    ArrayList<ArrayList<Zombie>> zombieGrid = new ArrayList<ArrayList<Zombie>>();
    int frequency = 5000;
    int sunTokenGen = 0;
    int currency = 0;
    String selected = "";

    @Override
    public void initialize(URL url, ResourceBundle rb){
//        Level
        for (int j=0;j<5;j++){
            zombieGrid.add(new ArrayList<Zombie>());
            LawnMower temp = new LawnMower(this,j);
        }

        this.stopFlag = false;
        Random seed =  new Random();

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                javafx.application.Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if(!stopFlag){
                            countDown.setText(returnTime(timerSeconds--));
                            if(sunTokenGen == 1)
                                generateSunToken(seed);
                            frequency-=2;
//                        Change sun token Frequency
                            sunTokenGen = (sunTokenGen+1)%8;
                        }
                        if(timerSeconds==0){
//                           LEVEL WIN
                            stopFlag=true;
                            timer.cancel();
                            timer.purge();
                            levelClear();
                        }
                        if(currency<50){
                            sunFlowerPlant.setEffect(new Lighting());
                            sunFlowerPlant.setDisable(true);
                            peaShooterPlant.setEffect(new Lighting());
                            peaShooterPlant.setDisable(true);
                            potatoBarrier.setEffect(new Lighting());
                            potatoBarrier.setDisable(true);
                            cherryBomb.setEffect(new Lighting());
                            cherryBomb.setDisable(true);
                            doublePeaShooter.setEffect(new Lighting());
                            doublePeaShooter.setDisable(true);
                        }
                        else if(currency<100){
                            sunFlowerPlant.setEffect(null);
                            sunFlowerPlant.setDisable(false);
                            peaShooterPlant.setEffect(new Lighting());
                            peaShooterPlant.setDisable(true);
                            potatoBarrier.setEffect(null);
                            potatoBarrier.setDisable(false);
                            cherryBomb.setEffect(new Lighting());
                            cherryBomb.setDisable(true);
                            doublePeaShooter.setEffect(new Lighting());
                            doublePeaShooter.setDisable(true);
                        }
                        else if(currency<150){
                            sunFlowerPlant.setEffect(null);
                            sunFlowerPlant.setDisable(false);
                            peaShooterPlant.setEffect(null);
                            peaShooterPlant.setDisable(false);
                            potatoBarrier.setEffect(null);
                            potatoBarrier.setDisable(false);
                            cherryBomb.setEffect(new Lighting());
                            cherryBomb.setDisable(true);
                            doublePeaShooter.setEffect(new Lighting());
                            doublePeaShooter.setDisable(true);
                        }
                        else if(currency<200) {
                            sunFlowerPlant.setEffect(null);
                            sunFlowerPlant.setDisable(false);
                            peaShooterPlant.setEffect(null);
                            peaShooterPlant.setDisable(false);
                            potatoBarrier.setEffect(null);
                            potatoBarrier.setDisable(false);
                            cherryBomb.setEffect(null);
                            cherryBomb.setDisable(false);
                            doublePeaShooter.setEffect(new Lighting());
                            doublePeaShooter.setDisable(true);
                        }
                        else if (currency<Integer.MAX_VALUE) {
                            sunFlowerPlant.setEffect(null);
                            sunFlowerPlant.setDisable(false);
                            peaShooterPlant.setEffect(null);
                            peaShooterPlant.setDisable(false);
                            potatoBarrier.setEffect(null);
                            potatoBarrier.setDisable(false);
                            cherryBomb.setEffect(null);
                            cherryBomb.setDisable(false);
                            doublePeaShooter.setEffect(null);
                            doublePeaShooter.setDisable(false);
                        }
                    }
                });
            }
        }, 0, 1000);
        generateZombie(seed);
    }

    public void levelClear() {
        System.out.println("Tum Jeet Gaye");
    }

    public void generateSunToken(Random seed){
        ImageView sunTokenContainer = new ImageView();
        Image sunTokenImg = new Image(".\\sample\\Img_Assets\\sunToken.png",50,50,true,true);
        sunTokenContainer.setImage(sunTokenImg);
        sunTokenContainer.setFitHeight(50);
        gameGrid.add(sunTokenContainer,seed.nextInt(10),0);
        sunTokenContainer.setTranslateX(seed.nextInt(20));
        sunTokenContainer.setTranslateY(-60 + seed.nextInt(5));

        ParallelTransition anim = new ParallelTransition();

        sunTokenContainer.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                currency += 50;
                sunTokenLabel.setText(String.valueOf(currency));
                anim.stop();
                gameGrid.getChildren().remove(sunTokenContainer);
            }
        });

        TranslateTransition sunTokenTransition = new TranslateTransition();
        sunTokenTransition.setDuration(Duration.millis(9000));
        sunTokenTransition.setNode(sunTokenContainer);
        sunTokenTransition.setToY(400);
        sunTokenTransition.setCycleCount(1);

        anim.getChildren().add(sunTokenTransition);
        anim.play();
    }

    public void generateZombie(Random seed) {
        Game ref = this;
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                javafx.application.Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
//                       Level Selector
                        if(!stopFlag){
                            int temp = seed.nextInt(5);
                            Zombie genZombie = new Zombie(gameGrid, ref, temp);
                            zombieGrid.get(temp).add(genZombie);
                        }
                    }
                });
            }
        }, 0, frequency);
    }


    public void inGameMenu(){
        this.stopFlag = true;
        inGameMenu.setVisible(true);

    }
    public void returnToMainMenu(Event mouseEvent) throws IOException{

        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        stage.setScene(new Scene(root));

    }

    public void restartGame(Event mouseEvent) throws IOException{

        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("Game.fxml"));
        stage.setScene(new Scene(root));

    }

    public void resumeGame(){
        this.stopFlag = false;
        inGameMenu.setVisible(false);
    }

    public String returnTime(int totalSecs) {

        int minutes = (totalSecs % 3600) / 60;
        int seconds = totalSecs % 60;

        return String.format("%02d:%02d", minutes, seconds);
    }

    public void handleDragDetection(Event event) throws IOException {
        String source = ((ImageView) event.getSource()).getId();
        Dragboard db = ((ImageView) event.getSource()).startDragAndDrop(TransferMode.ANY);
        ClipboardContent cb = new ClipboardContent();

        if(source.equals("peaShooterPlant")){
            Image img = new Image(".\\sample\\Img_Assets\\plants\\peaShooter_instance.png",60,60,true,true);

            selected = "peaShooterPlant";
            cb.putImage(img);
            db.setContent(cb);
        }
        if(source.equals("sunFlowerPlant")){
            Image img = new Image(".\\sample\\Img_Assets\\plants\\sunflower_instance.png",60,60,true,true);

            selected = "sunFlowerPlant";
            cb.putImage(img);
            db.setContent(cb);
        }

        if(source.equals("potatoBarrier")){
            Image img = new Image(".\\sample\\Img_Assets\\plants\\potatoInstance.png",60,60,true,true);

            selected = "potatoBarrier";
            cb.putImage(img);
            db.setContent(cb);
        }

        if(source.equals("doublePeaShooter")){
            Image img = new Image(".\\sample\\Img_Assets\\plants\\doublePeaShooter.png",60,60,true,true);

            selected = "doublePeaShooter";
            cb.putImage(img);
            db.setContent(cb);
        }

        if(source.equals("cherryBomb")){
            Image img = new Image(".\\sample\\Img_Assets\\plants\\cherryInstance.png",70,70,true,true);

            selected = "cherryBomb";
            cb.putImage(img);
            db.setContent(cb);
        }

//        Collision Detection
//        System.out.println(peaShooter2.getBoundsInParent().intersects(zombie1.getBoundsInParent()));
//        System.out.println(zombie1.getBoundsInParent().intersects(pea_2.getBoundsInParent()));

        event.consume();
    }

    public void handleDragOver(DragEvent event){
        Image img = ((ImageView) event.getSource()).getImage();
        if((img == null || img.isError()) && event.getDragboard().hasImage()) {
            event.acceptTransferModes(TransferMode.ANY);
        }
    }

    public void handleDragDrop(DragEvent event) {
        int x = -1;
        int y = (int) ((ImageView)event.getSource()).getProperties().get("gridpane-column");
        try{
            x = (int) ((ImageView)event.getSource()).getProperties().get("gridpane-row");
        } catch (Exception e) {
            x = 0;
        }

        Image img = event.getDragboard().getImage();

        ImageView source = ((ImageView) event.getSource());
        source.setImage(img);
        source.setFitHeight(65);

        if(selected.equals("sunFlowerPlant")){
            Plant temp = new Plant(x,y, source, pea_2, zombieGrid.get(x), this);
            currency -= 50;
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    javafx.application.Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if(!stopFlag){
                                temp.genSunAnim(timer);
                            }
                        }
                    });
                }
            }, 2000, 6000);
        }

        if(selected.equals("peaShooterPlant")){
            Plant temp = new Plant(x,y, source, pea_2, zombieGrid.get(x), this);
            currency -= 100;
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    javafx.application.Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if(!stopFlag) {
                                temp.startAnim(timer);
                            }
                        }
                    });
                }
            }, 0, 2000);
        }

        if(selected.equals("potatoBarrier")){
            Plant temp = new Plant(x,y, source, pea_2, zombieGrid.get(x), this);
            currency -= 50;
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    javafx.application.Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if(!stopFlag){
                                temp.genSunAnim(timer);
                            }
                        }
                    });
                }
            }, 2000, 6000);
        }

        if(selected.equals("doublePeaShooter")){
            Plant temp = new Plant(x,y, source, pea_2, zombieGrid.get(x), this);
            currency -= 200;
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    javafx.application.Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if(!stopFlag){
                                temp.genSunAnim(timer);
                            }
                        }
                    });
                }
            }, 2000, 6000);
        }

        if(selected.equals("cherryBomb")){
            source.setFitHeight(75);
            Plant temp = new Plant(x,y, source, pea_2, zombieGrid.get(x), this);
            currency -= 150;
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    javafx.application.Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if(!stopFlag){
                                temp.genSunAnim(timer);
                            }
                        }
                    });
                }
            }, 2000, 6000);
        }
        sunTokenLabel.setText(String.valueOf(currency));

    }

    public void removeZombie(Zombie temp) {
        zombieGrid.get(temp.row).remove(temp);
    }

}






