package Entities;
import Entities.PlayerSkills.*;
import Inputs.MouseInputs;
import MainPackage.Settings;
import MainPackage.Wish;
import HUD.HUD;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;


public class Player extends Entity{

    public static String playerClassType ="";
    public static String playerGender="";
    private EntitySkills skills;


    /**
     * Constructs player setting attributes
     * @param lvl character level of the player that will be set
     * @param percentLocationX Location X on map player will be spawned
     * @param percentLocationY Location Y on map player will be spawned
     */
    public Player(int percentLocationX, int percentLocationY, int lvl){
        super("Player",percentLocationX,percentLocationY,lvl);
        level=lvl;
        if(playerClassType.equals("Warrior")){
            animationsArrayCount=new int[]{4,3,4,3};
            skills=new WarriorSkills(this);
        }
        else if(playerClassType.equals("Mage")){
            animationsArrayCount=new int[]{4,3,4,3,4,4,4};
            skills=new MageSkills(this);
        }
        else{
            animationsArrayCount=new int[]{4,3,4,3,0,4,4};
            skills=new AgentSkills(this);
        }
        maxHealthPoints=200;

        Wish.playerToArray(this);
        setLevel(level);
        movementSpeed=Settings.SCALE_MULTIPLIER*movementSpeedMultiplier/Settings.FPS;
        currentHealthPoints=maxHealthPoints;
        currentManaPoints=maxManaPoints;
        expForKill=level*10;
        aliveFlag=true;
        enemyTeams.add(EntityNums.TEAM_ENEMY);
        teamColor=EntityNums.TEAM_PLAYER;
        healthBar.setValueColor(teamColor);
        current_exp=0;
        next_exp=50;

        this.setAppearance();
        loadAnimations(animationsArrayCount);

        setAttackRange(2);
        updateHealthBar();

    }

    //= = = = = = = = = = = = = =    METHODS    = = = = = = = = = = = = = =

    /**
     * Set level of player, and attributes
     * @param level Which level player should have
     */
    @Override
     protected void setLevel(int level){
        this.level=level;
        switch (Player.playerClassType){
            case "Warrior"->{
                basicAttackDamage = 16+level*6;
                maxHealthPoints =200 + level*20;
                healthRegen= 0.75F + level * 0.1F;
                maxManaPoints = 100+level*10;
                manaRegen = 0.5F;
                armorPoints = 50 +level*10;
                movementSpeedMultiplier=45.0f;
            }
            case "Mage"->{
                basicAttackDamage = 9+level*2;
                maxHealthPoints =200 + level*5;
                healthRegen= 0.30F + level * 0.02F;
                maxManaPoints = 150+level*30;
                manaRegen = 1.5F;
                armorPoints = 30 + level*2;
                movementSpeedMultiplier=60.0f;
            }
            default->{
                basicAttackDamage = 16+level*4;
                maxHealthPoints =150 + level*10;
                healthRegen= 1.25F + level *0.5F;
                maxManaPoints = 80+level*10;
                manaRegen = 0.75F;
                armorPoints = 80 +level*5;
                movementSpeedMultiplier = 75.0f;
            }
        }
    }

    /**
     * Makes player start to use mechanisms
     * to cast skill Q
     */
    public void castSpellQ(){
        skills.castQ();
    }

    /**
     * Makes player start to use mechanisms
     * to cast skill W
     */
    public void castSpellW(){
            skills.castW();
    }

    /**
     * Makes player start to use mechanisms
     * to cast skill E
     */
    public void castSpellE(){
        skills.castE();
    }

    public String Q_getSkillDescription(){return skills.getDescription_Q();}
    public String W_getSkillDescription(){return skills.getDescription_W();}
    public String E_getSkillDescription(){return skills.getDescription_E();}

    public long Q_getCD(){return skills.getQ_skillCD();}
    public long W_getCD(){return skills.getW_skillCD();}
    public long E_getCD(){return skills.getE_skillCD();}

    /**
     * Basic existential tasks player will be doing
     * when spawned on map
     */
    @Override
    void tasks() {
        if(enemy!=null && !enemyInAttackRange){
            try{
                checkIfEnemyInAttackRange(enemy);
                setDestination(
                        (int)enemy.loc_x+enemy.getWidth()/2,
                        (int)enemy.loc_y + enemy.getHeight()
                );
                move();
            }catch(NullPointerException e){
                for(StackTraceElement element:e.getStackTrace()){
                    System.out.println(element);
                }
            }
        }
        else if(enemy !=null && enemyInAttackRange){
            meleeAttack(enemy);
        }
        else{
            if(getAnimationType()==EntityNums.ATTACK) setAnimationType(EntityNums.IDLE);
            enemyInAttackRange=false;
            enemyFound=false;
            move();
        }
        skills.checkSkills();
    }

    /**
     * Renders skills of player on the screen
     */
    @Override
    public void renderSkills(){
        skills.render();
    }

    /**
     * Render player appearance on the screen
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    public void paintComponent(Graphics g) {
        try {
            g2d = (Graphics2D) g;
            if (directionX == 1)
                g2d.drawImage(animations.get(animationType).get(animationStage), this.entityWidth, 0, -this.entityWidth, this.entityHeight, null);
            else
                g2d.drawImage(animations.get(animationType).get(animationStage), 0, 0, this.entityWidth, this.entityHeight, null);
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
}
