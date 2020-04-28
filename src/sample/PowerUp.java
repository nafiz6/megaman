package sample;

public class PowerUp {
    Sprite sprite;
    double add = 0.15;
    double posX;
    double posY;
    double width;
    double height;
    boolean taken=false;
    String path = "src/sounds/healthPowerUp.wav";
    double takenTime=0;


    PowerUp(String image,double posX, double posY, double width, double height){
        taken=false;
        sprite=new Sprite();
        sprite.setImage(image);
        this.width= width;
        this.height = height;
        this.posX = posX;
        this.posY = posY;
        sprite.width = width;
        sprite.height = height;
        sprite.positionX=posX;
        sprite.positionY=posY;
    }



    public boolean intersects(Character character){
        if (sprite.intersects(character) && !taken){
            takenTime = System.nanoTime();
            taken=true;
            //new Sound(path, 3);
            return true;
        }
        return false;
    }

    public void display(){
        if (taken)return;
        if (posY>105 || posY<95)add*=-1;
        posY+=add;
        Game.gc.drawImage(sprite.image,posX,posY,40,40);
    }


}
