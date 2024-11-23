package Entities.PlayerSkills;
import Entities.Entity;
import Entities.EntityNums;
import Entities.Player;
import Inputs.MouseInputs;
import LanguageStrings.Language;
import MainPackage.CheatsPanel;
import MainPackage.Settings;
import MainPackage.Wish;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Class responsible for adding skills to entity
 */
public class AgentSkills extends EntitySkills {
    float bonusDamage;
    FuryAttack furyAttack;
    Bribery bribery;
    Teleport teleport;
    boolean castedW,castedE;
    int W_range;
    Point requestedLocation;
    /**
     * Gives player skills of Agent
     * @param player
     */
    public AgentSkills(Player player) {
        super(player);
        bonusDamage =15 + player.getLevel()*40;
        Q_lastTimeUsed=0;
        Q_skillCD =9_000;
        Q_duration=4_000;
        Q_isActive =false;
        Q_CD_wearOff =true;
        Q_manaCost=15;

        W_lastTimeUsed = 0;
        W_skillCD =18_000;
        W_duration=6_000;
        W_isActive =false;
        W_CD_wearOff =true;
        W_manaCost=30;

        E_lastTimeUsed =0;
        E_skillCD =3_000;
        E_duration=3_000;
        E_isActive =false;
        E_CD_wearOff =true;
        E_manaCost=30;

    }

    /**
     * Cast skill assigned to Q - key
     */
    @Override
    public void castQ() {
        if(Q_isActive) return;

        player.isPerformingAction(
                player.getAnimationType() != EntityNums.ATTACK
                        && player.isPerformingAction()
        );

        if(Q_CD_wearOff && player.getCurrentManaPoints() >= Q_manaCost){
            requestedLocation = new Point(MouseInputs.mouseX(),MouseInputs.mouseY());
            player.setAnimationStage(0);
            bonusDamage=player.getLevel()*15;
            if(!CheatsPanel.isCheatManaEnabled()) player.decreaseManaPoints(Q_manaCost);
            Q_lastTimeUsed=System.currentTimeMillis();
            player.increaseBonusDamage(bonusDamage);
            furyAttack = new FuryAttack();
            player.add(furyAttack);
            Q_isActive=true;
            Q_CD_wearOff=false;
        }
    }

    /**
     * Cast skill assigned to W - key
     */
    @Override
    public void castW() {
        player.isPerformingAction(
                player.getAnimationType() != EntityNums.ATTACK
                        && player.isPerformingAction()
        );

        if(W_CD_wearOff
                && player.getCurrentManaPoints()>=W_manaCost
                && !player.isPerformingAction()){
            W_range = Settings.SIZE_UNIT*10;
            W_CD_wearOff=false;
            W_isActive=true;
            castedW =true;
            player.isPerformingAction(true);
            if(!CheatsPanel.isCheatManaEnabled()) player.decreaseManaPoints(W_manaCost);
            W_lastTimeUsed = System.currentTimeMillis();
            player.setAnimationStage(0);
            player.setAnimationType(EntityNums.SPELL_W);
            bribery = new Bribery();
            player.add(bribery,(Integer)1);
        }
    }

    /**
     * Cast skill assigned to E - key
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
            requestedLocation = new Point(MouseInputs.mouseX(),MouseInputs.mouseY());
            E_CD_wearOff=false;
            E_isActive=true;
            castedE =true;
            player.isPerformingAction(true);
            if(!CheatsPanel.isCheatManaEnabled()) player.decreaseManaPoints(E_manaCost);
            E_lastTimeUsed = System.currentTimeMillis();
            player.setAnimationStage(0);
            player.setAnimationType(EntityNums.SPELL_E);
            teleport = new Teleport();
            player.add(teleport,(Integer)1);
        }
    }

    /**
     * Checks if skills are ready to use
     */
    @Override
    public void checkSkills() {
        checkSkillW();
        checkSkillE();
        checkSkillQ();
    }

    /**
     * Checks if skill is on cooldown if true
     * it assign time how long it will yet last
     * If casting skill is possible it prepares for casting
     * When given animation stage is achieved it creates
     * new animation effect object, makes it cast and sets cooldown
     */
    private void checkSkillQ(){
        if(!Q_CD_wearOff) {
            if (Q_lastTimeUsed + Q_skillCD <= System.currentTimeMillis()){
                Q_CD_wearOff = true;
            }
        }

        if(Q_isActive){
            boolean cond1=Q_lastTimeUsed+Q_duration<=System.currentTimeMillis();
            boolean cond2=player.getAnimationStage()==2 &&
                    player.getAnimationType()==EntityNums.ATTACK;
            if((cond1 || cond2) && player.isAttackonOnCD()){
                player.decreaseBonusDamage(bonusDamage);
                player.remove(furyAttack);
                furyAttack = null;
                Q_isActive=false;
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
    private void checkSkillW(){
        if(!W_CD_wearOff) {
            if (W_lastTimeUsed + W_skillCD <= System.currentTimeMillis())
                W_CD_wearOff = true;
        }

        if(castedW && player.getAnimationStage()==2
                && player.getAnimationType()==EntityNums.SPELL_W){

            castedW = false;
            double distanceX;
            double distanceY;
            double distance;
            for(Entity e: Wish.getAllEntities()){
                if(player==e) continue;
                distanceX=Math.abs(
                        (player.getLocX()+(float)player.getWidth()/2) -(e.getLocX()+(float)e.getWidth()/2));
                distanceY=Math.abs(
                        (player.getLocY()+(float)player.getHeight()/2)-(e.getLocY()+(float)e.getHeight()/2));
                distance=Math.sqrt(Math.pow(distanceX,2)+Math.pow(distanceY,2));

                if(W_range >= distance && !e.isTriggered()){
                    e.setTeamColor(EntityNums.TEAM_ALLY);
                    e.removeEnemyTeam(EntityNums.TEAM_ALLY);
                    e.removeEnemyTeam(EntityNums.TEAM_PLAYER);
                    e.removeEnemyTeam(EntityNums.TEAM_CIVILIAN);
                    e.addEnemyTeam(EntityNums.TEAM_ENEMY);

                    e.enemyFound=false;
                    e.enemyInAttackRange=false;
                    e.enemy=null;
                }
            }
        }

        else if(!castedW && player.getAnimationType()==EntityNums.SPELL_W
                && player.getAnimationStage()==0){
            player.isPerformingAction(false);
            player.remove(bribery);
            bribery=null;
        }
    }

    private void checkSkillE(){
        if(!E_CD_wearOff) {
            if (E_lastTimeUsed + E_skillCD <= System.currentTimeMillis())
                E_CD_wearOff = true;
        }

        if(castedE && player.getAnimationStage()==2
                && player.getAnimationType()==EntityNums.SPELL_E){

            castedE = false;
            player.teleport((int)requestedLocation.getX(),(int)requestedLocation.getY());
        }

        else if(!castedE && player.getAnimationType()==EntityNums.SPELL_E
                && player.getAnimationStage()==0){
            player.remove(teleport);
            teleport = null;
            player.isPerformingAction(false);
        }
    }

    /**
     * @return Description of Q skill
     */
    @Override
    public String getDescription_Q() {
        return Language.Skills.Q_Agent+bonusDamage+Language.Skills.Q_Warr[1];
    }

    /**
     * @return Description of W skill
     */
    @Override
    public String getDescription_W() {
        return Language.Skills.W_Agent;
    }

    /**
     * @return Description of E skill
     */
    @Override
    public String getDescription_E() {
        return Language.Skills.E_Agent;
    }

    /**
     * Renders skills
     */
    @Override
    public void render(){
    }

    /**
     * Skill, adds animations and mechanisms of a skill
     */
    class FuryAttack extends JPanel {
        int stage = 0;
        BufferedImage[] animation;
        FuryAttack(){
            setVisible(true);
            setSize(player.getSize());
            setOpaque(false);
            animation=new BufferedImage[3];
            try {loadImages();
            } catch (IOException e){
                System.out.println("Agent Warrior FurryAttack IO exception");
            }
        }

        /**
         * Load animation based on given images
         * @throws IOException Exception if reading file goes wrong
         */
        private void loadImages() throws IOException{
            int sizeOnSheet;
            BufferedImage imageFury;

            switch (Settings.ACTOR_TEXTURE_QUALITY) {
                case "Medium" -> {sizeOnSheet = 96;}
                case "High" -> {sizeOnSheet = 128;}
                case "Ultra" -> {sizeOnSheet = 192;}
                default -> {sizeOnSheet = 64;}
            }

            String imagePath ="src\\Images\\PlayerSkills\\sheets\\"+Settings.ACTOR_TEXTURE_QUALITY+"\\Fury.png";
            imageFury=ImageIO.read(new File(imagePath));
            for(int i=0; i<animation.length; i++){
                animation[i] = imageFury.getSubimage(
                        sizeOnSheet*i,0,sizeOnSheet,sizeOnSheet);
            }
        }

        /**
         * Renders image on the screen
         * @param g the <code>Graphics</code> object to protect
         */
        public void paintComponent(Graphics g){
            Graphics2D g2d = (Graphics2D)g;
            stage = player.getAnimationStage();
            if(stage >= 3) stage=0;
            g2d.drawImage(animation[stage],
                    0, 0, player.getWidth(), player.getHeight(), null);
        }
    }

    /**
     * Skill, adds animations and mechanisms of a skill
     */
    class Bribery extends JPanel{
        BufferedImage[] animation;

        /**
         * Prepare skill animations
         */
        Bribery(){
            setVisible(true);
            setSize(player.getSize());
            setOpaque(false);
            animation=new BufferedImage[4];
            try {loadImages();}catch (IOException e){
                //System.out.println(e);
            }
        }

        /**
         * Loading images to make animations of it
         * @throws IOException Exception if reading file goes wrong
         */
        private void loadImages() throws IOException{
            int sizeOnSheet;
            BufferedImage imageBribary;

            switch (Settings.ACTOR_TEXTURE_QUALITY) {
                case "Medium" -> {sizeOnSheet = 96;}
                case "High" -> {sizeOnSheet = 128;}
                case "Ultra" -> {sizeOnSheet = 192;}
                default -> {sizeOnSheet = 64;}
            }

            String imagePath ="src\\Images\\PlayerSkills\\sheets\\"+Settings.ACTOR_TEXTURE_QUALITY+"\\Bribe.png";
            imageBribary = ImageIO.read(new File(imagePath));
            for(int i=0; i<animation.length; i++){
                animation[i] = imageBribary.getSubimage(
                        sizeOnSheet*i,0,sizeOnSheet,sizeOnSheet);
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

    /**
     * Teleport load images ready for animation and animates them
     */
    private class Teleport extends JPanel{
        BufferedImage[] animation;
        Teleport(){
            setVisible(true);
            setSize(player.getSize());
            setOpaque(false);
            animation=new BufferedImage[4];
            try {loadImages();}catch (IOException e){
                System.out.println("Exception at AgentSkills Teleport");
            }
        }

        /**
         * Loading images to make animations of it
         * @throws IOException Exception if reading file goes wrong
         */
        private void loadImages() throws IOException{
            int sizeOnSheet;
            BufferedImage imageTeleport;

            switch (Settings.ACTOR_TEXTURE_QUALITY) {
                case "Medium" -> {sizeOnSheet = 96;}
                case "High" -> {sizeOnSheet = 128;}
                case "Ultra" -> {sizeOnSheet = 192;}
                default -> {sizeOnSheet = 64;}
            }

            String imagePath ="src\\Images\\PlayerSkills\\sheets\\"+Settings.ACTOR_TEXTURE_QUALITY+"\\Teleport.png";
            imageTeleport = ImageIO.read(new File(imagePath));
            for(int i=0; i<animation.length; i++){
                animation[i] = imageTeleport.getSubimage(
                        sizeOnSheet*i,0,sizeOnSheet,sizeOnSheet);
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
