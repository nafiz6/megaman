package sample;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;

public class Fire implements Runnable {

    Thread fireThread;
    GraphicsContext gc;
    Character shooter;
    Character opponent;
    Sprite sprite;

    Fire(GraphicsContext gc, Character shooter, Character opponent) {
        fireThread = new Thread(this);
        this.gc = gc;
        this.shooter = shooter;
        this.opponent = opponent;
        fireThread.start();
    }


    public void run() {
        sprite = new Sprite();

        if(shooter.getDirection() > 0) {
            sprite.setImage("bullet/bullet.png");
            sprite.setPosition(shooter.getBoundary().getMaxX()-10, shooter.getBoundary().getMinY()+10);
            sprite.setVelocity(8,0);
        }
        else {
            sprite.setImage("bullet/bulletRev.png");
            sprite.setPosition(shooter.getBoundary().getMinX()-17, shooter.getBoundary().getMinY()+10);
            sprite.setVelocity(-8,0);
        }

        final long[] lastNanoTime = new long[1];
        lastNanoTime[0] = System.nanoTime();
        final long createdTime = System.nanoTime();

        new AnimationTimer() {

            public void handle(long currentNanoTime) {
                double elapsedTimePerFrame = (currentNanoTime - lastNanoTime[0])/ 1000000000.0;
                lastNanoTime[0] = currentNanoTime;
                if (currentNanoTime-createdTime>650084413)this.stop();

                if(sprite.getBoundary().getMaxX() > Game.WIN_WIDTH) {
                    this.stop();
                }

                if(sprite.intersects(opponent)) {
                    opponent.hitFlag = true;
                    opponent.damage(10);
                    this.stop();
                }

                sprite.update(elapsedTimePerFrame);
                sprite.render(gc);
            }
        }.start();
    }
}