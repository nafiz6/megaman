package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class HealthBar{


    double health;
    GraphicsContext gc;
    Character character;
    int posX;
    int posY;
    int direction;
    Sprite sprite;
    double width;
    double maxHealth;
    double bullets;

    HealthBar (Character character, double health, int posX, int posY, int direction ){
        this.health = health;
        this.character = character;
        this.gc = Game.gc;
        this.posX = posX;
        this.posY = posY;
        this.direction = direction;
        maxHealth = health;
        bullets =7;

        sprite = new Sprite();

        if (direction==1)sprite.setImage("healthBar/healthBar1.png");
        else sprite.setImage("healthBar/healthBar1Rev.png");

        sprite.setPosition(posX,posY);
        sprite.width*=15;
        sprite.height*=15;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
        if (this.health<0)this.health=0;
        if (this.health>maxHealth)this.health=maxHealth;
    }

    public void render(){
        if (health<maxHealth && health>0){
            health+=0.05;
        }
        if (bullets<7)bullets+=0.01;

        width = (health*139)/maxHealth;
        Color c;
        if (direction==1) {
            gc.setFill(new Color(0, 0.4, 1, 1));
            gc.fillRect(posX + 10, posY + 20, width, 20);
            sprite.render(gc);
            c=Color.web("47464e");
            gc.setFill(c);
            gc.fillRect(posX + 71-(7-Math.floor(bullets))*8.3,posY+35,(7-Math.floor(bullets))*8.3,14);
        }

        else{
            gc.setFill(new Color(0.8, 0.2, 0, 1));
            gc.fillRect(posX + 24 + (maxHealth-health)*139/maxHealth, posY + 20, width, 20);
            sprite.render(gc);
            c=Color.web("47464e");
            gc.setFill(c);
            gc.fillRect(posX + 102.1,posY+35,(7-Math.floor(bullets))*8.3,14);
        }
    }

}
