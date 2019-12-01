package sample;

import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
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

import static sample.LoadGame.loadGameData;
import static sample.ChooseLevel.userSelectedLevel;

public class Game implements Initializable {
    @FXML
    private AnchorPane gameWonMenu;

    @FXML
    private ImageView BG_level2_top;

    @FXML
    private ImageView BG_level2_bottom;

    @FXML
    private ImageView BG_level1_bottom;

    @FXML
    private ImageView BG_level1_top;

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
    ArrayList<Plant> plantList = new ArrayList<Plant>();
    ArrayList<LawnMower> lawnMowersList = new ArrayList<>();
    int frequency = 5000;
    int sunTokenGen = 0;
    int bonusTokenCheck = 1;
    int currency = 0;
    String selected = "";
    int level = 1;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        serializedObjectReceived
        if(!loadGameData.equals("")){
            ObjectInputStream in = null;
            gameObject test = null;
            try{
                String filePath = ".\\src\\sample\\bin\\"+loadGameData;
                System.out.println(filePath);
                in = new ObjectInputStream(new FileInputStream(filePath));
                test = (gameObject) in.readObject();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            currency = test.currency;
            frequency = test.frequency;
            timerSeconds = test.timerSeconds;
            level = test.level;

            for (int i = 0; i < 5; i++) {
                zombieGrid.add(new ArrayList<Zombie>());
            }

            switch (level){
                case 2:
                case 3:
                    for (int i = 1; i < 4; i++) {
                        LawnMower temp = new LawnMower(this,i,test.lawnMowers.get(i-1).isUsed);
                        lawnMowersList.add(temp);
                    }
                    BG_level1_bottom.setVisible(false);
                    BG_level1_top.setVisible(false);
                    doublePeaShooter.setVisible(false);
                    cherryBomb.setVisible(false);
                    break;
                case 4:
                case 5:
                    for (int i = 0; i < 5; i++) {
                        LawnMower temp = new LawnMower(this,i,test.lawnMowers.get(i).isUsed);
                        lawnMowersList.add(temp);
                    }
                    BG_level1_bottom.setVisible(false);
                    BG_level1_top.setVisible(false);
                    BG_level2_bottom.setVisible(false);
                    BG_level2_top.setVisible(false);
                    break;
                default:
                    for (int i = 2; i < 3; i++) {
                        LawnMower temp = new LawnMower(this,i,test.lawnMowers.get(i-2).isUsed);
                        lawnMowersList.add(temp);
                    }
                    BG_level2_bottom.setVisible(false);
                    BG_level2_top.setVisible(false);
                    sunFlowerPlant.setVisible(false);
                    doublePeaShooter.setVisible(false);
                    potatoBarrier.setVisible(false);
                    cherryBomb.setVisible(false);
                    break;
            }

//            path blocking and plant buttons and timer
            switch (level){
                case 2:
                    BG_level1_bottom.setVisible(false);
                    BG_level1_top.setVisible(false);
                    doublePeaShooter.setVisible(false);
                    cherryBomb.setVisible(false);
                    potatoBarrier.setVisible(false);
                    break;
                case 3:
                    BG_level1_bottom.setVisible(false);
                    BG_level1_top.setVisible(false);
                    doublePeaShooter.setVisible(false);
                    cherryBomb.setVisible(false);
                    break;
                case 4:
                    BG_level1_bottom.setVisible(false);
                    BG_level1_top.setVisible(false);
                    BG_level2_bottom.setVisible(false);
                    BG_level2_top.setVisible(false);
                    doublePeaShooter.setVisible(false);
                    break;
                case 5:
                    BG_level1_bottom.setVisible(false);
                    BG_level1_top.setVisible(false);
                    BG_level2_bottom.setVisible(false);
                    BG_level2_top.setVisible(false);
                    break;
                default:
                    BG_level2_bottom.setVisible(false);
                    BG_level2_top.setVisible(false);
                    sunFlowerPlant.setVisible(false);
                    doublePeaShooter.setVisible(false);
                    potatoBarrier.setVisible(false);
                    cherryBomb.setVisible(false);
                    break;
            }

            test.plantList.forEach(plant -> {
                if(plant.getClass()==PeaShooter.class){
                    Image img = new Image(".\\sample\\Img_Assets\\plants\\peaShooter_instance.png",60,60,true,true);
                    ImageView source = new ImageView();
                    source.setImage(img);
                    source.setFitHeight(65);

                    gameGrid.add(source,plant.getY(),plant.getX());
                    Plant temp = new PeaShooter(plant.getX(),plant.getY(), source, pea_2, zombieGrid.get(plant.getX()), this);
                    temp.setHealth(plant.getHealth());
                    plantList.add(temp);
                    Timer timer = new Timer();
                    timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            javafx.application.Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    if(!stopFlag) {
                                        temp.specialMove(timer);
                                    }
                                }
                            });
                        }
                    }, 0, 2000);
                }
                if(plant.getClass()==Sunflower.class){
                    Image img = new Image(".\\sample\\Img_Assets\\plants\\sunflower_instance.png",60,60,true,true);
                    ImageView source = new ImageView();
                    source.setImage(img);
                    source.setFitHeight(65);

                    gameGrid.add(source,plant.getY(),plant.getX());
                    Plant temp = new Sunflower(plant.getX(),plant.getY(), source, pea_2, zombieGrid.get(plant.getX()), this);
                    temp.setHealth(plant.getHealth());
                    plantList.add(temp);
                    Timer timer = new Timer();
                    timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            javafx.application.Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    if(!stopFlag) {
                                        temp.specialMove(timer);
                                    }
                                }
                            });
                        }
                    }, 2000, 6000);
                }
                if(plant.getClass()==PotatoBarrier.class){
                    Image img = new Image(".\\sample\\Img_Assets\\plants\\potatoInstance.png",60,60,true,true);
                    ImageView source = new ImageView();
                    source.setImage(img);
                    source.setFitHeight(65);

                    gameGrid.add(source,plant.getY(),plant.getX());
                    Plant temp = new PotatoBarrier(plant.getX(),plant.getY(), source, pea_2, zombieGrid.get(plant.getX()), this);
                    temp.setHealth(plant.getHealth());
                    plantList.add(temp);
                }
                if(plant.getClass()==CherryBomb.class){
                    Image img = new Image(".\\sample\\Img_Assets\\plants\\cherryInstance.png",70,70,true,true);
                    ImageView source = new ImageView();
                    source.setImage(img);
                    source.setFitHeight(65);

                    gameGrid.add(source,plant.getY(),plant.getX());
                    Plant temp = new CherryBomb(plant.getX(),plant.getY(), source, pea_2, zombieGrid.get(plant.getX()), this);
                    temp.setHealth(plant.getHealth());
                    plantList.add(temp);
                    Timer timer = new Timer();
                    temp.specialMove(timer);
                }
                if(plant.getClass()==DoublePeaShooter.class){
                    Image img = new Image(".\\sample\\Img_Assets\\plants\\doublePeaShooter.png",60,60,true,true);
                    ImageView source = new ImageView();
                    source.setImage(img);
                    source.setFitHeight(65);

                    gameGrid.add(source,plant.getY(),plant.getX());
                    Plant temp = new DoublePeaShooter(plant.getX(),plant.getY(), source, pea_2, zombieGrid.get(plant.getX()), this);
                    temp.setHealth(plant.getHealth());
                    plantList.add(temp);
                    Timer timer = new Timer();
                    timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            javafx.application.Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    if(!stopFlag) {
                                        temp.specialMove(timer);
                                    }
                                }
                            });
                        }
                    }, 2000, 6000);
                }
            });

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
                                if(sunTokenGen == 1){
                                    generateSunToken(seed);
                                    bonusTokenCheck += 1;
                                }
                                if(bonusTokenCheck%6==0){
                                    if (seed.nextInt(2) == 0) {
                                        generateSlowToken(seed);
                                    } else {
                                        generateBonusToken(seed);
                                    }
                                }
                                frequency-=2;
//                        Change sun token Frequency
                                sunTokenGen = (sunTokenGen+1)%4;
                            }
                            if(timerSeconds==0){
//                           LEVEL WIN
                                stopFlag=true;
                                timer.cancel();
                                timer.purge();
                                try {
                                    levelClear();
                                } catch (IOException e) {
                                    e.printStackTrace();e.printStackTrace();
                                }
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
        else{
            try{
                String filePath = new File("").getAbsolutePath().concat("\\src\\sample\\level.txt");
                File file = new File(filePath);
                BufferedReader br = new BufferedReader(new FileReader(file));
                level = Integer.parseInt(br.readLine());

            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if(userSelectedLevel>0) level = userSelectedLevel;
            for (int i = 0; i < 5; i++) {
                zombieGrid.add(new ArrayList<Zombie>());
            }
            switch (level){
                case 2:
                case 3:
                    for (int i = 1; i < 4; i++) {
                        LawnMower temp = new LawnMower(this,i,false);
                        lawnMowersList.add(temp);
                    }
                    BG_level1_bottom.setVisible(false);
                    BG_level1_top.setVisible(false);
                    doublePeaShooter.setVisible(false);
                    cherryBomb.setVisible(false);
                    break;
                case 4:
                case 5:
                    for (int i = 0; i < 5; i++) {
                        LawnMower temp = new LawnMower(this,i,false);
                        lawnMowersList.add(temp);
                    }
                    BG_level1_bottom.setVisible(false);
                    BG_level1_top.setVisible(false);
                    BG_level2_bottom.setVisible(false);
                    BG_level2_top.setVisible(false);
                    break;
                default:
                    for (int i = 2; i < 3; i++) {
                        LawnMower temp = new LawnMower(this,i,false);
                        lawnMowersList.add(temp);
                    }
                    BG_level2_bottom.setVisible(false);
                    BG_level2_top.setVisible(false);
                    sunFlowerPlant.setVisible(false);
                    doublePeaShooter.setVisible(false);
                    potatoBarrier.setVisible(false);
                    cherryBomb.setVisible(false);
                    break;
            }

//        path blocking and plant buttons and timer
            switch (level){
                case 2:
                    frequency=9000;
                    timerSeconds=120;
                    BG_level1_bottom.setVisible(false);
                    BG_level1_top.setVisible(false);
                    doublePeaShooter.setVisible(false);
                    cherryBomb.setVisible(false);
                    potatoBarrier.setVisible(false);
                    break;
                case 3:
                    frequency=8500;
                    timerSeconds=180;
                    BG_level1_bottom.setVisible(false);
                    BG_level1_top.setVisible(false);
                    doublePeaShooter.setVisible(false);
                    cherryBomb.setVisible(false);
                    break;
                case 4:
                    frequency=8000;
                    timerSeconds=240;
                    BG_level1_bottom.setVisible(false);
                    BG_level1_top.setVisible(false);
                    BG_level2_bottom.setVisible(false);
                    BG_level2_top.setVisible(false);
                    doublePeaShooter.setVisible(false);
                    break;
                case 5:
                    frequency=7000;
                    BG_level1_bottom.setVisible(false);
                    BG_level1_top.setVisible(false);
                    BG_level2_bottom.setVisible(false);
                    BG_level2_top.setVisible(false);
                    break;
                default:
                    timerSeconds=60;
                    BG_level2_bottom.setVisible(false);
                    BG_level2_top.setVisible(false);
                    sunFlowerPlant.setVisible(false);
                    doublePeaShooter.setVisible(false);
                    potatoBarrier.setVisible(false);
                    cherryBomb.setVisible(false);
                    break;
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
                                if(sunTokenGen == 1){
                                    generateSunToken(seed);
                                    bonusTokenCheck += 1;
                                }
                                if(bonusTokenCheck%5==0){
                                    if (seed.nextInt(2) == 0) {
                                        generateSlowToken(seed);
                                    } else {
                                        generateBonusToken(seed);
                                    }
                                }
                                frequency-=2;
                                sunTokenGen = (sunTokenGen+1)%4;
                            }
                            if(timerSeconds==0){
//                           LEVEL WIN
                                stopFlag=true;
                                timer.cancel();
                                timer.purge();
                                try {
                                    levelClear();
                                } catch (IOException e) {
                                    e.printStackTrace();e.printStackTrace();
                                }
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
    }

    public void levelClear() throws IOException {
        System.out.println("Tum Jeet Gaye");
        String filePath = new File("").getAbsolutePath().concat("\\src\\sample\\level.txt");
        PrintWriter out = new PrintWriter( new FileWriter(filePath));
        level++;
        out.println(Integer.toString(level));
        out.close();
        stopFlag=true;
        gameWonMenu.setVisible(true);
    }

    public void nextLevel(Event mouseEvent) throws IOException{

        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("Game.fxml"));
        stage.setScene(new Scene(root));
    }

    public void generateSlowToken(Random seed){
        ImageView sunTokenContainer = new ImageView();
        Image sunTokenImg = new Image(".\\sample\\Img_Assets\\pinkSun.png",50,50,true,true);
        sunTokenContainer.setImage(sunTokenImg);
        sunTokenContainer.setFitHeight(50);
        gameGrid.add(sunTokenContainer,seed.nextInt(10),0);
        sunTokenContainer.setTranslateX(seed.nextInt(20));
        sunTokenContainer.setTranslateY(-60 + seed.nextInt(5));

        ParallelTransition anim = new ParallelTransition();

        sunTokenContainer.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                frequency += 50;
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

    public void generateBonusToken(Random seed){
        ImageView sunTokenContainer = new ImageView();
        Image sunTokenImg = new Image(".\\sample\\Img_Assets\\greenSun.png",50,50,true,true);
        sunTokenContainer.setImage(sunTokenImg);
        sunTokenContainer.setFitHeight(50);
        gameGrid.add(sunTokenContainer,seed.nextInt(10),0);
        sunTokenContainer.setTranslateX(seed.nextInt(20));
        sunTokenContainer.setTranslateY(-60 + seed.nextInt(5));

        ParallelTransition anim = new ParallelTransition();

        sunTokenContainer.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                currency += 75;
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
                currency += 25;
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
                            switch (level){
                                case 2:
                                case 3:
                                    int temp = seed.nextInt(3) + 1;
                                    Zombie genZombie2 = new Zombie(gameGrid, ref, temp);
                                    zombieGrid.get(temp).add(genZombie2);
                                    break;
                                case 4:
                                case 5:
                                    int temp2 = seed.nextInt(5);
                                    Zombie genZombie3 = new Zombie(gameGrid, ref, temp2);
                                    zombieGrid.get(temp2).add(genZombie3);
                                    break;
                                default:
                                    Zombie genZombie = new Zombie(gameGrid, ref, 2);
                                    zombieGrid.get(2).add(genZombie);
                                    break;
                            }

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

        ImageView source = new ImageView();
        source.setImage(img);
        source.setFitHeight(65);
        gameGrid.add(source,y,x);

        if(selected.equals("sunFlowerPlant")){
            Plant temp = new Sunflower(x,y, source, pea_2, zombieGrid.get(x), this);
            plantList.add(temp);
            currency -= 50;
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    javafx.application.Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if(!stopFlag){
                                temp.specialMove(timer);
                            }
                        }
                    });
                }
            }, 2000, 6000);
        }

        if(selected.equals("peaShooterPlant")){
            Plant temp = new PeaShooter(x,y, source, pea_2, zombieGrid.get(x), this);
            plantList.add(temp);
            currency -= 100;
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    javafx.application.Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if(!stopFlag) {
                                temp.specialMove(timer);
                            }
                        }
                    });
                }
            }, 0, 2000);
        }

        if(selected.equals("potatoBarrier")){
            Plant temp = new PotatoBarrier(x,y, source, pea_2, zombieGrid.get(x), this);
            plantList.add(temp);
            currency -= 50;
        }

        if(selected.equals("doublePeaShooter")){
            Plant temp = new DoublePeaShooter(x,y, source, pea_2, zombieGrid.get(x), this);
            plantList.add(temp);
            currency -= 200;
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    javafx.application.Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if(!stopFlag){
                                temp.specialMove(timer);
                            }
                        }
                    });
                }
            }, 2000, 6000);
        }

        if(selected.equals("cherryBomb")){
            source.setFitHeight(75);
            Plant temp = new CherryBomb(x,y, source, pea_2, zombieGrid.get(x), this);
            plantList.add(temp);
            currency -= 150;
            Timer timer = new Timer();
            temp.specialMove(timer);
        }
        sunTokenLabel.setText(String.valueOf(currency));
    }

    public void removeZombie(Zombie temp) {
        zombieGrid.get(temp.row).remove(temp);
    }

    public void saveGame(Event mouseEvent) throws IOException {
        zombieGrid.forEach(array -> {
            array.forEach(zombie -> {
                Bounds temp = zombie.zombieSpawner.getBoundsInParent();
                zombie.minX = temp.getMinX();
                zombie.minY = temp.getMinY();
                zombie.height = temp.getHeight();
                zombie.width = temp.getWidth();
            });
        });
        gameObject saveState = new gameObject(zombieGrid,plantList,lawnMowersList,level,timerSeconds,frequency,currency);
        saveState.serialize();

        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        stage.setScene(new Scene(root));
    }

}





