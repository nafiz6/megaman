package sample;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.canvas.GraphicsContext;
import javafx.geometry.Rectangle2D;

public class Sprite
{
    protected Image image;
    public double positionX;
    public double positionY;
    private double velocityX;
    private double velocityY;
    public double width;
    public double height;
    private double accelerationX;
    private double accelerationY;

    public Sprite()
    {
        positionX = 0;
        positionY = 0;
        velocityX = 0;
        velocityY = 0;
        accelerationY = 0;
        accelerationX = 0;
    }

    public void setImage(Image i)
    {
        image = i;
        width = i.getWidth();
        height = i.getHeight();
    }

    public void setImage(String filename)
    {
        Image i = new Image(filename);
        setImage(i);
    }

    public void setPosition(double x, double y)
    {
        positionX = x;
        positionY = y;
    }

    public void setVelocity(double x, double y)
    {
        velocityX = x;
        velocityY = y;
    }

    public void addVelocity(double x, double y)
    {
        velocityX += x;
        velocityY += y;
    }

    public void setAcceleration(double x, double y) {
        accelerationX = x;
        accelerationY = y;
    }

    public void addAcceleration(double x, double y) {
        accelerationX += x;
        accelerationY += y;
    }

    public void update(double time)
    {//System.out.println(velocityY + " 1c" + accelerationY);
        velocityX += accelerationX ;
        velocityY += accelerationY ;
        positionX += velocityX;
        positionY += velocityY;

       // System.out.println(velocityY + " 2c" + accelerationY);
    }

    public double getVelocityY() {
        return velocityY;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public void render(GraphicsContext gc)
    {
        Game.gc.drawImage(image, positionX, positionY);
    }

    public void render(GraphicsContext gc,double width, double height)
    {
        gc.drawImage(image, positionX, positionY, width, height);

    }

    public void renderByCenter(){
        double x = positionX - image.getWidth()/2;
        double y = positionY - image.getHeight()/2;
        Game.gc.drawImage(image,x,y);
    }

    public Rectangle2D getBoundary()
    {
        return new Rectangle2D(positionX,positionY,width,height);
    }

    public boolean intersects(Sprite s)
    {
        return s.getBoundary().intersects( this.getBoundary() );
    }

    public boolean contains(Point2D s)
    {
        return this.getBoundary().contains( s );
    }

    public String toString()
    {
        return " Position: [" + positionX + "," + positionY + "]"
                + " Velocity: [" + velocityX + "," + velocityY + "]";
    }
}