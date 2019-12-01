package sample;

import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

abstract class Plant implements Serializable{
    private int health;
    private int x;
    private int y;
    private double absX;
    private double absY;
    transient private ImageView container;
    transient private Circle copy;
    transient GridPane parent;
    private ArrayList<Zombie> zombieInRowArray;
    transient private Game reference;

    transient private ParallelTransition anim;
    transient private Timer timer;
    transient private Timer mainTimer;


    Plant(int _x, int _y, ImageView _container, Circle _copy, ArrayList<Zombie> zombie, Game _reference) {
        setX(_x);
        setY(_y);
        setHealth(20);
        setContainer(_container);
        setCopy(_copy);
        setParent((GridPane) getContainer().getParent());
        setZombieInRowArray(zombie);
        setReference(_reference);
        setAbsX(getContainer().getBoundsInParent().getMinX());
        setAbsY(getContainer().getBoundsInParent().getMinY());
        setAnim(new ParallelTransition());
        setTimer(new Timer());
        setMainTimer(new Timer());
        getHitByZombie();
    }
    abstract public void specialMove(Timer _timer);
    public void getHitByZombie() {
        Timer zombieTime = new Timer();

        zombieTime.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                javafx.application.Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if(!getReference().stopFlag){
                            getZombieInRowArray().forEach(zombie -> {
                                if(getContainer().getBoundsInParent().intersects(zombie.zombieSpawner.getBoundsInParent())){
                                    zombie.movementFlag=false;
                                    setHealth(getHealth() - 1);
                                    if(getHealth() == 0){
                                        getAnim().stop();
                                        zombie.movementFlag=true;
                                        zombieTime.cancel();
                                        zombieTime.purge();
                                        getMainTimer().cancel();
                                        getMainTimer().purge();
                                        getTimer().cancel();
                                        getTimer().purge();
                                        getParent().getChildren().remove(getContainer());
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

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double getAbsX() {
        return absX;
    }

    public void setAbsX(double absX) {
        this.absX = absX;
    }

    public double getAbsY() {
        return absY;
    }

    public void setAbsY(double absY) {
        this.absY = absY;
    }

    public ImageView getContainer() {
        return container;
    }

    public void setContainer(ImageView container) {
        this.container = container;
    }

    public Circle getCopy() {
        return copy;
    }

    public void setCopy(Circle copy) {
        this.copy = copy;
    }

    public GridPane getParent() {
        return parent;
    }

    public void setParent(GridPane parent) {
        this.parent = parent;
    }

    public ArrayList<Zombie> getZombieInRowArray() {
        return zombieInRowArray;
    }

    public void setZombieInRowArray(ArrayList<Zombie> zombieInRowArray) {
        this.zombieInRowArray = zombieInRowArray;
    }

    public Game getReference() {
        return reference;
    }

    public void setReference(Game reference) {
        this.reference = reference;
    }

    public ParallelTransition getAnim() {
        return anim;
    }

    public void setAnim(ParallelTransition anim) {
        this.anim = anim;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public Timer getMainTimer() {
        return mainTimer;
    }

    public void setMainTimer(Timer mainTimer) {
        this.mainTimer = mainTimer;
    }
}

class Sunflower extends Plant implements Serializable{

    Sunflower(int _x, int _y, ImageView _container, Circle _copy, ArrayList<Zombie> zombie, Game _reference) {
        super(_x, _y, _container, _copy, zombie, _reference);
    }

    @Override
    public void specialMove(Timer _timer) {
        genSunAnim(_timer);
    }
    //    sunflower
    public void genSunAnim(Timer _timer){
        setMainTimer(_timer);
        ImageView sunTokenContainer = new ImageView();
        Image sunTokenImg = new Image(".\\sample\\Img_Assets\\sunToken.png",50,50,true,true);
        sunTokenContainer.setImage(sunTokenImg);
        sunTokenContainer.setFitHeight(50);
        getParent().add(sunTokenContainer, getY(), getX());

        sunTokenContainer.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                getReference().currency += 50;
                getReference().sunTokenLabel.setText(String.valueOf(getReference().currency));
                getAnim().stop();
                getReference().gameGrid.getChildren().remove(sunTokenContainer);
            }
        });
        Random seed = new Random();
        TranslateTransition sunTokenTransition= new TranslateTransition();
        sunTokenTransition.setDuration(Duration.millis(3500));
        sunTokenTransition.setNode(sunTokenContainer);
        sunTokenTransition.setToX(seed.nextInt(2) == 0 ? seed.nextInt(30): -1*seed.nextInt(30));
        sunTokenTransition.setToY(30);
        sunTokenTransition.setCycleCount(1);

        getAnim().getChildren().add(sunTokenTransition);
        getAnim().play();
    }
}

class PeaShooter extends Plant implements Serializable{

    PeaShooter(int _x, int _y, ImageView _container, Circle _copy, ArrayList<Zombie> zombie, Game _reference) {
        super(_x, _y, _container, _copy, zombie, _reference);
    }

    @Override
    public void specialMove(Timer _timer){
        setMainTimer(_timer);

        Circle temp =  new Circle(getAbsX(), getAbsY(),10, getCopy().getFill());
        temp.setStroke(getCopy().getStroke());
        temp.setStrokeWidth(getCopy().getStrokeWidth());
        temp.setStrokeType(getCopy().getStrokeType());
        getParent().add(temp, getY(), getX());
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

                getReference().gameGrid.getChildren().remove(temp);
                getAnim().getChildren().remove(peaTransition);
            }
        });

        getAnim().getChildren().add(peaTransition);
        getAnim().play();

        setTimer(new Timer());
        getTimer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                javafx.application.Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if(!getReference().stopFlag){
                            getZombieInRowArray().forEach(zombie -> {
                                if(temp.getBoundsInParent().intersects(zombie.zombieSpawner.getBoundsInParent())){
                                    getAnim().stop();
                                    getTimer().cancel();
                                    getTimer().purge();
                                    temp.setRadius(0);
                                    temp.setTranslateZ(10);
                                    getReference().gameGrid.getChildren().remove(temp);
                                    zombie.health--;
                                }
                            });
                        }
                    }
                });
            }
        }, 0, 150);
    }

}

class DoublePeaShooter extends Plant implements Serializable{

    DoublePeaShooter(int _x, int _y, ImageView _container, Circle _copy, ArrayList<Zombie> zombie, Game _reference) {
        super(_x, _y, _container, _copy, zombie, _reference);
    }

    @Override
    public void specialMove(Timer _timer) {
        doublePeaAnim(_timer);
    }

    //double peashooter
    public void doublePeaAnim(Timer _timer) {
        setMainTimer(_timer);

        Circle temp =  new Circle(getAbsX(), getAbsY(),10, getCopy().getFill());
        temp.setStroke(getCopy().getStroke());
        temp.setStrokeWidth(getCopy().getStrokeWidth());
        temp.setStrokeType(getCopy().getStrokeType());
        getParent().add(temp, getY(), getX());
        temp.setTranslateX(25);
        temp.setTranslateY(-12);
        temp.toBack();

        Circle temp2 =  new Circle(getAbsX(), getAbsY(),10, getCopy().getFill());
        temp2.setStroke(getCopy().getStroke());
        temp2.setStrokeWidth(getCopy().getStrokeWidth());
        temp2.setStrokeType(getCopy().getStrokeType());
        getParent().add(temp2, getY(), getX());
        temp2.setTranslateX(25);
        temp2.setTranslateY(-12);
        temp2.toBack();

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

                getReference().gameGrid.getChildren().remove(temp);
                getAnim().getChildren().remove(peaTransition);
            }
        });

        getAnim().getChildren().add(peaTransition);
        getAnim().play();

        ParallelTransition anim2 = new ParallelTransition();
        TranslateTransition peaTransition2 = new TranslateTransition();
        peaTransition2.setDuration(Duration.millis(2200));
        peaTransition2.setNode(temp2);
        peaTransition2.setToX(700);
        peaTransition2.setCycleCount(1);
        peaTransition2.setDelay(Duration.millis(500));
        peaTransition2.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                temp2.setRadius(0);
                temp2.setTranslateZ(10);

                getReference().gameGrid.getChildren().remove(temp2);
                anim2.getChildren().remove(peaTransition2);
            }
        });

        anim2.getChildren().add(peaTransition2);
        anim2.play();

        setTimer(new Timer());
        getTimer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                javafx.application.Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if(!getReference().stopFlag){
                            getZombieInRowArray().forEach(zombie -> {
                                if(temp.getBoundsInParent().intersects(zombie.zombieSpawner.getBoundsInParent())){
                                    getAnim().stop();
                                    getTimer().cancel();
                                    getTimer().purge();
                                    temp.setRadius(0);
                                    temp.setTranslateZ(10);
                                    getReference().gameGrid.getChildren().remove(temp);
                                    zombie.health--;
                                }
                            });
                        }
                    }
                });
            }
        }, 0, 150);

        Timer timer2 = new Timer();
        timer2.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                javafx.application.Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if(!getReference().stopFlag){
                            getZombieInRowArray().forEach(zombie -> {
                                if(temp2.getBoundsInParent().intersects(zombie.zombieSpawner.getBoundsInParent())){
                                    anim2.stop();
                                    timer2.cancel();
                                    timer2.purge();
                                    temp2.setRadius(0);
                                    temp2.setTranslateZ(10);
                                    getReference().gameGrid.getChildren().remove(temp2);
                                    zombie.health--;
                                }
                            });
                        }
                    }
                });
            }
        }, 0, 150);
    }
}

class CherryBomb extends Plant implements Serializable{

    CherryBomb(int _x, int _y, ImageView _container, Circle _copy, ArrayList<Zombie> zombie, Game _reference) {
        super(_x, _y, _container, _copy, zombie, _reference);
    }

    @Override
    public void specialMove(Timer _timer) {
        cherryBomb(_timer);
    }
    public void cherryBomb(Timer _timer) {
        setMainTimer(_timer);
        Rectangle rec = new Rectangle();
        rec.setHeight(180);
        rec.setWidth(180);
        getParent().add(rec, getY(), getX());
        rec.setX(getContainer().getLayoutX());
        rec.setY(getContainer().getLayoutY());
        rec.setTranslateX(-55);
        rec.setVisible(false);

        getReference().zombieGrid.forEach(array -> {
            array.forEach(zombie -> {
                if(rec.getBoundsInLocal().intersects(zombie.zombieSpawner.getBoundsInParent())){
                    zombie.health = 0;
                }
            });
        });

        getParent().getChildren().remove(rec);
        getParent().getChildren().remove(getContainer());
    }
}

class PotatoBarrier extends Plant implements Serializable{

    PotatoBarrier(int _x, int _y, ImageView _container, Circle _copy, ArrayList<Zombie> zombie, Game _reference) {
        super(_x, _y, _container, _copy, zombie, _reference);
        setHealth(40);
    }

    @Override
    public void specialMove(Timer _timer) {

    }
}

class Zombie implements Serializable {
    public int health;
    transient public ImageView zombieSpawner;
    transient public GridPane gameGrid;
    transient Game reference;
    int row;
    Boolean movementFlag;
    double minX,minY,height,width;

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

class LawnMower implements Serializable{
    transient ImageView container;
    transient Game reference;
    int row;
    transient Rectangle rect;
    Boolean isUsed = false;

    LawnMower(Game _reference, int _row, boolean flag){
        reference = _reference;
        row = _row;

        rect = new Rectangle();
        rect.setHeight(75);
        rect.setWidth(50);
        reference.gameGrid.add(rect,0,row);
        rect.setVisible(false);

        if(!flag){
            container = new ImageView();
            Image lawnMower = new Image(".\\sample\\Img_Assets\\lawnmower.png", 107,95,true,true);
            container.setImage(lawnMower);
            container.setFitWidth(107);
            container.setTranslateX(-12);
            container.setTranslateY(-5);
            reference.gameGrid.add(container,0,row);
            lawnMowerActivate();
        }

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
                        if(!reference.stopFlag){
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

                                    isUsed = true;
                                    return;
                                }
                            });
                        }
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
                        if(!reference.stopFlag){
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
                    }
                });
            }
        }, 0, 200);
    }

}

class gameObject implements Serializable{
    ArrayList<ArrayList<Zombie>> zombieGrid;
    ArrayList<Plant> plantList;
    ArrayList<LawnMower> lawnMowers;
    int level;
    int timerSeconds;
    int frequency;
    int currency;

    gameObject(ArrayList<ArrayList<Zombie>> _zombieGrid, ArrayList<Plant> _plantList, ArrayList<LawnMower> _lawnmower, int _level, int _timerSeconds, int _frequency, int _currency){
        zombieGrid = _zombieGrid;
        plantList = _plantList;
        lawnMowers = _lawnmower;
        level = _level;
        timerSeconds = _timerSeconds;
        frequency = _frequency;
        currency = _currency;
    }

    public void serialize() throws IOException {

        ObjectOutputStream out = null;
        try{
            String filePath = new File("").getAbsolutePath().concat("\\src\\sample\\bin");
            File folder = new File(filePath);
            File[] listOfFiles = folder.listFiles();
            int i = listOfFiles.length%3 + 1;
            filePath = ".\\src\\sample\\bin\\Save_Game_" + Integer.toString(i) + ".bin";
            out = new ObjectOutputStream(new FileOutputStream(filePath));
            out.writeObject(this);
        } finally {
            out.close();
        }
    }
}