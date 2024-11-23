package Entities.PlayerSkills;
import Entities.Entity;
import Entities.EntityNums;
import Entities.Player;
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
import java.util.ArrayList;

public class WarriorSkills extends EntitySkills {

    private ArmorShield armorShield;
    private FuryAttack furyAttack;
    private MultipleAttack multipleAttack;

    float bonusDamage;
    float bonusArmor;
    ArrayList<Entity> enemies;

    /**
     * Class responsible for adding skills to entity
     * @param player player object
     */
    public WarriorSkills(Player player) {
        super(player);
        enemies=new ArrayList<>();

        Q_lastTimeUsed=0;
        Q_skillCD =7000;
        Q_duration=3000;
        Q_isActive =false;
        Q_CD_wearOff =true;
        Q_manaCost=25;

        W_lastTimeUsed =0;
        W_skillCD =10000;
        W_duration=4000;
        W_isActive =false;
        W_CD_wearOff =true;
        W_manaCost=20;

        E_lastTimeUsed =0;
        E_skillCD =7000;
        E_duration=3000;
        E_isActive =false;
        E_CD_wearOff =true;
        E_manaCost=20;
    }

    /**
     * When have enough mana
     * increases damage player can deal
     * with his next attack
     */
    @Override
    public void castQ() {
        if(Q_isActive) return;
        if(Q_CD_wearOff && player.getCurrentManaPoints() >= Q_manaCost){
            player.setAnimationStage(0);
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
     * when enough mana
     * increases amount of armor based on level,
     * sets cooldown on that skill
     * consumes mana
     */
    @Override
    public void castW() {
        if(W_isActive) return;
        if(W_CD_wearOff && player.getCurrentManaPoints()>=W_manaCost){
            W_CD_wearOff=false;
            if(!CheatsPanel.isCheatManaEnabled()) player.decreaseManaPoints(W_manaCost);
            W_isActive=true;
            W_lastTimeUsed = System.currentTimeMillis();
            armorShield = new ArmorShield();
            player.add(armorShield,(Integer)2);
            player.increaseBonusArmor(bonusArmor);
        }
    }

    /**
     * Increases range of next attack
     * and will hit multiple targets
     * sets cooldown on that skill
     * consumes mana
     */
    @Override
    public void castE() {
        if(E_isActive) return;

        player.isPerformingAction(
                player.getAnimationType() != EntityNums.ATTACK
                        && player.isPerformingAction()
        );

        if(E_CD_wearOff && player.getCurrentManaPoints()>=E_manaCost){
            player.setAnimationStage(0);
            E_lastTimeUsed =System.currentTimeMillis();
            if(!CheatsPanel.isCheatManaEnabled()) player.decreaseManaPoints(E_manaCost);
            E_isActive = true;
            E_CD_wearOff = false;
            multipleAttack = new MultipleAttack();
            multipleAttack.setVisible(false);
            player.add(multipleAttack);
            player.setAttackRange(player.attackRangeMultiplier()+3);
        }
    }

    /**
     * Checks if skills are ready to use, duration
     * and disables it when wear off
     */
    @Override
    public void checkSkills() {
        checkSkillW();
        bonusArmor=player.getLevel()*15;

        checkSkillE();

        checkSkillQ();
        bonusDamage=player.getLevel()*12;
    }

    /**
     * Check if skill w is ready to use and other stuff
     */
    private void checkSkillW(){
        if (W_lastTimeUsed + W_skillCD <= System.currentTimeMillis()){
            W_CD_wearOff = true;
        }

        //Check if w stop working because of certain time have already passed
        if(W_isActive){
            if(W_lastTimeUsed+W_duration<=System.currentTimeMillis()){
                player.decreaseBonusArmor(bonusArmor);
                player.remove(armorShield);
                armorShield = null;
                W_isActive=false;
            }
        }
    }

    /**
     * Check if skill w is ready to use and other stuff
     */
    private void checkSkillE(){
        if(E_isActive){
            E_isActive=E_lastTimeUsed+E_duration>=System.currentTimeMillis();
            boolean cond2=player.getAnimationStage()==2 &&
                    player.getAnimationType()==EntityNums.ATTACK;
            if(cond2){
                 multipleAttack.setVisible(true);
                 double distanceX;
                 double distanceY;
                 double distance;
                 for(Entity e:Wish.getAllEntities()){
                     if(player==e) continue;
                     distanceX=Math.abs(
                             (player.getLocX()+player.getWidth()/2f) - (e.getLocX() + e.getWidth()/2f));
                     distanceY=Math.abs(
                             (player.getLocY()+player.getHeight()/2f) - (e.getLocY() + e.getHeight()/2f));
                     distance=Math.sqrt(Math.pow(distanceX,2)+Math.pow(distanceY,2));
                     if(player.getAttackRange()*2>=distance)
                         if(player.getEnemy()!=e) player.dealDamage(e, player.getBonusDamage() + player.getBasicAttackDamage());
                 }
                E_isActive=false;
            }
        }
        else if(multipleAttack != null && player.getAnimationStage()==0){
            player.remove(multipleAttack);
            multipleAttack=null;
            E_isActive=false;
            player.setAttackRange(player.attackRangeMultiplier()-3);
        }

        if(!E_CD_wearOff){
            if(E_lastTimeUsed+E_skillCD <=System.currentTimeMillis())
                E_CD_wearOff=true;
        }
    }

    private boolean attacked=false;
    /**
     * Check if skill w is ready to use and other stuff
     */
    private void checkSkillQ(){
        if(player.getAnimationStage() ==1) attacked=false;
        if(!Q_CD_wearOff) {
            if (Q_lastTimeUsed + Q_skillCD <= System.currentTimeMillis()){
                Q_CD_wearOff = true;
            }
        }

        if(Q_isActive){
            if(player.getAnimationType() == EntityNums.ATTACK
                    && player.getAnimationStage()==2
                    && !attacked){
                player.heal(bonusDamage);
                attacked=true;
            }
        }
        boolean cond1=Q_lastTimeUsed+Q_duration<=System.currentTimeMillis();
        if(cond1 && furyAttack !=null){
            Q_isActive=false;
            player.decreaseBonusDamage(bonusDamage);
            player.remove(furyAttack);
            furyAttack = null;
        }
    }

    /**
     * @return String description of skill
     */
    @Override
    public String getDescription_Q() {
        return Language.Skills.Q_Warr[0]+bonusDamage+Language.Skills.Q_Warr[1];
    }

    /**
     * @return String description of skill
     */
    @Override
    public String getDescription_W() {
        return Language.Skills.W_Warr[0]+bonusArmor+Language.Skills.W_Warr[1]+(float)(W_duration/1000)+"s";
    }

    /**
     * @return String description of skill
     */
    @Override
    public String getDescription_E() {
        return Language.Skills.E_Warr;
    }

    /**
     * Renders skills on the screen
     */
    @Override
    public void render() {
        if(armorShield != null) armorShield.repaint();
    }

    /**
     * Skill, adds animations and mechanisms of a skill
     */
    private class ArmorShield extends JPanel {
        int stage = 0;
        BufferedImage[] animation;
        ArmorShield(){
            setVisible(true);
            setSize(player.getSize());
            setOpaque(false);
            animation=new BufferedImage[1];
            try {loadImages();}catch (IOException e){
                for(StackTraceElement el: e.getStackTrace())
                    System.out.println(el);
            }
        }

        /**
         * Load animation based on given images
         * @throws IOException Exception if reading file goes wrong
         */
        private void loadImages() throws IOException{
            int sizeOnSheet;
            BufferedImage imageArmorShield;

            switch (Settings.ACTOR_TEXTURE_QUALITY) {
                case "Medium" -> {sizeOnSheet = 96;}
                case "High" -> {sizeOnSheet = 128;}
                case "Ultra" -> {sizeOnSheet = 192;}
                default -> {sizeOnSheet = 64;}
            }

            String imagePath ="src\\Images\\PlayerSkills\\sheets\\"+Settings.ACTOR_TEXTURE_QUALITY+"\\ArmorShield.png";
            imageArmorShield = ImageIO.read(new File(imagePath));
            for(int i=0; i<animation.length; i++){
                animation[i] = imageArmorShield.getSubimage(
                        sizeOnSheet*i,0,sizeOnSheet,sizeOnSheet);
            }
        }

        /**
         * Renders image on the screen
         * @param g the <code>Graphics</code> object to protect
         */
        public void paintComponent(Graphics g){
            Graphics2D g2d = (Graphics2D)g;
            g2d.drawImage(animation[stage],
                    (player.getWidth() - getHeight()/3)/2,
                    player.getHeight() - getHeight()/2,
                    getWidth()/3,
                    getHeight()/3,
                    null);
        }
    }

    private class FuryAttack extends JPanel{
        int stage = 0;
        BufferedImage[] animation;
        FuryAttack(){
            setVisible(true);
            setSize(player.getSize());
            setOpaque(false);
            animation=new BufferedImage[3];
            try {loadImages();
            } catch (IOException e){
                System.out.println(e);
                System.out.println("Warrior skills IO exception");
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
            imageFury = ImageIO.read(new File(imagePath));
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

    private class MultipleAttack extends JPanel{
        BufferedImage[] animation;
        MultipleAttack(){
            setVisible(true);
            setSize(player.getSize());
            setOpaque(false);
            animation=new BufferedImage[1];
            try {loadImages();
            } catch (IOException e){
                for(StackTraceElement el:e.getStackTrace())
                    System.out.println(el);
            }
        }

        /**
         * Load animation based on given images
         * @throws IOException Exception if reading file goes wrong
         */
        private void loadImages() throws IOException{
            int sizeOnSheet;
            BufferedImage imageMultipleAttack;

            switch (Settings.ACTOR_TEXTURE_QUALITY) {
                case "Medium" -> {sizeOnSheet = 96;}
                case "High" -> {sizeOnSheet = 128;}
                case "Ultra" -> {sizeOnSheet = 192;}
                default -> {sizeOnSheet = 64;}
            }

            String imagePath ="src\\Images\\PlayerSkills\\sheets\\"+Settings.ACTOR_TEXTURE_QUALITY+"\\multipleAttack.png";
            imageMultipleAttack = ImageIO.read(new File(imagePath));
            for(int i=0; i<animation.length; i++){
                animation[i] = imageMultipleAttack.getSubimage(
                        sizeOnSheet*i,0,sizeOnSheet,sizeOnSheet);
            }
        }

        /**
         * Renders image on the screen
         * @param g the <code>Graphics</code> object to protect
         */
        public void paintComponent(Graphics g){
            Graphics2D g2d = (Graphics2D)g;
            g2d.drawImage(animation[0],
                    0, 0, player.getWidth(), player.getHeight(), null);
        }
    }
}
