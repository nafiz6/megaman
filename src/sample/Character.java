package sample;


import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.lang.reflect.GenericArrayType;
import java.util.ArrayList;

public class Character extends Sprite {

    Character opponent;

    HealthBar healthBar;
    int direction;
    boolean hitFlag = false;
    boolean onFloor = false;
    boolean touchedTop = false;

    boolean meleeFlag = false;
    boolean meleeDamaged = false;
    int meleeState = 0;
    int hitAnimState = 0;
    int dieAnimState = 0;

    boolean playLandSound = false;




    boolean shootable = true;
    public int shootOnlyOnce = -1;

    String up, left, right, shoot, melee;

    public ArrayList<String> input = new ArrayList<>();

    public ArrayList<String> controls = new ArrayList<>();

    String bulletSoundPath = "src/sounds/fire.wav";
    String meleeSoundPath = "src/sounds/melee.wav";
    String damageSoundPath = "src/sounds/damage.wav";
    String jumpSoundPath = "src/sounds/jump.wav";
    String landSoundPath = "src/sounds/land.wav";
    String noAmmoSoundPath = "src/sounds/land.wav";



    AnimatedImage characterStand;
    AnimatedImage characterStandReverse;
    AnimatedImage characterRun;
    AnimatedImage characterRunReverse;
    AnimatedImage characterRunShoot;
    AnimatedImage characterRunShootReverse;
    AnimatedImage characterJump;
    AnimatedImage characterMelee;
    AnimatedImage characterMeleeReverse;
    AnimatedImage characterHit;
    AnimatedImage characterHitReverse;
    AnimatedImage characterDie;
    AnimatedImage characterDieReverse;

    Image[] standImageArray;
    Image[] standReverseImageArray;
    Image[] runImageArray;
    Image[] runReverseImageArray;
    Image[] runShootImageArray;
    Image[] runShootReverseImageArray;
    Image[] jumpImageArray;
    Image[] meleeImageArray;
    Image[] meleeReverseImageArray;
    Image[] hitImageArray;
    Image[] hitReverseImageArray;
    Image[] dieImageArray;
    Image[] dieReverseImageArray;
    Shield shield;

    Character(int direction) {

        super();
        this.direction = direction;
        shield = new Shield(this);


        characterStand = new AnimatedImage();
        characterStandReverse = new AnimatedImage();
        characterRun = new AnimatedImage();
        characterRunReverse = new AnimatedImage();
        characterRunShoot = new AnimatedImage();
        characterRunShootReverse = new AnimatedImage();
        characterJump = new AnimatedImage();
        characterMelee = new AnimatedImage();
        characterMeleeReverse = new AnimatedImage();
        characterHit = new AnimatedImage();
        characterHitReverse = new AnimatedImage();
        characterDie = new AnimatedImage();
        characterDieReverse = new AnimatedImage();

        standImageArray = new Image[3];
        standReverseImageArray = new Image[3];
        runImageArray = new Image[11];
        runReverseImageArray = new Image[11];
        runShootImageArray = new Image[11];
        runShootReverseImageArray = new Image[11];
        jumpImageArray = new Image[7];
        meleeImageArray = new Image[11];
        meleeReverseImageArray = new Image[11];
        hitImageArray = new Image[3];
        hitReverseImageArray = new Image[3];
        dieImageArray = new Image[12];
        dieReverseImageArray = new Image[12];

        for(int i = 0; i<3;i++){
            standImageArray[i] = new Image("stand/stand_" + (i+1)+ ".png");
            standReverseImageArray[i] = new Image("standRev/standRev_" + (i+1)+ ".png");
        }

        for(int i = 0; i<11;i++){
            runReverseImageArray[i] = new Image("run/runRev_" + (i+1)+ ".png");
            runImageArray[i] = new Image("run/run_" + (i+1)+ ".png");
        }

        for(int i = 0; i<11;i++){
            runShootImageArray[i] = new Image("runShoot/runShoot_" + (i+1)+ ".png");
            runShootReverseImageArray[i] = new Image("runShootRev/runShootRev_" + (i+1)+ ".png");
        }

        for(int i = 0; i<7;i++){
            jumpImageArray[i] = new Image("jump/jump_" + (i+1)+ ".png");
        }

        for(int i = 0; i<11;i++){
            meleeImageArray[i] = new Image("melee/melee_" + (i+1)+ ".png");
            meleeReverseImageArray[i] = new Image("meleeRev/meleeRev_" + (i+1)+ ".png");
        }

        for(int i = 0; i<3;i++){
            hitImageArray[i] = new Image("damage/damage_" + (i+1)+ ".png");
            hitReverseImageArray[i] = new Image("damageRev/damageRev_" + (i+1)+ ".png");
        }
        for(int i = 0; i< 12; i++) {
            dieImageArray[i] = new Image("die/die_" + (i+1) + ".png");
            dieReverseImageArray[i] = new Image("dieRev/dieRev_" + (i+1) + ".png");
        }

        characterStand.frames = standImageArray;
        characterStand.duration = 0.100;

        characterStandReverse.frames = standReverseImageArray;
        characterStandReverse.duration = 0.100;

        characterRun.frames = runImageArray;
        characterRun.duration = 0.100;

        characterRunReverse.frames = runReverseImageArray;
        characterRunReverse.duration = 0.100;

        characterRunShoot.frames = runShootImageArray;
        characterRunShoot.duration = 0.100;

        characterRunShootReverse.frames = runShootReverseImageArray;
        characterRunShootReverse.duration = 0.100;

        characterJump.frames = jumpImageArray;
        characterJump.duration = 0.100;

        characterMelee.frames = meleeImageArray;
        characterMelee.duration = 0.050;

        characterMeleeReverse.frames = meleeReverseImageArray;
        characterMeleeReverse.duration = 0.050;

        characterHit.frames = hitImageArray;
        characterHit.duration = 0.100;

        characterHitReverse.frames = hitReverseImageArray;
        characterHitReverse.duration = 0.100;

        characterDie.frames = dieImageArray;
        characterDie.duration = 0.100;

        characterDieReverse.frames = dieReverseImageArray;
        characterDieReverse.duration = 0.100;
    }

    @Override
    public void setImage(Image i)
    {
        image = i;
        width = i.getWidth() * Game.CHARACTER_SCALE;
        height = i.getHeight() * Game.CHARACTER_SCALE;
    }

    @Override
    public void render(GraphicsContext gc)
    {
        Game.gc.drawImage(image, positionX, positionY, width, height);

    }

    public void renderOpponent(GraphicsContext gc) {
        ColorAdjust ca = new ColorAdjust();
        ca.setHue(+0.7);
        gc.setEffect(ca);
        gc.drawImage(image, positionX, positionY, width, height);
        ca.setHue(0);
        gc.setEffect(ca);
    }

    public void createHealthBar(int health, int posX, int posY, int dir){
        healthBar = new HealthBar(this, health, posX, posY, dir);
    }

    public void damage(double dmg){

        //new Sound(damageSoundPath, 3);

        if (shield.hasShield) return;
        healthBar.setHealth(healthBar.getHealth()-dmg);
    }
    public void increaseHealth(double health){
        healthBar.setHealth(healthBar.getHealth()+health);
    }

    public void setOpponent(Character opponent) {
        this.opponent = opponent;
    }

    public void setGravity (double acceleration, Sprite[] floors) {
        onFloor = false;

        for(Sprite floor: floors) {
            if(floor.getBoundary().contains(this.getBase()) || floor.getBoundary().intersects(this.getBase())) {

                this.setPosition(positionX, floor.getBoundary().getMinY() - this.getBoundary().getHeight() + 3);
                onFloor = true;
                touchedTop = false;

                if(playLandSound) {
                    //new Sound(landSoundPath, 3);
                    playLandSound = false;
                }

            }
         }
        if(!onFloor) {
            playLandSound = true;
            setAcceleration(-getVelocityX(),acceleration);
        } else {
            setAcceleration(-getVelocityX(),0);
            setVelocity(getVelocityX(),0);
        }
    }

    public boolean detectCollisionWith (Sprite ... sprites) {

        for(Sprite s: sprites) {
            if(this.getFront().intersects(s.getBoundary())) {
                return true;
            }
        }
        return false;
    }

    public void detectVerticalCollisionsWith (Sprite ... sprites) {
        for (Sprite s: sprites) {
            if(this.getTop().intersects(s.getBoundary()) && !touchedTop) {
                this.setVelocity(getVelocityX(), 0);
                touchedTop = true;
            }
        }
    }

    public void stand(double t) {
        if(direction > 0) {

            setImage(characterStand.getFrame(t));

        }
        else {
            setImage(characterStandReverse.getFrame(t));

        }

    }

    public void run(double t) {
        if(this.detectCollisionWith(Game.floors) || this.detectCollisionWith( Game.sideWallLeft, Game.sideWallRight)) {
            if(direction > 0) {
                setImage(characterRun.getFrame(t));
                setVelocity(0,getVelocityY());
            }
            else {
                setImage(characterRunReverse.getFrame(t));
                setVelocity(0, getVelocityY());
            }
        }
        else {
            if(direction > 0) {
                setImage(characterRun.getFrame(t));
                addVelocity(3.0,0);
            }
            else {
                setImage(characterRunReverse.getFrame(t));
                addVelocity(-3.0,0);
            }
        }

    }

    public void jump() {
        if (getVelocityY()==0) {
            //new Sound(jumpSoundPath, 3);
            setVelocity(getVelocityX(),-10);
            playLandSound = true;
        }

    }

    void dieAnim() {
        if (dieAnimState/4 >11) return;
        if(direction > 0) {
            setImage(characterDie.frames[dieAnimState/4]);
        } else {
            setImage(characterDieReverse.frames[dieAnimState/4]);
        }
        dieAnimState++;
    }


    public void shoot(Character opponent) {
        //new Sound(bulletSoundPath, 2);
        new Fire(Game.gc,this, opponent);
    }
    public void shootAnim(double t) {
        if(getVelocityX() > 0) {
            setImage(characterRunShoot.getFrame(t));
        }

        else if(getVelocityX() < 0) {
            setImage(characterRunShootReverse.getFrame(t));
        }
        else {
            if(direction > 0) {
                setImage(characterRunShoot.getFrame(0));
            }
            else {
                setImage(characterRunShootReverse.getFrame(0));
            }
        }
    }

    public void melee(Character opponent) {


        if(direction>0) {
            setImage(characterMelee.frames[meleeState/2]);

        }
        else {
            setImage(characterMeleeReverse.frames[meleeState/2]);
        }

        if(intersects(opponent)) {

            if (!meleeDamaged){
                opponent.hitFlag=true;
                meleeDamaged=true;
                opponent.damage(20);
            }
        }

    }

    public boolean hitAnim() {


        if(hitAnimState >18) {
            hitAnimState=-1;
            return false;
        }

        if(direction > 0) {
            setImage(characterHit.frames[(hitAnimState/3)%3]);
        }
        else {
            setImage(characterHitReverse.frames[(hitAnimState/3)%3]);
        }
        return true;
    }

    public void setInput(String up, String left, String right, String shoot, String melee) {
        this.up = up;
        this.right = right;
        this.left = left;
        this.shoot = shoot;
        this.melee = melee;
        controls.add(up);
        controls.add(left);
        controls.add(right);
        controls.add(shoot);
        controls.add(melee);
    }

    public void handleInput(double timeSinceStart) {

        if(input.isEmpty() && healthBar.health>0) {
            this.stand(timeSinceStart);
        }
        if (input.contains(right)){
            this.setDirection(1);
            this.run(timeSinceStart);
        }

        if(input.contains(left)) {
            this.setDirection(-1);
            this.run(timeSinceStart);
        }

        if(input.contains(up)) {

            this.jump();
        }


        if(input.contains(shoot) ) {
            this.shootAnim(timeSinceStart);

            if (shootable && healthBar.bullets>=1) {
                healthBar.bullets-=1;
                if (healthBar.bullets<0) {
                    healthBar.bullets=0;
                }
                if(healthBar.bullets == 0) {
                    //new Sound(noAmmoSoundPath, 1);
                }
                this.shoot(opponent);
                shootOnlyOnce++;
                shootable = false;
            }
        }
        if(input.contains(melee) && !meleeFlag) {
            meleeFlag = true;
        }

        if(meleeFlag && meleeState<=20) {
            if(meleeState ==1) {
                //new Sound(meleeSoundPath, 2);
                }
            this.melee(opponent);
            meleeState++;
        }

        if (!input.contains(melee) && meleeState>20){
            meleeFlag=false;
            meleeState=0;
            meleeDamaged=false;
        }

        if(hitFlag) {
            hitFlag=hitAnim();
            hitAnimState++;
        }


    }

    public void createShield(){

        shield.shieldOn();
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getDirection() {
        return direction;
    }

    public Rectangle2D getBase() {
        if(this.getBoundary().getHeight() <= 10) return new Rectangle2D(0,0,0,0);
        return new Rectangle2D (getBoundary().getMinX() + 5,
                getBoundary().getMinY() + getBoundary().getHeight()-2, getBoundary().getWidth() - 20, 2);
    }

    public Rectangle2D getFront() {
        if(this.getBoundary().getHeight() <= 10) return new Rectangle2D(0,0,0,0);
        else {
            if(direction > 0) return new Rectangle2D(this.getBoundary().getMinX() + 40 ,
                    this.getBoundary().getMinY() + 4,
                    5, getBoundary().getHeight() - 15);
            else return new Rectangle2D(this.getBoundary().getMinX() , this.getBoundary().getMinY() + 4,
                    5,getBoundary().getHeight() - 15);
        }

    }

    public Rectangle2D getTop() {
        if(this.getBoundary().getHeight() <= 10) return new Rectangle2D(0,0,0,0);
        return new Rectangle2D(this.getBoundary().getMinX() + 5, this.getBoundary().getMinY(), getBoundary().getWidth() - 20, 3);
    }

    public void refresh(){
        hitFlag=false;
        hitAnimState=0;
        meleeState=0;
        dieAnimState=0;
    }



    }

