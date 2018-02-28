package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import javafx.geometry.Point2D;



public class Game extends Application {

    final static int WIN_HEIGHT = 500;
    final static int WIN_WIDTH = 1000;
    final static double CHARACTER_SCALE = 1.5;

    double gameStartTime;

    boolean playIntro = true;

    long stateThreeTime;

    GameInfo gameInfo;
    String username;
    String opponentName;

    public int gameState = 0;
    public int side;

    boolean playWin = false;
    boolean playLose = false;
    boolean gameEnd = false;
    boolean getKeyboardInput = false;
    int playOnlyOnce = 0;


    String selectSoundPath = "src/sounds/select.wav";
    String scoreSoundPath = "src/sounds/score.mp3";
    String winSoundPath = "src/sounds/win.wav";
    String loseSoundPath = "src/sounds/lose.wav";

    Sound score;

    Character player = new Character(1);
    PowerUp shieldItem;
    PowerUp healthItem;

    Client client;
    static Sprite sideWallLeft;
    static Sprite sideWallRight;

    Scene theScene;
    Character opponent = new Character(-1);
    Character toControl;
    Character against;

    static Sprite[] floors;
    static Sprite background;
    Sprite win;
    Sprite lose;

    Sprite waiting;
    Sprite startButton;
    Sprite ready;
    Sprite intro;
    Sprite rematch;

    TextField name;
    Button nameButton;

    static GraphicsContext gc;

    @Override
    public void start(Stage theStage) throws Exception {
        gameInfo = new GameInfo();
        score = new Sound(scoreSoundPath,540);

        opponent.setOpponent(player);
        player.setOpponent(opponent);

        client = new Client(this);

        Group root = new Group();
        theScene = new Scene(root);
        theStage.setScene(theScene);
        theStage.setTitle("Mega");
        Canvas canvas = new Canvas(WIN_WIDTH, WIN_HEIGHT);

        name = new TextField("No Name");
        nameButton = new Button("set name");

        name.setLayoutX(500 - 85);
        name.setLayoutY(300);

        nameButton.setLayoutX(500 - 40);
        nameButton.setLayoutY(400);

        root.getChildren().addAll(canvas, nameButton, name);
        initiateMouseInput();

        nameButton.setOnAction(e -> {
            username = name.getText();
            System.out.println(username);
            root.getChildren().removeAll(name, nameButton);
            gameState=1;
        });


        gc = canvas.getGraphicsContext2D();

        loadGameObjects();


        final Long[] lastNanoTime =  new Long[1];
        lastNanoTime[0] = System.nanoTime();

        final long startNanoTime = System.nanoTime();

        theStage.setResizable(false);


        new AnimationTimer() {
            public void handle(long currentNanoTime){

                gc.clearRect(0,0, WIN_WIDTH, WIN_HEIGHT);
                background.render(gc);

                switch (gameState) {
                    case 1:
                        startButton.render(gc);
                        break;
                    case 2:
                        ready.render(gc);
                        break;
                    case 3:
                        if (!playIntro){
                            gameState=5;
                            break;
                        }
                        intro = new Sprite();
                        intro .setPosition(0,0);
                        intro.setImage("start/intro.gif");
                        intro.width = 1000;
                        intro.height=500;
                        gameState=4;
                        break;
                    case 4:
                        playIntro=false;
                        intro.render(gc,1000,500);
                        if ((currentNanoTime-stateThreeTime)/7>1070000000){
                            gameState=5;
                        }
                        break;
                    case 5:
                        if(root.getChildren().contains(name)) {
                            root.getChildren().removeAll(name, nameButton);
                        }
                        startGame(currentNanoTime,startNanoTime,lastNanoTime);
                }
            }
        }.start();

        theStage.show();
    }

    void startGame(long currentNanoTime, long startNanoTime, Long[] lastNanoTime){

        if (Math.floor((currentNanoTime-shieldItem.takenTime)/1000000000)==15){
            shieldItem = new PowerUp("shield.png",120,100,40,40);
        }
        if (Math.floor((currentNanoTime-healthItem.takenTime)/1000000000)==15){
            healthItem = new PowerUp("health.png", 620,100,40,40);
        }
        double timeSinceStart = (currentNanoTime - startNanoTime)/1000000000.0;
        double elapsedTimePerFrame = (currentNanoTime - lastNanoTime[0])/ 1000000000.0;
        lastNanoTime[0]  = currentNanoTime;

        player.setGravity(0.3, floors);
        opponent.setGravity(0.3, floors);

        if (player.healthBar.getHealth()<=0 || opponent.healthBar.getHealth()<=0){
            player.input.clear();
            opponent.input.clear();
            if (side==1){
                if(player.healthBar.getHealth()<=0) {
                    lose.render(gc);
                    playLose = true;
                    player.dieAnim();
                }
                else if(opponent.healthBar.getHealth()<=0) {
                    win.render(gc);
                    playWin = true;
                    opponent.dieAnim();
                }
            }
            else if (side==2){
                if(opponent.healthBar.getHealth()<=0) {
                    lose.render(gc);
                    playLose = true;
                    opponent.dieAnim();

                }
                else if(player.healthBar.getHealth()<=0) {
                    win.render(gc);
                    player.dieAnim();
                    playWin = true;
                }

            }
            if(playWin && playOnlyOnce == 0) {
                score.stop();
                new Sound(winSoundPath, 3);
                playOnlyOnce++;
            } else if(playLose && playOnlyOnce == 0) {
                score.stop();
                new Sound(loseSoundPath, 3);
                playOnlyOnce++;
            }
            gameEnd=true;

        }
        player.handleInput(timeSinceStart);
        opponent.handleInput(timeSinceStart);

        generateGameInfo();
        client.sendGameInfo(gameInfo);

        player.detectVerticalCollisionsWith(floors);
        opponent.detectVerticalCollisionsWith(floors);

        opponent.update(elapsedTimePerFrame);
        player.update(elapsedTimePerFrame);

        opponent.renderOpponent(gc);
        player.render(gc);


        player.healthBar.render();
        opponent.healthBar.render();
        for(Sprite floor: floors) {
            floor.render(gc);
        }

        shieldItem.display();
        if (shieldItem.intersects(player)){
            player.createShield();
        }
        if (shieldItem.intersects(opponent)){
            opponent.createShield();
        }

        healthItem.display();
        if (healthItem.intersects(player)){
            player.increaseHealth(30);
        }
        if (healthItem.intersects(opponent)){
            opponent.increaseHealth(30);
        }

        gc.setFont(Font.font("Courier New", FontWeight.EXTRA_BOLD, 18));
        if(side == 1) {
            gc.setFill(new Color(0, 0.4, 1, 1));
            gc.fillText(username, player.positionX, player.positionY - 5);
            gc.setFill(new Color(0.8, 0.2, 0, 1));
            gc.fillText(opponentName, opponent.positionX, opponent.positionY - 5);
        } else if(side == 2) {
            gc.setFill(new Color(0, 0.4, 1, 1));
            gc.fillText(opponentName, player.positionX, player.positionY - 5);
            gc.setFill(new Color(0.8, 0.2, 0, 1));
            gc.fillText(username, opponent.positionX, opponent.positionY - 5);
        }

        if (player.healthBar.getHealth()<=0 || opponent.healthBar.getHealth()<=0){
            rematch.render(gc);
        }


    }

    public void setPlayer(Character character,Character opp){
        toControl = character;
        against = opp;
    }

    void initiateKeyBoardInput(){


        theScene.setOnKeyPressed(e -> {

            String code = e.getCode().toString();
            if (code.equals("P")) {
                toControl.createShield();
            }
            if (toControl.controls.contains(code)){
                if (!toControl.input.contains(code)){
                    toControl.input.add(code);
                }
                client.sendCode(code);
            }
        });

        theScene.setOnKeyReleased(e -> {

            String code = e.getCode().toString();
            toControl.input.remove(code);
            if (code.equals(toControl.shoot)){toControl.shootable=true;}
            client.sendCode(code+"R");
        });
    }

    void initiateMouseInput() {
        theScene.setOnMouseClicked(event -> {
            Point2D p = new Point2D(event.getSceneX(), event.getSceneY());
            if ((gameState==1 || gameEnd) && startButton.contains(p)){
                new Sound(selectSoundPath, 3);
                refreshGameObjects();
                initiateKeyBoardInput();
                gameState = 2;
                client.sendCode("ready");
                getKeyboardInput = true;
            }
        });

    }

    public void setGameState(int state){
        gameStartTime = System.nanoTime();
        gameState = state;
    }

    public static void main(String[] args) {
        launch(args);
    }

    void generateGameInfo(){
        gameInfo.opponentName = username;
        gameInfo.setPosX(toControl.positionX);
        gameInfo.setPosY(toControl.positionY);
        gameInfo.setDirection(toControl.direction);
        gameInfo.setOpponentHealth(against.healthBar.health);
        gameInfo.playerHasShield=toControl.shield.hasShield;
    }

    @Override
    public void stop(){
        System.exit(0);
    }

    void loadGameObjects(){

        sideWallLeft = new Sprite();
        sideWallRight = new Sprite();

        sideWallRight.positionX = WIN_WIDTH -8;
        sideWallRight.positionY = 0;
        sideWallRight.width = 2;
        sideWallRight.height = WIN_HEIGHT + 5;

        sideWallLeft.positionX = 5;
        sideWallLeft.positionY = 0;
        sideWallLeft.width = 2;
        sideWallLeft.height = WIN_HEIGHT;

        floors = new Sprite[5];

        floors[0] = new Sprite();
        floors[0].setPosition(0 , 450);
        floors[0].width = WIN_WIDTH;
        floors[0].height = 50;

        for(int i = 1; i < floors.length; i++) {
            floors[i] = new Sprite();
            floors[i].setImage("skyFloor.png");
        }

        floors[1].setPosition(300, 300);

        floors[2].setPosition(570, 335);

        floors[3].setPosition(750, 190);

        floors[4].setPosition(150, 180);

        background = new Sprite();
        background.setPosition(0,0);
        background.setImage("background.gif");

        win = new Sprite();
        win.setPosition(320,50);
        win.setImage("start/win.png");

        rematch = new Sprite();
        rematch.setPosition(300,175);
        rematch.setImage("start/rematch.png");

        lose = new Sprite();
        lose.setPosition(320,50);
        lose.setImage("start/lose.png");

        waiting = new Sprite();
        waiting.setPosition(250,200);
        waiting.setImage("start/waiting.png");

        ready = new Sprite();
        ready.setPosition(320,180);
        ready.setImage("start/ready.png");

        startButton = new Sprite();
        startButton.setPosition(300,175);
        startButton.setImage("start/startButton.png");

        refreshGameObjects();



    }

    void refreshGameObjects(){
        gameEnd = false;

        player.refresh();
        opponent.refresh();

        player.setPosition(20, WIN_HEIGHT - floors[0].getBoundary().getHeight()-100);
        opponent.setPosition(WIN_WIDTH - 100, WIN_HEIGHT - floors[0].getBoundary().getHeight()-100);

        player.setInput("UP", "LEFT", "RIGHT", "X", "C");
        opponent.setInput("UP", "LEFT", "RIGHT", "X", "C");

        player.createHealthBar(100,10,20,1);
        opponent.createHealthBar(100,WIN_WIDTH-200,20,-1);

        shieldItem = new PowerUp("shield.png",120,100,40,40);
        healthItem = new PowerUp("health.png", 620,100,40,40);
    }


}

