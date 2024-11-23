package Entities;
import MainPackage.CheatsPanel;
import MainPackage.Settings;
import MainPackage.Wish;
import MyComponents.ProgressBar;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Entity implementation of every thing entity should have
 */
public abstract class Entity extends JLayeredPane implements Runnable, MouseListener {

    protected volatile int entityHeight, entityWidth;
    protected String name="";
    protected String entity_type;

    protected volatile float attackRangeMultiplier=2.0f;
    protected volatile float attackRange = Settings.SIZE_UNIT*attackRangeMultiplier;
    protected volatile float basicAttackDamage=0;
    protected volatile float bonusDamage;
    protected volatile float maxHealthPoints;
    protected volatile float currentHealthPoints;
    protected volatile float healthRegen=0.25f;
    protected volatile float maxManaPoints;
    protected volatile float currentManaPoints;
    protected volatile float manaRegen=0.25f;
    protected volatile int armorPoints;
    protected volatile float bonusArmorPoints;
    protected volatile int level;
    protected volatile int expForKill;
    protected volatile int current_exp=0;
    protected volatile int next_exp=400;
    protected volatile ProgressBar healthBar;
    protected volatile boolean isImmortal=false;

    protected volatile float movementSpeed;
    protected volatile float bonusMovementSpeed=0;
    protected volatile float movementSpeedMultiplier;
    protected volatile float loc_x,loc_y;
    protected volatile int destination_x,destination_y;
    protected volatile int directionX=1;
    protected volatile int directionY=1;

    protected volatile boolean aliveFlag;
    protected volatile Color teamColor;
    protected volatile ArrayList<Color> enemyTeams;
    public volatile boolean enemyFound;

    private int lookForEnemyDistance;
    public volatile Entity enemy;
    public volatile boolean enemyInAttackRange=false;
    protected volatile Point spawnPoint;
    protected volatile long lastTimeInFight=0;
    protected volatile boolean isTriggered = false;

    protected volatile boolean performingAction=false;
    protected volatile boolean basicAttackCD=false;

    protected float entitySizeMultiplier=1.0f;
    protected int[] animationsArrayCount={};
    protected volatile int animationStage=0;
    protected volatile int animationType=0;
    protected String imgAppearancePath;
    protected int entityWidthOnSheet;
    protected int entityHeightOnSheet;
    protected BufferedImage img;
    protected ArrayList<ArrayList<BufferedImage>> animations;
    protected Graphics2D g2d;

    protected Thread thread;
    double now=0;
    double secondStarted=0;
    public volatile boolean tasksDone;

    /**
     *
     * @param entity_type String with existing entity type
     * @param locationPercentX Location on map
     * @param locationPercentY Location on map
     */
    public Entity(String entity_type,int locationPercentX,int locationPercentY, int lvl){
        level=lvl;
        tasksDone=false;
        isImmortal=false;
        enemyTeams=new ArrayList<>();
        entityWidth =(int)(Settings.SIZE_UNIT*4*entitySizeMultiplier);
        entityHeight =(int)(Settings.SIZE_UNIT*4*entitySizeMultiplier);
        setSize(entityWidth,entityHeight);
        setLayout(null);
        this.entity_type=entity_type;

        loc_x=(float)(locationPercentX*Settings.GAME_PANEL_WIDTH)/100;
        loc_y=(float)(locationPercentY*Settings.GAME_PANEL_HEIGHT)/100;
        setLocation((int)loc_x,(int)loc_y);
        spawnPoint=new Point((int)loc_x,(int)loc_y);
        destination_x=(int)loc_x;
        destination_y=(int)loc_y;

        enemyFound =false;
        bonusDamage=0;

        healthBar=new ProgressBar();
        healthBar.setSize(
                (int)(entityWidth*0.8f),
                (int)(Settings.SIZE_UNIT*0.5f));
        healthBar.setLocation(
                (entityWidth-healthBar.getWidth())/2,0);
        healthBar.setBarColor(new Color(0,0,0, 90));
        healthBar.setValueColor(teamColor);
        healthBar.setTextColor(Color.BLACK);

        this.add(healthBar);
        this.setOpaque(false);
        tasksDone=true;
        Wish.putElementToArray(this);
    }
    // = = = = = = = = = = = = = =    METHODS    = = = = = = = = = = = = = =

    /**
     * Updates health bar status
     */
    protected void updateHealthBar(){
        healthBar.setMaxValue((int)maxHealthPoints);
        healthBar.setValue((int)currentHealthPoints);
        healthBar.setText(entity_type+" "+level+" LVL");
    }

    /**
     * Sets entity appearance, look animations etc.
     */
    protected void setAppearance(){
        try{
            switch(entity_type){
                case "Player"->{imgAppearancePath="src\\Images\\Appearances\\"+Settings.ACTOR_TEXTURE_QUALITY +"\\"+Player.playerGender+"_"+ Player.playerClassType +"_sheet.png";}
                default ->{imgAppearancePath="src\\Images\\Appearances\\"+Settings.ACTOR_TEXTURE_QUALITY +"\\"+ entity_type +"_sheet.png";}
            }
            img= ImageIO.read(new File(imgAppearancePath));
            switch(Settings.ACTOR_TEXTURE_QUALITY){
                case "Medium"->{
                    entityWidthOnSheet =96;entityHeightOnSheet =96;}
                case "High"->{
                    entityWidthOnSheet =128;entityHeightOnSheet =128;}
                case "Ultra"->{
                    entityWidthOnSheet =192;entityHeightOnSheet =192;}
                default->{
                    entityWidthOnSheet =64;entityHeightOnSheet =64;}
            }
        }catch(Exception e){System.out.println("Entity.setAppearance() Exception:"+e.getCause());}
    }

    /**
     * Loading animations for entity
     * @param aniTypeAmount amount of animations entity has
     */
    protected void loadAnimations(int[] aniTypeAmount){
        int aniStage=entityWidthOnSheet;
        int aniType=entityHeightOnSheet;

        animations=new ArrayList<>();
        for (int i=0;i<aniTypeAmount.length;i++){
            animations.add(new ArrayList<>());
            for(int j=0;j<aniTypeAmount[i];j++){
                animations.get(i).add(img.getSubimage(aniStage*j,aniType*i,entityWidthOnSheet,entityHeightOnSheet));
            }
        }
    }

    //- - - - - - - - - -    UPDATE SIZE AND LOCATION AFTER SETTINGS     - - - - - - - - - - - - -

    /**
     * Updates entity scale, location, animation speed etc
     * Used when Settings are changed
     */
    public void updateIt(){
        attackRange =Settings.SIZE_UNIT*attackRangeMultiplier;
        entityWidth =(int)(Settings.SIZE_UNIT*4*entitySizeMultiplier);
        entityHeight =(int)(Settings.SIZE_UNIT*4*entitySizeMultiplier);
        setSize(entityWidth, entityHeight);
        setAppearance();
        loadAnimations(animationsArrayCount);
        updateSpeed();

        float percentLocationX=loc_x/EntityNums.currentWidthPanel;
        float percentLocationY=loc_y/EntityNums.currentHeightPanel;
        float percentDestinationX=((float)destination_x/EntityNums.currentWidthPanel);
        float percentDestinationY=((float)destination_y/EntityNums.currentHeightPanel);

        loc_x=(int)(percentLocationX*Settings.GAME_PANEL_WIDTH);
        loc_y=(int)(percentLocationY*Settings.GAME_PANEL_HEIGHT);
        destination_x=(int)(percentDestinationX*Settings.GAME_PANEL_WIDTH);
        destination_y=(int)(percentDestinationY*Settings.GAME_PANEL_HEIGHT);

        spawnPoint=new Point((int)loc_x,(int)loc_y);

        setLocation((int)loc_x,(int)loc_y);

        healthBar.setSize(
                (int)(entityWidth*0.8f),
                (int)(Settings.SIZE_UNIT*0.5f));
        healthBar.setLocation(
                (entityWidth-healthBar.getWidth())/2,0);

    }
    //- - - - - - - - - - - - -    UPDATE SPEED    - - - - - - - - - - - - - - - - - - - - - -

    /**
     * Updates movement speed, and apply changes
     */
    public void updateSpeed(){
        movementSpeed=Settings.SCALE_MULTIPLIER*movementSpeedMultiplier/Settings.FPS;
    }
    //- - - - - - - - - - - - -     SET DESTINATION     - - - - - - - - - - - - - - -

    /**
     * Sets destination that entity is moving to
     * @param toX X location
     * @param toY Y location
     */
    public void setDestination(int toX, int toY){
        destination_x=toX-entityWidth /2;;
        destination_y=toY-entityHeight;
    }

    //- - - - - - - - - - - - -     MOVE ENTITY     - - - - - - - - - - - - - - -

    /**
     * Makes entity move to requested destination
     */
    protected void move(){
        if(animationType==EntityNums.DIE) return;
        if(animationType==EntityNums.IDLE && performingAction) performingAction=false;
        else if (performingAction) return;
        if((int)loc_x==destination_x&&(int)loc_y==destination_y){
            if(animationType== EntityNums.IDLE) return;
            animationType= EntityNums.IDLE;
            animationStage=0;
            return;
        }
        if(animationType!=EntityNums.WALK){
            animationStage=0;
            animationType= EntityNums.WALK;
        }
        float diffX=Math.abs(loc_x-destination_x);
        float diffY=Math.abs(loc_y-destination_y);
        if((int)loc_x>destination_x) directionX=-1;
        else directionX=1;
        if((int)loc_y>destination_y) directionY=-1;
        else directionY=1;

        if((int)loc_x!=destination_x){
            diffX-=Math.abs((bonusMovementSpeed+movementSpeed)*directionX);
            if(diffX<0){
                loc_x=destination_x;
            }else{
                loc_x+=(bonusMovementSpeed+movementSpeed)*directionX;
            }
        }
        if((int)loc_y!=destination_y){
            diffY-=Math.abs((bonusMovementSpeed+movementSpeed)*directionY);
            if(diffY<0){
                loc_y=destination_y;
            }else{
                loc_y+=(bonusMovementSpeed+movementSpeed)*directionY;
            }
        }
        setLocation((int)loc_x, (int)loc_y);
    }

    //- - - - - - - - - - - - - - -     MOVE RANDOM    - - - - - - - - - - - - - - -

    /**
     * It makes entity move to random close to its spawn point
     */
    public void moveRandom() {
        if(animationType==EntityNums.IDLE && !performingAction){

            int maxMoveDistance=Settings.SIZE_UNIT*4;
            int walkAreaX_start=spawnPoint.x-(maxMoveDistance-entityWidth/2);
            if(walkAreaX_start<0) walkAreaX_start=0;
            int walkAreaY_start=spawnPoint.y-(maxMoveDistance-entityHeight);
            if(walkAreaY_start<0) walkAreaY_start=0;

            Random random=new Random();
            int locationX= random.nextInt(maxMoveDistance*2)+walkAreaX_start;
            int locationY= random.nextInt(maxMoveDistance*2)+walkAreaY_start;

            if(locationX>EntityNums.currentWidthPanel) locationX=EntityNums.currentWidthPanel;
            if(locationY>EntityNums.currentHeightPanel) locationY=EntityNums.currentHeightPanel;

            setDestination(locationX,locationY);
        }
        move();
    }

    //- - - - - - - - - - - - - -    TELEPORT ENTITY    - - - - - - - - - - - - - -

    /**
     * Teleport entity to requested location on map
     * @param x X location
     * @param y Y location
     */
    public void teleport(int x,int y){
        loc_x= x-entityWidth/2;
        loc_y= y-entityHeight;
        setDestination((int)x,(int)y);
        setLocation((int)loc_x,(int)loc_y);

    }

    //- - - - - - - - - - - - - - - - - -     LOOK FOR ENEMIES    - - - - - - - - - - -

    /**
     * Makes entity look for enemies nearby
     * It multiplies screen unit size by this parameter
     * and it becomes searching range
     * @param searchRange range of searching
     */
    public void lookForEnemy(int searchRange) {
        searchRange*=Settings.SIZE_UNIT;
        double distanceX,distanceY,distance;
        double closestEnemyRange=100_000;

        HashMap<Double,Entity> enemiesInRange = new HashMap<>();

        for(int i=0; i<Wish.getAllEntities().size();i++){
            Entity entity=Wish.getAllEntities().get(i);

            if(entity.getTeamColor()==this.teamColor
                    ||entity.animationType==EntityNums.DIE
                    ||!entity.aliveFlag
                    ||entity.getTeamColor()==null) continue;

            else if(entity.enemyTeams.contains(this.teamColor)){
                try{
                    distanceX = Math.abs(Wish.getAllEntities().get(i).loc_x - this.loc_x);
                    distanceY = Math.abs(Wish.getAllEntities().get(i).loc_y - this.loc_y);
                    distance = Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));

                    if(searchRange>=distance){
                        enemiesInRange.put(distance,entity);
                        closestEnemyRange = Math.min(closestEnemyRange, distance);
                    }

                }catch(NullPointerException e){
                    for(StackTraceElement el: e.getStackTrace()){
                        System.out.println(el);
                    }
                }
            }
        }
        enemyFound = !enemiesInRange.isEmpty();
        if(enemyFound){
            enemy=enemiesInRange.get(closestEnemyRange);
            checkIfEnemyInAttackRange(enemy);
        }
    }

    /**
     * Checks if other enemy entity is in attack range
     * @param entity entity to check if in range
     */
    public void checkIfEnemyInAttackRange(Entity entity){
        try{
            if(entity.currentHealthPoints<0) return;
            double distanceX=Math.abs((enemy.loc_x+enemy.entityWidth/2)-(this.loc_x+entityWidth/2));
            double distanceY=Math.abs((enemy.loc_y+enemy.entityHeight)-(this.loc_y+entityHeight));
            double distance=Math.sqrt(Math.pow(distanceX,2)+Math.pow(distanceY,2));

            enemyInAttackRange=Math.abs(attackRange -distance)< attackRange;
            if(animationType==EntityNums.ATTACK && !enemyInAttackRange) performingAction=false;

        }catch (NullPointerException e){
            for(StackTraceElement el: e.getStackTrace())
                System.out.println(el);
        }
    }
    //- - - - - - - - - - - - - -    ATTACK    - - - - - - - - - - - - -

    /**
     * Used to attack entity
     * @param enemy Entity that will be attacked
     */
    protected void meleeAttack(Entity enemy){
        if(performingAction && animationType != EntityNums.ATTACK){
            return;
        }
        if(this.aliveFlag){
            if(!performingAction) {
                basicAttackCD=false;
                performingAction=true;
                animationStage=0;
                animationType=EntityNums.ATTACK;
            }
            if(animationStage==2 && !basicAttackCD){
                dealDamage(enemy,basicAttackDamage+bonusDamage);
                basicAttackCD=true;
            } else if(animationStage==3) {
                performingAction=false;
            }
        }
    }

    //- - - - - - - - - - - - - -    ATTACK    - - - - - - - - - - - - -

    /**
     * Deal damage to entity
     * @param enemyEntity Entity that will be attacked
     * @param damage Damage that will be dealt
     */
    public void dealDamage(Entity enemyEntity, float damage){
        boolean isEnemyA_Player = enemyEntity.entity_type.equals("Player");
        boolean isCheatGod = CheatsPanel.isCheatGodEnabled();
        if(!isEnemyA_Player){
            enemyEntity.enemy=this;
            enemyEntity.enemyFound=true;
        }
        //if( isEnemyA_Player && isCheatGod){ enemyEntity.getDamage(0);}
        //else enemyEntity.getDamage(damage);
        enemyEntity.getDamage(damage);
        lastTimeInFight=System.currentTimeMillis();
        if(enemyEntity.currentHealthPoints()<=0){
            enemyInAttackRange=false;
            enemyFound=false;
            if(teamColor==EntityNums.TEAM_ALLY){
                Player p = Wish.getPlayer();
                p.current_exp += enemyEntity.expForKill;
            }
            else current_exp += enemyEntity.expForKill;
        }
    }

    //- - - - - - - - - -     GET DAMAGE    - - - - - - - - - -

    /**
     * Make take damage
     * @param damagePoints Amount of damage
     */
    public void getDamage(float damagePoints){
        boolean isPlayer = entity_type.equals("Player");
        boolean isCheatGod = CheatsPanel.isCheatGodEnabled();
        lastTimeInFight=System.currentTimeMillis();
        if(isImmortal ||(isPlayer && isCheatGod)) return;
        float damage=damagePoints*((float)100/(100+armorPoints+bonusArmorPoints));
        currentHealthPoints-=(int)damage;
        healthBar.setValue((int)currentHealthPoints);
    }

    /**
     * Heal this entity
     * @param amount Amount of health points to heal
     */
    public void heal(float amount){
        currentHealthPoints+=amount;
        healthBar.setValue((int)currentHealthPoints);
        if(currentHealthPoints>maxHealthPoints) currentHealthPoints=maxHealthPoints;
    }

    /**
     * It makes entity checking if it's dead
     * if true runs dead animation
     */
    protected void tryDie(){
        if(animationType!=EntityNums.DIE){
            enemyFound=false;
            enemy=null;
            enemyInAttackRange=false;
            animationStage=0;
            animationType=EntityNums.DIE;
            for(int i=0;i<Wish.getAllEntities().size();i++){
                if(Wish.getAllEntities().get(i).enemy==this){
                    Wish.getAllEntities().get(i).enemy=null;
                    Wish.getAllEntities().get(i).enemyFound=false;
                    Wish.getAllEntities().get(i).enemyInAttackRange=false;
                }
            }
        }
        else if(animationStage==animationsArrayCount[EntityNums.DIE]-1){
            aliveFlag=false;
            Wish.getAllEntities().remove(this);
        }
    }

    //- - - - - - - - - - - - - -    RENDER ENTITY    - - - - - - - - - - - - - - -

    /**
     * Renders entity
     */
    public void renderIt(){
        repaint();
    }

    /**
     * Used to render skill visuals
     */
    public abstract void renderSkills();

    //- - - - - - - - - - - -     ACTIONS TO PERFORM    - - - - - - - - - - -

    /**
     * Tasks that entity is doing in a thread
     */
    abstract void tasks();

    abstract void setLevel(int level);

    /**
     * What should be done after Entity levels up
     */
    void levelUp(){
        level++;
        next_exp=(int)(next_exp * 1.25f * level);
        setLevel(level);
        new lvlUP();
    }


    private class lvlUP implements Runnable{
        private long usedAt;
        JPanel panel;
        lvlUP(){
            panel=new JPanel();
            panel.setSize(Entity.this.entityWidth,Settings.SIZE_UNIT);
            panel.setOpaque(false);
            panel.setLocation(
                    0,
                    (int)Entity.this.getHeight()/2);
            Font font= new Font(Font.SERIF,Font.BOLD,(int)(Settings.SIZE_UNIT*0.6));
            JLabel label=new JLabel("level UP!");
            label.setFont(font);
            label.setForeground(Color.WHITE);
            label.setOpaque(true);
            label.setBackground(new Color(0, 204, 255, 220));
            panel.add(label);
            Entity.this.add(panel);
            usedAt = System.currentTimeMillis();
            Thread.ofVirtual().start(this);
        }

        @Override
        public void run() {
            System.out.println("run first");
            while(true ){
                if(usedAt + 2000 < System.currentTimeMillis()){
                    Entity.this.remove(panel);
                    break;
                }
            }
        }
    }

    /**
     * Check if entity leveled up;
     */
    private void checkLevelUpdate(){
        if(current_exp>=next_exp){
            levelUp();
        }
    }

    //- - - - - - - - - -     THREAD METHODS    - - - - - - - - - - - - - - -

    /**
     * This is starting a thread for entity
     */
    public void runThread(){
        if(!tasksDone || !isAlive()) return;
        thread=Thread.ofVirtual().start(this);
    }

    /**
     * Separate thread loop for entity with checking
     * status of this entity
     */
    protected void entityLoop() {
        tasksDone = false;
        now = System.nanoTime();
        if(currentHealthPoints<=0) tryDie();
        else tasks();
        if (now - secondStarted >= (1000000000 * 0.25d)) {
            secondStarted = now;
            animationStage++;
            if (lastTimeInFight + 7000 <= System.currentTimeMillis() && !isTriggered) {
                enemyFound=false;
                enemyInAttackRange=false;
                if(!this.entity_type.equals("Player")) enemy=null;
                if (currentHealthPoints < maxHealthPoints) {
                    currentHealthPoints += healthRegen;
                    healthBar.setValue((int) currentHealthPoints);
                }else if (currentHealthPoints > maxHealthPoints) {
                    currentHealthPoints = maxHealthPoints;
                }
            }
            if (currentManaPoints < maxManaPoints) currentManaPoints += manaRegen;
            else if (currentManaPoints > maxManaPoints) currentManaPoints = maxManaPoints;
            if (animationsArrayCount[animationType] - 1 < animationStage)
                animationStage = 0;
        }
        updateHealthBar();
        checkLevelUpdate();
        tasksDone=true;
    }

    /**
     * Things that entity will do in a thread
     */
    @Override
    public void run() {
        entityLoop();
    }

    //- - - - - - - - - - - - -     Paint Component    - - - - - - - - - - - - - -

    /**
     * Responsible for rendering entity
     * @param g the <code>Graphics</code> object to protect
     */
    public void paintComponent(Graphics g) {
        try {
            g2d = (Graphics2D) g;
            if (directionX == -1)
                g2d.drawImage(animations.get(animationType).get(animationStage), this.entityWidth, 0, -this.entityWidth, this.entityHeight, null);
            else
                g2d.drawImage(animations.get(animationType).get(animationStage), 0, 0, this.entityWidth, this.entityHeight, null);
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
    }

    //- - - - - - - -     GETTERS AND SETTERS    - - - - - - - - - - - -
    public String getName(){return this.name;}

    /**
     * @return Current amount health points
     */
    public float currentHealthPoints() {return currentHealthPoints;}

    /**
     * @param hp set current health points as this parameter
     */
    public void currentHealthPoints(int hp){this.currentHealthPoints =hp;}

    /**
     * @return Maximum amount of health points of this entity
     */
    public float maxHealthPoints(){return maxHealthPoints;}

    /**
     * Sets maximum amount of health points
     * @param maxHp Number to be set as maximum amount of health points
     */
    public void maxHealthPoints(int maxHp){ this.maxHealthPoints = maxHp;}
    public float getCurrentManaPoints(){return currentManaPoints;}
    public float getMaxManaPoints(){return maxManaPoints;}
    public void decreaseManaPoints(float amount){currentManaPoints-=amount;}
    public void setImmortal(boolean bool){isImmortal=bool;}
    public boolean isImmortal(){return isImmortal;}

    public float getMovementSpeed(){return movementSpeed;}
    public float getBonusMovementSpeed(){return bonusMovementSpeed;}
    public void setMovementSpeedMultiplier(float speed){
        this.movementSpeedMultiplier=speed;
        updateSpeed();
    }

    public boolean isAttackonOnCD(){return basicAttackCD;}
    public float getBasicAttackDamage(){return basicAttackDamage;}
    public int getLevel() {return level;}
    public boolean isAlive() {return aliveFlag;}
    public Color getTeamColor() {return teamColor;}

    /**
     * Makes entity to be a part of a team
     * @param color Color of team to be part of
     */
    public void setTeamColor(Color color){
        healthBar.setValueColor(color);
        teamColor=color;}

    /**
     * Add other team to enemies list
     * @param color Color of enemy
     */
    public void addEnemyTeam(Color color){enemyTeams.add(color);}

    /**
     * Removes other team from enemies list
     * @param color Color of enemy
     */
    public void removeEnemyTeam(Color color){enemyTeams.remove(color);}

    /**
     * @return Returns current enemy
     */
    public Entity getEnemy(){return enemy;}

    /**
     * @return Current Y location
     */
    public float getLocY() {return loc_y;}

    /**
     * @return Current X location
     */
    public float getLocX() {return loc_x;}

    /**
     * This returns attack range of this entity
     * Range is screen size unit multiplied by
     * @return return base, not increased distance
     */
    public float getAttackRange() {return attackRange;}

    /**
     * Returns melee attack range.
     * 1 is normal range
     * 2 is doubled range, etc
     * @return attack range multiplier
     */
    public float attackRangeMultiplier(){return attackRangeMultiplier;}

    /**
     * This increases scale of attack range. Base attack
     * range is multiplied by parameter passed in this method.
     * For example:
     * attackRange(1) will make it normal distance
     * attackRange(2) will double attack range distance, etc.
     * @param multipliedAttackRange Base attack range to be multiplied by.
     */
    public void setAttackRange(float multipliedAttackRange){
        this.attackRangeMultiplier = multipliedAttackRange;
        attackRange = Settings.SIZE_UNIT * attackRangeMultiplier;
    }
    /**
     * Make entity bigger or smaller
     * @param scale Scale of size
     */
    public void setEntitySizeMultiplier(float scale){
        entitySizeMultiplier=scale;
        entityWidth =(int)(Settings.SIZE_UNIT*4*entitySizeMultiplier);
        entityHeight =(int)(Settings.SIZE_UNIT*4*entitySizeMultiplier);
        setSize(entityWidth,entityHeight);
    }

    public int getAnimationType(){return animationType;}
    public int getAnimationStage(){return animationStage;}
    public void setAnimationType(int type){animationType=type;}
    public void setAnimationStage(int stage){animationStage=stage;}

    public void increaseBonusDamage(float dmg){bonusDamage+=dmg;}
    public void decreaseBonusDamage(float dmg){bonusDamage-=dmg;}
    public float getBonusDamage(){return bonusDamage;}

    public void decreaseBonusArmor(float bonus){bonusArmorPoints-=bonus;}

    /**
     * Entity has two types of armor: standard and bonus.
     * this function sets bonus armor, which is independent
     * of standard one.
     * @param bonus Set bonus armor to be this value
     */
    public void increaseBonusArmor(float bonus){bonusArmorPoints += bonus;}
    public float getArmorPoints(){return armorPoints;}
    public float getBonusArmorPoints(){return bonusArmorPoints;}

    public void isPerformingAction(boolean bool){performingAction=bool;}
    public long getLastTimeInFight(){return lastTimeInFight;}

    /**
     * Set distance that this entity will search for enemy
     * @param distance integer that is multiplier of range distance
     */
    public void distanceSearchForEnemy(int distance){ lookForEnemyDistance = distance;}

    /**
     * @return distance that this entity is looking for enemy
     */
    public int distanceSearchForEnemy(){ return lookForEnemyDistance;}

    /**
     * Checks if entity is triggered, if true entity never leave combat
     * false means it will leave combat after certain time if did not receive damage
     * @return boolean if entity is triggered
     */
    public boolean isTriggered(){return isTriggered;}

    /**
     * Set entity to be triggered or not, if true entity never leave combat
     * if false it leaves combat after certain time if did not receive damage
     * @return boolean if entity is triggered
     */
    public void isTriggered(boolean isTriggered){this.isTriggered = isTriggered;}
    public boolean isPerformingAction(){return performingAction;}

    /**
     * @return amount of experience received after this entity is killed
     */
    public int expForKill(){return expForKill;}

    /**
     * Set amount experience that is given when this entity is killed
     * @param exp amount of experience
     */
    public void expForKill(int exp){this.expForKill=exp;}
}
