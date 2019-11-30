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
        temp.toBack();

        TranslateTransition peaTransition = new TranslateTransition();
        peaTransition.setDuration(Duration.millis(2200));
        peaTransition.setNode(temp);
        peaTransition.setToX(700);
        peaTransition.setCycleCount(1);
        peaTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                temp.setRadius(0);
                temp.setTranslateZ(10);

                reference.gameGrid.getChildren().remove(temp);
                anim.getChildren().remove(peaTransition);
            }
        });

        anim.getChildren().add(peaTransition);
        anim.play();

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                javafx.application.Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if(!reference.stopFlag){
                              zombieInRowArray.forEach(zombie -> {
                                    if(temp.getBoundsInParent().intersects(zombie.zombieSpawner.getBoundsInParent())){
                                        anim.stop();
                                        timer.cancel();
                                        timer.purge();
                                        temp.setRadius(0);
                                        temp.setTranslateZ(10);
                                        reference.gameGrid.getChildren().remove(temp);
                                        zombie.health--;
                                    }
                              });
                        }
                    }
                });
            }
        }, 0, 150);
    }

    public void genSunAnim(Timer _timer){
        mainTimer = _timer;
        ImageView sunTokenContainer = new ImageView();
        Image sunTokenImg = new Image(".\\sample\\Img_Assets\\sunToken.png",50,50,true,true);
        sunTokenContainer.setImage(sunTokenImg);
        sunTokenContainer.setFitHeight(50);
        parent.add(sunTokenContainer,y,x);

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
                        if(!reference.stopFlag){
                            zombieInRowArray.forEach(zombie -> {
                                if(container.getBoundsInParent().intersects(zombie.zombieSpawner.getBoundsInParent())){
                                    zombie.movementFlag=false;
                                    health--;
                                    if(health == 0){
                                        anim.stop();
                                        zombie.movementFlag=true;
                                        zombieTime.cancel();
                                        zombieTime.purge();
                                        mainTimer.cancel();
                                        mainTimer.purge();
                                        timer.cancel();
                                        timer.purge();
                                        parent.getChildren().remove(container);
                                    }
                                    return;
                                }
                            });
                        }
                    }
                });
            }
        }, 0, 200);
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
        zombieSpawner.setTranslateX(220);
        zombieSpawner.setTranslateY(-5);

        Zombie thisZombie = this;

        TranslateTransition zombieTransition= new TranslateTransition();
        zombieTransition.setDuration(Duration.seconds(38));
        zombieTransition.setNode(zombieSpawner);
        zombieTransition.setToX(-640);
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
                        if(reference.stopFlag){
                            zombieAnim.stop();
                        }
                        else{
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
                    }
                });
            }
        }, 0, 200);

    }
}

class LawnMower{
    ImageView container;
    Game reference;
    int row;
    Rectangle rect;

    LawnMower(Game _reference, int _row){
        reference = _reference;
        row = _row;

        rect = new Rectangle();
        rect.setHeight(75);
        rect.setWidth(50);
        reference.gameGrid.add(rect,0,row);
        rect.setVisible(false);

        container = new ImageView();
        Image lawnMower = new Image(".\\sample\\Img_Assets\\lawnmower.png", 107,95,true,true);
        container.setImage(lawnMower);
        container.setFitWidth(107);
        container.setTranslateX(-12);
        container.setTranslateY(-5);
        reference.gameGrid.add(container,0,row);

        lawnMowerActivate();
        checkGameOver();
    }

    public void lawnMowerActivate(){
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                javafx.application.Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
//                        if(!reference.stopFlag){
//                        }
                        reference.zombieGrid.get(row).forEach(zombie -> {
                            if(container.getBoundsInParent().intersects(zombie.zombieSpawner.getBoundsInParent())){
                                timer.cancel();
                                timer.purge();

                                TranslateTransition lawnMowerMove = new TranslateTransition();
                                lawnMowerMove.setDuration(Duration.seconds(2));
                                lawnMowerMove.setNode(container);
                                lawnMowerMove.setToX(800);
                                lawnMowerMove.setCycleCount(1);

                                ParallelTransition lawnAnim = new ParallelTransition(lawnMowerMove);
                                lawnAnim.play();

                                reference.zombieGrid.get(row).forEach(killZombie -> {
                                    killZombie.health=0;
                                });
                                return;
                            }
                        });
                    }
                });
            }
        }, 0, 200);
    }
    public void checkGameOver(){
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                javafx.application.Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
//                        if(!reference.stopFlag){
//
//                        }
                        reference.zombieGrid.get(row).forEach(zombie -> {
                            if(rect.getBoundsInParent().intersects(zombie.zombieSpawner.getBoundsInParent())){
                                timer.cancel();
                                timer.purge();
                                reference.stopFlag=true;
                                reference.gameOverMenu.setVisible(true);
                                return;
                            }
                        });
                    }
                });
            }
        }, 0, 200);
    }
}
