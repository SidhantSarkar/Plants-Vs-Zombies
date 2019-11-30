package sample;

import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
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
    private ImageView peaShooterPlant;

    @FXML
    private ImageView sunFlowerPlant;

    @FXML
    public GridPane gameGrid;

    @FXML
    public Label sunTokenLabel;

    private int i = 300;
    private ParallelTransition seqT;
    private boolean stopFlag;
    ArrayList<ArrayList<Zombie>> zombieGrid = new ArrayList<ArrayList<Zombie>>();
    ArrayList<Zombie> zombieInRow = new ArrayList<Zombie>();
    int frequency = 5000;
    int sunTokenGen = 0;
    int currency = 0;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        for (int j=0;j<5;j++){
            zombieGrid.add(new ArrayList<Zombie>());
        }

        seqT = new ParallelTransition();
        seqT.play();
        this.stopFlag = false;
        Random seed =  new Random();

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                javafx.application.Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if(!stopFlag)
                            countDown.setText(returnTime(i--));
                        if(sunTokenGen == 1)
                            generateSunToken(seed);
                        frequency-=3;
//                        Change sun token Frequency
                        sunTokenGen = (sunTokenGen+1)%8;
                    }
                });
            }
        }, 0, 1000);
        generateZombie(seed);
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
                        int temp = seed.nextInt(5);
                        Zombie genZombie = new Zombie(gameGrid, ref, temp);
                        zombieGrid.get(temp).add(genZombie);
                        zombieInRow.add(genZombie);
                    }
                });
            }
        }, 0, frequency);
    }


    public void inGameMenu(){
        seqT.pause();
        this.stopFlag = true;
        inGameMenu.setVisible(true);

    }
    public void returnToMainMenu(Event mouseEvent) throws IOException{

        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        stage.setScene(new Scene(root));

    }
    public void resumeGame(){
        seqT.play();
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
            cb.putImage(img);
            db.setContent(cb);
        }
        if(source.equals("sunFlowerPlant")){
            Image img = new Image(".\\sample\\Img_Assets\\plants\\sunflower_instance.png",60,60,true,true);
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
        Plant temp = new Plant(x,y, source, pea_2, zombieGrid.get(x), this);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                javafx.application.Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        temp.genSunAnim(timer);
                    }
                });
            }
        }, 0, 6000);
    }

    public void removeZombie(Zombie temp) {
        zombieGrid.get(temp.row).remove(temp);
    }

}

class Plant {
    int health;
    int x,y;
    double absX, absY;
    ImageView container;
    Circle copy;
    GridPane parent;
    ArrayList<Zombie> zombieInRowArray;
    private Game reference;

    ParallelTransition anim;
    Timer timer;
    Timer mainTimer;


    Plant(int _x, int _y, ImageView _container, Circle _copy, ArrayList<Zombie> zombie, Game _reference) {
        x = _x;
        y = _y;
        health = 20;
        container = _container;
        copy = _copy;
        parent = (GridPane) container.getParent();
        zombieInRowArray = zombie;
        reference = _reference;
        absX = container.getBoundsInParent().getMinX();
        absY = container.getBoundsInParent().getMinY();
        anim = new ParallelTransition();
        timer = new Timer();
        mainTimer = new Timer();
        getHitByZombie();
    }

    public void startAnim(Timer _timer) {
        mainTimer = _timer;
        Circle temp =  new Circle(absX,absY,10,copy.getFill());
        temp.setStroke(copy.getStroke());
        temp.setStrokeWidth(copy.getStrokeWidth());
        temp.setStrokeType(copy.getStrokeType());
        parent.add(temp,y,x);
        temp.setTranslateX(25);
        temp.setTranslateY(-12);

        TranslateTransition peaTransition= new TranslateTransition();
        peaTransition.setDuration(Duration.millis(3500));
        peaTransition.setNode(temp);
        peaTransition.setToX(800);
        peaTransition.setCycleCount(1);

        anim.getChildren().add(peaTransition);
        anim.play();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                javafx.application.Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        zombieInRowArray.forEach(zombie -> {
                            if(temp.getBoundsInParent().intersects(zombie.zombieSpawner.getBoundsInParent())){
                                temp.setRadius(0);
                                temp.setTranslateZ(10);
                                zombie.health--;
                                anim.stop();
                                timer.cancel();
                                timer.purge();
                                return;
                            }
                        });
                    }
                });
            }
        }, 0, 250);
    }

    public void genSunAnim(Timer _timer){
        mainTimer = _timer;
        ImageView sunTokenContainer = new ImageView();
        Image sunTokenImg = new Image(".\\sample\\Img_Assets\\sunToken.png",50,50,true,true);
        sunTokenContainer.setImage(sunTokenImg);
        sunTokenContainer.setFitHeight(50);
        parent.add(sunTokenContainer,y,x);
//        Transition up and Change image of SunFlower

        sunTokenContainer.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                reference.currency += 50;
                reference.sunTokenLabel.setText(String.valueOf(reference.currency));
                anim.stop();
                reference.gameGrid.getChildren().remove(sunTokenContainer);
            }
        });
        Random seed = new Random();
        TranslateTransition sunTokenTransition= new TranslateTransition();
        sunTokenTransition.setDuration(Duration.millis(3500));
        sunTokenTransition.setNode(sunTokenContainer);
        sunTokenTransition.setToX(seed.nextInt(2) == 0 ? seed.nextInt(30): -1*seed.nextInt(30));
        sunTokenTransition.setToY(30);
        sunTokenTransition.setCycleCount(1);

        anim.getChildren().add(sunTokenTransition);
        anim.play();
    }

    public void getHitByZombie() {
        Timer zombieTime = new Timer();
        zombieTime.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                javafx.application.Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        zombieInRowArray.forEach(zombie -> {
                            if(container.getBoundsInParent().intersects(zombie.zombieSpawner.getBoundsInParent())){
                                zombie.movementFlag=false;
                                health--;
                                if(health == 0){
                                    anim.stop();
                                    zombie.movementFlag=true;
                                    mainTimer.cancel();
                                    mainTimer.purge();
                                    timer.cancel();
                                    timer.purge();
                                    zombieTime.cancel();
                                    zombieTime.purge();
                                    parent.getChildren().remove(container);
                                }
                                return;
                            }
                        });
                    }
                });
            }
        }, 0, 250);
    }
}

class Zombie {
    public int health;
    public ImageView zombieSpawner;
    public GridPane gameGrid;
    Game reference;
    int row;
    Boolean movementFlag;

    Zombie(GridPane _grid, Game _ref, int _row) {
        health = 5;
        movementFlag = true;
        gameGrid = _grid;
        reference=_ref;
        zombieSpawner = new ImageView();
        row = _row;
        startMovement();
    }

    public void startMovement() {
        Image zombie = new Image(".\\sample\\Img_Assets\\zombie1.gif",90,90,true,true);
        zombieSpawner.setImage(zombie);
        zombieSpawner.setFitHeight(90);
        gameGrid.add(zombieSpawner,9,row);
        zombieSpawner.setTranslateX(150);
        zombieSpawner.setTranslateY(-5);

        Zombie thisZombie = this;

        TranslateTransition zombieTransition= new TranslateTransition();
        zombieTransition.setDuration(Duration.seconds(25));
        zombieTransition.setNode(zombieSpawner);
        zombieTransition.setToX(-570);
        zombieTransition.setCycleCount(TranslateTransition.INDEFINITE);

        ParallelTransition zombieAnim = new ParallelTransition(zombieTransition);
        zombieAnim.play();

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                javafx.application.Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if(!movementFlag){
                            zombieAnim.stop();
                        }
                        else if(health==0) {
                            zombieAnim.stop();
                            gameGrid.getChildren().remove(zombieSpawner);
                            timer.cancel();
                            timer.purge();
                            reference.removeZombie(thisZombie);
                        }
                        else if(movementFlag){
                            zombieAnim.play();
                        }

                    }
                });
            }
        }, 0, 200);

    }
}
