package sample;

import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

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