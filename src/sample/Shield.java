package sample;

import javafx.animation.AnimationTimer;

public class Shield implements Runnable {

    Character toShield;
    Sprite sprite;
    Thread thread;
    boolean hasShield=false;
    String path = "src/sounds/shield.wav";

    Shield(Character player){
        toShield = player;
    }



    @Override
    public void run() {

        final long createdTime = System.nanoTime();
        sprite = new Sprite();
        sprite.setImage("shield.png");

        new AnimationTimer(){
            @Override
            public void handle(long now) {
                if ((now-createdTime)/3>2050084413){
                    hasShield=false;
                    this.stop();
                }
                Game.gc.drawImage(sprite.image,toShield.positionX-7,toShield.positionY,63,63);

            }
        }.start();

    }

    public void shieldOn(){
        hasShield=true;
        new Sound(path, 3);
        thread = new Thread(this);
        thread.start();
    }
}
