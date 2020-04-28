package sample;

import java.io.Serializable;

public class GameInfo implements Serializable, Cloneable{
    double posX;
    double posY;
    double OpponentHealth;
    boolean shieldTaken;
    int direction;
    public String from;
    boolean playerHasShield;
    public String opponentName;

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public double getOpponentHealth() {
        return OpponentHealth;
    }

    public void setOpponentHealth(double opponentHealth) {
        OpponentHealth = opponentHealth;
    }


    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public Object clone()throws CloneNotSupportedException{
        return super.clone();
    }
}
