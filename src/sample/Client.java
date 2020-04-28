package sample;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client implements Runnable{
    int portNumber = 12345;
    Thread t;
    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    Game game;
    Character toControl;
    String address;


    public Client(Game game, String address){

       this.game =game;
        t = new Thread(this,"threadC");
        t.start();
        this.address = address;
    }

    @Override
    public void run() {
        while (true) {
            try {
                clientSocket = new Socket(address, portNumber);

                out = new ObjectOutputStream(clientSocket.getOutputStream());
                in = new ObjectInputStream((clientSocket.getInputStream()));
                int val = Integer.parseInt((String) in.readObject());
                game.side = val;

                if (val == 1) {
                    System.out.println("player");
                    toControl = game.opponent;
                    game.setPlayer(game.player, toControl);
                } else {
                    System.out.println("opponent");
                    toControl = game.player;
                    game.setPlayer(game.opponent, toControl);
                }

                String code;
                while (true) {
                    Object o =  in.readObject();
                    if (o instanceof String) {
                        code = (String ) o;
                        if (game.gameState==5){
                            display(code);
                        }
                        else if (game.gameState != 3) {
                            if (code.equals("start")) {game.setGameState(3);
                            game.stateThreeTime=System.nanoTime();}
                            continue;
                        }

                    }
                    else if (o instanceof GameInfo){
                        GameInfo gameInfo = (GameInfo)o;
                        updateGameInfo(gameInfo);

                    }
                }

            } catch (UnknownHostException e) {
                System.err.println("Host Unknown");
            } catch (IOException e) {
                System.err.println("I/O");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void display(String move){
        if (toControl.controls.contains(move) && !toControl.input.contains(move)){
            toControl.input.add(move);
        }
        else if (toControl.controls.contains(move.substring(0,move.length()-1))){
            toControl.input.remove(move.substring(0,move.length()-1));
            if (move.substring(0,move.length()-1).equals(toControl.shoot)){
                toControl.shootable = true;
            }
        }
        else  if (move.substring(move.length()-1).equals("W")){
            double x = Double.parseDouble(move.substring(0,move.length()-1));
            toControl.positionX=x;
        }
        else  if (move.substring(move.length()-1).equals("E")){
            double y = Double.parseDouble(move.substring(0,move.length()-1));
            toControl.positionY=y;
        }
    }

    public void sendCode(String self){
        try {
            out.writeObject(self);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendGameInfo(GameInfo gameInfo){
        try {
            GameInfo clonedInfo = (GameInfo)gameInfo.clone();
            out.writeObject(clonedInfo);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    void updateGameInfo(GameInfo gameInfo){
        game.opponentName = gameInfo.opponentName;
        toControl.positionX=gameInfo.posX;
        toControl.positionY=gameInfo.posY;
        toControl.direction=gameInfo.direction;
        game.toControl.healthBar.setHealth(gameInfo.OpponentHealth);
        if (!game.against.shield.hasShield && gameInfo.playerHasShield){
            game.against.createShield();
        }
    }

}
