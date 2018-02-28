package sample;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Server{

    private ServerSocket serverSocket ;
    int portNumber;

    private Socket playerOne;
    private Socket playerTwo;

    int playersReady;


    public ObjectOutputStream outOne;
    private ObjectOutputStream outTwo ;
    public Server(){
        playersReady=0;

        portNumber=12345;
        try{
            serverSocket = new ServerSocket(portNumber);

            playerOne = serverSocket.accept();
            new ServerRead(playerOne,"one", this);
            outOne = new ObjectOutputStream(playerOne.getOutputStream());
            outOne.writeObject("1");
            System.out.println("First Player Connected");


            playerTwo = serverSocket.accept();
            new ServerRead(playerTwo,"two", this);
            outTwo =  new ObjectOutputStream(playerTwo.getOutputStream());
            outTwo.writeObject("2");
            System.out.println("Second Player Connected");

        } catch (UnknownHostException e) {
            System.err.println("Host Unknown" );
        }catch (IOException ioException) {
            ioException.printStackTrace();
        }

}


    public void write(String code) throws IOException{

        String player = code.substring(0,3);
        if (code.substring(3).equals("ready")){
            playersReady++;
            if (playersReady%2==0){
                startGame();
            }
            return;
        }
        if (playersReady%2!=0)return;
        if (player.equals("one")){
            outTwo.writeObject(code.substring(3));
        }
        else{
            outOne.writeObject(code.substring(3));
        }
    }

    void startGame() throws IOException{
        outOne.writeObject("start");
        outTwo.writeObject("start");
    }

    void writeGameInfo(GameInfo gameInfo) throws IOException{
        if (gameInfo.from.equals("one")){
            outTwo.writeObject(gameInfo);
        }
        else{
            outOne.writeObject(gameInfo);
        }
    }

    public static void main(String[] args){
        new Server();
    }



}
