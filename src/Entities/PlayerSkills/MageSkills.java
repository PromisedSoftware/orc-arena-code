package Entities.PlayerSkills;
import Entities.Entity;
import Entities.EntityNums;
import Entities.Player;
import Inputs.MouseInputs;
import LanguageStrings.Language;
import MainPackage.CheatsPanel;
import MainPackage.Settings;
import MainPackage.Wish;
import Maps.Terrain;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class responsible for adding skills to entity
 */
public class MageSkills extends EntitySkills {

    volatile private Terrain terrain;

    private ArrayList<FireBall> fireballs;
    float fireBallDamage;
    float barrierDamage;
    float healRate;
    int mouseX, mouseY;
    boolean castedQ;

    private Shield barrier;
    private long playerHarmedAt;
    private boolean castedW;
    HashMap<Entity,Boolean> microCDs=new HashMap<>();

    private Heal heal;
    private boolean castedE;

    /**
     * Gives player skills of Mage
     * @param player player object
     */
    public MageSkills(Player player) {
        super(player);
        if(Wish.getTerrain()!=null) terrain=Wish.getTerrain();
        fireballs=new ArrayList<>();

        Q_lastTimeUsed=0;
        Q_skillCD =3500;
        Q_duration=0;
        Q_isActive =false;
        Q_CD_wearOff =true;
        Q_manaCost=25;

        W_lastTimeUsed =0;
        W_skillCD =8000;
        W_duration=6000;
        W_isActive =false;
        W_CD_wearOff =true;
        W_manaCost=35;

        E_lastTimeUsed =0;
        E_skillCD =9000;
        E_duration=0;
        E_isActive =false;
        E_CD_wearOff =true;
        E_manaCost=30;
    }



    /**
     * Prepares entity to use skill
     * Checks if player has enough mana if true
     * it makes entity preparation to cast spell
     */
    @Override
    public void castQ() {
        player.isPerformingAction(
                player.getAnimationType() != EntityNums.ATTACK
                        && player.isPerformingAction()
        );

        if(Q_CD_wearOff
                && player.getCurrentManaPoints()>=Q_manaCost
                && !player.isPerformingAction()){
            fireBallDamage=20+player.getLevel()*25;
            Q_isActive=true;
            castedQ=true;
            player.isPerformingAction(true);
            mouseX =MouseInputs.mouseX();
            mouseY =MouseInputs.mouseY();
            player.setAnimationStage(0);
            player.setAnimationType(EntityNums.SPELL_Q);
        }
    }

    /**
     * Prepares entity to use skill
     * Checks if player has enough mana if true
     * it makes entity preparation to cast spell
     * entity is immortal when this skill is active
     */
    @Override
    public void castW() {
        player.isPerformingAction(
                player.getAnimationType() != EntityNums.ATTACK
                        && player.isPerformingAction()
        );

        if(!castedW && W_CD_wearOff
                && player.getCurrentManaPoints()>=W_manaCost
                && !player.isPerformingAction()){
            player.isPerformingAction(true);
            castedW=true;
            W_CD_wearOff=false;
            W_lastTimeUsed=System.currentTimeMillis();
            W_isActive=true;
            player.isPerformingAction(true);
            player.setImmortal(true);
            player.setAnimationStage(0);
            player.setAnimationType(EntityNums.SPELL_W);
        }
    }

    /**
     * Prepares entity to use skill
     * Checks if player has enough mana if true
     * it makes entity preparation to cast spell
     * creates new object of Heal class
     */
    @Override
    public void castE() {
        player.isPerformingAction(
                player.getAnimationType() != EntityNums.ATTACK
                && player.isPerformingAction()
        );

        if(E_CD_wearOff
                && player.getCurrentManaPoints()>=E_manaCost
                && !player.isPerformingAction()){
            player.isPerformingAction(true);
            E_CD_wearOff=false;
            E_isActive=true;
            castedE=true;
            player.isPerformingAction(true);
            healRate =25+player.getLevel()*8;
            if(!CheatsPanel.isCheatManaEnabled()) player.decreaseManaPoints(E_manaCost);
            E_lastTimeUsed=System.currentTimeMillis();
            player.setAnimationStage(0);
            player.setAnimationType(EntityNums.SPELL_E);
            heal=new Heal();
            player.add(heal,(Integer)1);
        }
    }

    /**
     * Checks current status of skills
     */
    @Override
    public void checkSkills() {
        checkSkillQ();
        fireBallDamage=20+player.getLevel()*12;

        checkSkillW();
        barrierDamage =3+player.getLevel()*3;

        checkSkillE();
        healRate =25+player.getLevel()*8;
    }

    /**
     * Renders animation for skills
     */
    @Override
    public void render() {
        for(FireBall f:fireballs) f.repaint();
        if(barrier!=null) barrier.repaint();
        if(heal!=null) heal.repaint();
    }

    /**
     * Checks if skill is on cooldown if true
     * it assign time how long it will yet last
     * If casting skill is possible it prepares for casting
     * When given animation stage is achieved it creates
     * new FireBall object and make it cast, and sets cooldown
     * of that skill
     */
    private void checkSkillQ(){
        if(!Q_CD_wearOff) {
            if (Q_lastTimeUsed + Q_skillCD <= System.currentTimeMillis())
                Q_CD_wearOff = true;
        }
        if(Q_CD_wearOff && player.getAnimationType()==EntityNums.SPELL_Q && player.getAnimationStage()==3){
            Q_CD_wearOff=false;
            Q_isActive=false;
            if(castedQ){
                castedQ=false;
                fireballs.add(new FireBall(mouseX, mouseY));
                if(!CheatsPanel.isCheatManaEnabled()) player.decreaseManaPoints(Q_manaCost);
                player.isPerformingAction(false);
                Q_lastTimeUsed=System.currentTimeMillis();
            }
        }
        else if(!Q_isActive&&player.getAnimationType()==EntityNums.SPELL_Q&&player.getAnimationStage()==0){
            player.isPerformingAction(false);
            Q_isActive=true;
        }
        if(!fireballs.isEmpty()){
            for(int i=0; i<fireballs.size();i++) fireballs.get(i).runThread();
        }
    }

    /**
     * Checks if skill is on cooldown if true
     * it assign time how long it will yet last
     * If casting skill is possible it prepares for casting
     * When given animation stage is achieved it creates
     * new Barrier object, makes it cast and sets cooldown
     */
    private void checkSkillW(){
        if(!W_CD_wearOff) {
            if (W_lastTimeUsed + W_skillCD <= System.currentTimeMillis()){
                W_CD_wearOff = true;}
        }

        if(castedW && player.getAnimationStage()==3
                && player.getAnimationType()==EntityNums.SPELL_W){

            barrier=new Shield();
            W_lastTimeUsed=System.currentTimeMillis();
            if(!CheatsPanel.isCheatManaEnabled()) player.decreaseManaPoints(W_manaCost);
            player.add(barrier,(Integer)2);
            castedW=false;
        }

        if(W_isActive) {
            if(!castedW && W_lastTimeUsed+W_duration < System.currentTimeMillis()){
                W_isActive=false;
                player.remove(barrier);
                barrier=null;
                player.setImmortal(false);
            }

            for (int i = 0; i < Wish.getAllEntities().size(); i++) {
                Entity entity = Wish.getAllEntities().get(i);
                if(!microCDs.containsKey(entity)) microCDs.put(entity,false);
                if (entity.getEnemy() != player || entity == player) continue;
                boolean cond1 = entity.getAnimationType() == EntityNums.ATTACK;
                boolean cond2 = entity.getAnimationStage() == 2;
                boolean cond3 = entity.getEnemy() == player;

                if (barrier!=null && cond1 && cond2 && cond3 && !microCDs.get(entity)) {
                    microCDs.put(entity,true);
                    entity.getDamage(barrierDamage);
                    playerHarmedAt=System.currentTimeMillis();
                    barrier.stage=1;
                }
                if(cond1 && cond3 && entity.getAnimationStage() == 3){
                    microCDs.put(entity,false);
                }
                if(playerHarmedAt+100<System.currentTimeMillis() && barrier!=null){
                    barrier.stage=0;}
            }
        }
        if(player.getAnimationStage()==0
                && player.getAnimationType()==EntityNums.SPELL_W) {
            if(!castedW){
                player.isPerformingAction(false);
            }
        }
    }

    /**
     * Checks if skill is on cooldown if true
     * it assign time how long it will yet last
     * If casting skill is possible it prepares for casting
     * When given animation stage is achieved it creates
     * new animation effect object, makes it cast and sets cooldown
     */
    private void checkSkillE(){
        if(!E_CD_wearOff) {
            if (E_lastTimeUsed + E_skillCD <= System.currentTimeMillis())
                E_CD_wearOff = true;
        }

        if(castedE && player.getAnimationStage()==3
                && player.getAnimationType()==EntityNums.SPELL_E){
            castedE=false;
            player.heal(healRate);
            player.add(heal,(Integer)1);
        }
        else if(!castedE && player.getAnimationType()==EntityNums.SPELL_E
        && player.getAnimationStage()==0){
            player.isPerformingAction(false);
            player.remove(heal);
            heal=null;
        }
    }

    /**
     * @return String description of skill
     */
    @Override
    public String getDescription_Q() {
        return Language.Skills.Q_Mage[0]+fireBallDamage+Language.Skills.Q_Mage[1];
    }

    /**
     * @return String description of skill
     */
    @Override
    public String getDescription_W() {
        return Language.Skills.W_Mage[0]+(float)(W_duration/1000)+"s"+Language.Skills.W_Mage[1]+barrierDamage+Language.Skills.W_Mage[2];
    }

    /**
     * @return String description of skill
     */
    @Override
    public String getDescription_E() {
        return Language.Skills.E_Mage+ healRate;
    }

    //= = = = = = = = = = = = = = =     CLASSES     = = = = = = = = = = = = = = = = = = = = =

    /**
     * Private class responsible for making visual effects
     * and mechanisms of a skill
     */
    private class FireBall extends JPanel implements Runnable{

        private int directionX,directionY;
        private int destinationX,destinationY;
        private float speedLimiterX,speedLimiterY;
        private float currentX,currentY;
        private float speed;
        private int animationStage=0;
        private int animationType=0;
        private BufferedImage[][] animations;
        private long animationLastChanged;
        private float impactRange;
        private boolean isExploded=false;

        /**
         * Creates a Fireball object, loads animations and
         * calculate speed of traveling
         * @param mapLocationX Requested X location of skill to travel to
         * @param mapLocationY Requested Y location of skill to travel to
         */
        FireBall(int mapLocationX,int mapLocationY){
            setOpaque(false);
            setSize(Settings.SIZE_UNIT*2,Settings.SIZE_UNIT*2);
            destinationX = mapLocationX - getWidth()/2;
            destinationY = mapLocationY - getHeight()/2;
            currentX=(int)(player.getLocX()+player.getWidth()/2);
            currentY=(int)(player.getLocY()+player.getHeight()*0.5f);
            setLocation((int)currentX,(int)currentY);

            speed=Settings.SCALE_MULTIPLIER/Settings.FPS*400.f;
            speedLimiterY=Math.abs(currentY-destinationY)/Math.abs(currentX-destinationX);
            speedLimiterY= speedLimiterY>1 ? 1.0f:speedLimiterY;
            speedLimiterX=Math.abs(currentX-destinationX)/Math.abs(currentY-destinationY);
            speedLimiterX= speedLimiterX > 1? 1.0f:speedLimiterX;

            animations = new BufferedImage[4][2];
            try {loadImages();}
            catch (IOException e){
                System.out.println("MageSkills FireBall failed to load");
            }
            terrain.add(this,(Integer)200);
            animationLastChanged=System.currentTimeMillis();
        }

        /**
         * Load sub images of skill to make animation
         * @throws IOException Exception if reading file goes wrong
         */
        private void loadImages() throws IOException{
            int sizeOnSheetFireBall;
            int sizeOnSheetArea;
            BufferedImage imageFireBall;
            String imagePath ="src\\Images\\PlayerSkills\\sheets\\"+Settings.ACTOR_TEXTURE_QUALITY+"\\fireBall.png";
            imageFireBall = ImageIO.read(new File(imagePath));

            switch (Settings.ACTOR_TEXTURE_QUALITY) {
                case "Medium" -> {sizeOnSheetFireBall = 48;sizeOnSheetArea = 192;}
                case "High" -> {sizeOnSheetFireBall = 64;sizeOnSheetArea = 256;}
                case "Ultra" -> {sizeOnSheetFireBall = 96;sizeOnSheetArea = 384;}
                default -> {sizeOnSheetFireBall = 32;sizeOnSheetArea = 128;}
            }
            for (int i = 0; i < 4; i++) {
                animations[i][0] = imageFireBall.getSubimage(i * sizeOnSheetFireBall, 0, sizeOnSheetFireBall, sizeOnSheetFireBall);
            }
            for (int i = 0; i < 4; i++) {
                animations[i][1] = imageFireBall.getSubimage(i * sizeOnSheetArea, sizeOnSheetFireBall, sizeOnSheetArea, sizeOnSheetArea);
            }

        }

        /**
         * Makes a new thread for this instance
         */
        void runThread(){
            Thread thread;
            thread=Thread.ofVirtual().start(this);
            try{thread.join();}catch (Exception e){e.printStackTrace();}
        }

        /**
         * Make this skill to move to requested destination,
         * animates loaded images
         */
        private void move(){
            if((int)currentX==destinationX && (int)currentY==destinationY && animationType==0){
                if(animationType==0){
                    animationType++;
                    animationStage=0;
                    animationLastChanged=System.currentTimeMillis();
                }
                if(animationType==1 && animationStage==0 && !isExploded){
                    setSize(Settings.SIZE_UNIT*16,Settings.SIZE_UNIT*16);
                    impactRange=getHeight()*0.55f;
                    setLocation(
                            (int)(destinationX-getWidth()*0.5),
                            (int)(destinationY-getHeight()*0.5)
                    );
                    for(Entity e:Wish.getAllEntities()){
                        if(e==player) continue;
                        double distanceX = Math.abs((e.getLocX()+e.getWidth()*0.5f)-(getX()+getWidth()/2.0d));
                        double distanceY = Math.abs((e.getLocY()+e.getHeight()*0.5f)-(getY()+getHeight()/2.0d));
                        double distance =Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));
                        if(impactRange>=distance){
                            player.dealDamage(e,fireBallDamage);
                        }
                    }
                    isExploded = true;
                } else if(animationType==1 && animationStage==4){
                    this.setVisible(false);
                    terrain.remove(this);
                    fireballs.remove(this);
                }

            }else if(!isExploded){
                directionX=(int)currentX>destinationX? -1:1;
                directionY=(int)currentY>destinationY? -1:1;
                float diffX=Math.abs(currentX-destinationX);
                float diffY=Math.abs(currentY-destinationY);

                if((int)currentX!=destinationX){
                    diffX-=Math.abs(speed*directionX*speedLimiterX);
                    currentX=diffX<0? destinationX:(currentX+speed*directionX*speedLimiterX);
                }

                if((int)currentY!=destinationY){
                    diffY-=Math.abs(speed*directionY*speedLimiterY);
                    currentY=diffY<0? destinationY:(currentY+speed*directionY*speedLimiterY);
                }
                setLocation((int)currentX, (int)currentY);
            }
        }

        /**
         * Render images of this skill on screen
         * @param g the <code>Graphics</code> object to protect
         */
        public void paintComponent(Graphics g){
            if (animationStage>3) return;

            Graphics2D g2d = (Graphics2D) g;
            if(directionX<0)g2d.drawImage(
                    animations[animationStage][animationType],getWidth(),
                    0,
                    -getWidth() ,
                    getHeight() ,
                    null);

            else g2d.drawImage(
                    animations[animationStage][animationType],
                    0,
                    0,getWidth(),
                    getHeight() ,
                    null);
        }


        /**
         * Thread responsible for changing animation stage and looping it
         */
        @Override
        public void run() {
            if(animationLastChanged+100<System.currentTimeMillis()){
                animationLastChanged=System.currentTimeMillis();
                animationStage++;
                if(animationType==0&& animationStage>3) animationStage=0;
            }
            move();
        }
    }

    /**
     * Skill, adds new image on npc to make animations
     * then renders them based on player animation stage
     */
    private class Shield extends JPanel{
        int stage=0;
        BufferedImage[] animation;

        /**
         * Prepare skill animations
         */
        Shield(){
            setVisible(true);
            setSize(player.getSize());
            setOpaque(false);
            animation=new BufferedImage[2];
            try{loadImages();}catch(IOException e){
                System.out.println("MageSkills failed to load images at Shield");
            }
        }

        /**
         * Load animation based on given images
         * @throws IOException Exception if reading file goes wrong
         */
        private void loadImages() throws IOException {
            int sizeOnSheet;
            BufferedImage imageBarrier;

            switch (Settings.ACTOR_TEXTURE_QUALITY) {
                case "Medium" -> {sizeOnSheet = 96;}
                case "High" -> {sizeOnSheet = 128;}
                case "Ultra" -> {sizeOnSheet = 192;}
                default -> {sizeOnSheet = 64;}
            }
            String imagePath ="src\\Images\\PlayerSkills\\sheets\\"+Settings.ACTOR_TEXTURE_QUALITY+"\\Barrier.png";
            imageBarrier=ImageIO.read(new File(imagePath));

            for(int i=0;i<animation.length;i++){
                animation[i]=imageBarrier.getSubimage(
                        sizeOnSheet*i,0,sizeOnSheet,sizeOnSheet);
            }
        }

        /**
         * Renders image on the screen
         * @param g the <code>Graphics</code> object to protect
         */
        public void paintComponent(Graphics g){
            Graphics2D g2d=(Graphics2D)g;
            g2d.drawImage(animation[stage],0,0,getWidth(),getHeight(),null);
        }
    }

    /**
     * Skill, adds animations and mechanisms of a skill
     */
    private class Heal extends JPanel{
        BufferedImage[] animation;

        /**
         * Prepare skill animations
         */
        Heal(){
            setVisible(true);
            setSize(player.getSize());
            setOpaque(false);
            animation=new BufferedImage[4];
            try{loadImages();}catch(IOException e){
                System.out.println("Failed to load images at MageSkills Heal");}
        }

        /**
         * Loading images to make animations of it
         * @throws IOException Exception if reading file goes wrong
         */
        private void loadImages() throws IOException {
            int sizeOnSheet;
            BufferedImage imageBarrier;

            switch (Settings.ACTOR_TEXTURE_QUALITY) {
                case "Medium" -> {sizeOnSheet = 96;}
                case "High" -> {sizeOnSheet = 128;}
                case "Ultra" -> {sizeOnSheet = 192;}
                default -> {sizeOnSheet = 64;}
            }

            String imagePath ="src\\Images\\PlayerSkills\\sheets\\"+Settings.ACTOR_TEXTURE_QUALITY+"\\Heal.png";
            imageBarrier=ImageIO.read(new File(imagePath));
            for(int i=0;i<animation.length;i++){
                animation[i]=imageBarrier.getSubimage(sizeOnSheet*i,0,sizeOnSheet,sizeOnSheet);
            }
        }

        /**
         * Renders skill image on the screen
         * @param g the <code>Graphics</code> object to protect
         */
        public void paintComponent(Graphics g){
            Graphics2D g2d=(Graphics2D)g;
            g2d.drawImage(animation[player.getAnimationStage()],0,0,getWidth(),getHeight(),null);
        }
    }
}
