package Entities;

import Inputs.MouseInputs;
import MainPackage.CheatsPanel;
import MainPackage.Settings;
import MainPackage.Wish;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Orc extended with Entity class, mechanisms of npc
 */
public class Orc extends Entity implements MouseListener, MouseMotionListener {
    public Orc(int locationPercentX,int locationPercentY, int lvl){
        super("Orc",locationPercentX,locationPercentY,lvl);
        level=lvl;
        setLevel(level);

        setAttackRange(2);
        setEntitySizeMultiplier(1.6f);

        enemyTeams.add(EntityNums.TEAM_PLAYER);
        enemyTeams.add(EntityNums.TEAM_CIVILIAN);
        enemyTeams.add(EntityNums.TEAM_ALLY);
        teamColor=EntityNums.TEAM_ENEMY;

        movementSpeed= Settings.SCALE_MULTIPLIER*movementSpeedMultiplier/Settings.FPS;
        currentHealthPoints=maxHealthPoints;
        currentManaPoints=maxManaPoints;
        aliveFlag=true;
        animationsArrayCount=new int[]{2,4,4,3};
        setAppearance();
        loadAnimations(animationsArrayCount);
        updateHealthBar();
        healthBar.setValueColor(teamColor);
        addMouseListener(this);
        addMouseMotionListener(this);

        distanceSearchForEnemy(11);
    }

    //= = = = = = = = = = = = =      METHODS    = = = = = = = = = = = = = = =

    /**
     * Tasks that Orc will do when spawned on map
     */
    @Override
    void tasks() {
        if(CheatsPanel.isCheatAI_Enabled()) return;
        if(enemy == null)lookForEnemy(distanceSearchForEnemy());
        else if(enemy !=null && teamColor==EntityNums.TEAM_ALLY) lookForEnemy(distanceSearchForEnemy());
        if(enemyFound && enemy != null && teamColor == EntityNums.TEAM_ALLY){
                for(Entity e : Wish.getAllEntities()){
                    if(e.teamColor == teamColor){
                        e.isTriggered(true);
                        e.enemyFound=true;
                        e.enemy=enemy;
                        e.setDestination((int)enemy.loc_x+enemy.getWidth()/2,(int)enemy.loc_y+enemy.getHeight());
                    }
                }
        }
        if(enemyInAttackRange){
            meleeAttack(enemy);
            //
        }
        if(enemyFound&&!enemyInAttackRange) {
            setDestination((int)enemy.loc_x+enemy.entityWidth/2,(int)enemy.loc_y+enemy.entityHeight);
            move();
        }
        else if(!enemyFound && !enemyInAttackRange){
            if(animationStage==0 && animationType==EntityNums.ATTACK)performingAction=false;
            moveRandom();
        }
        if(!enemyFound && this.teamColor== EntityNums.TEAM_ALLY) spawnPoint = Wish.getPlayer().getLocation();
        lookForEnemy(distanceSearchForEnemy());
    }
    @Override
    protected void setLevel(int level){
        this.level=level;
        maxHealthPoints=20 + 16 * level;
        basicAttackDamage=2 + 6 * level;
        maxManaPoints=0;
        armorPoints=10+5*level;
        expForKill=level*25;
        healthRegen=5f;
        manaRegen=0.1f;
        movementSpeedMultiplier=35.0f;
    }

    /**
     * Rendering skills of orc on the screen
     */
    @Override
    public void renderSkills(){}

    /**
     * What will happen when Player click or drag mouse on this Orc
     * @param e the event to be processed
     */
    private void mouseInputEvent(MouseEvent e){
        if(aliveFlag){
            if(e.getButton()==3) {
                if(teamColor != EntityNums.TEAM_ENEMY){
                    setTeamColor(EntityNums.TEAM_ENEMY);
                    removeEnemyTeam(EntityNums.TEAM_ENEMY);
                    addEnemyTeam(EntityNums.TEAM_PLAYER);
                    addEnemyTeam(EntityNums.TEAM_ALLY);
                    addEnemyTeam(EntityNums.TEAM_CIVILIAN);
                }
                Wish.getPlayer().enemy = this;
            }
        }
        else {
            if(Wish.getPlayer().animationType==EntityNums.ATTACK) Wish.getPlayer().performingAction=false;
            Wish.getPlayer().setDestination(MouseInputs.mouseX()+e.getX(),MouseInputs.mouseY()+e.getY());
        }
    }
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseInputEvent(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    /**
     * What will happen when Player click mouse on this Orc
     * @param e the event to be processed
     */
    @Override
    public void mouseDragged(MouseEvent e) {

    }

    /**
     * What will happen when Player move mouse on this Orc
     * @param e the event to be processed
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        int x = this.getX();
        int y = this.getY();

        MouseInputs.mouseX(x+e.getX());
        MouseInputs.mouseY(y+e.getY());
    }
}
