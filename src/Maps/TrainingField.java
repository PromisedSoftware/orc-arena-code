package Maps;

import Entities.Entity;
import Entities.Orc;
import MainPackage.Wish;

import java.util.Random;

/**
 * Extends from Terrain, one of maps
 */
public class TrainingField extends Terrain{

    /**
     * Create map
     */
    public TrainingField(){
        super("Practice_Field");
    }

    /**
     * Tasks that are performed, basic existential entities
     */
    @Override
    protected void tasks() {
        for(Entity e: entities){
            if(!e.isAlive()){
                e.removeMouseListener(e);
                this.remove(e);
            }
        }
        for(int i=0; i < entities.size(); i++)
            if(!entities.get(i).isAlive())
                entities.remove(entities.get(i));
    }

    private long lastTimeSpawned=0;
    private int entitiesSpawned =0;
    private Orc boss;
    private int spawnEvery=5000;
    protected void secondaryTasks() {

        if(boss != null){
            if(!boss.isAlive()) boss=null;
            spawnEvery=10_000;
        } else spawnEvery=5000;

        if(lastTimeSpawned + spawnEvery<= System.currentTimeMillis()){
            entitiesSpawned++;
            lastTimeSpawned=System.currentTimeMillis();
            if(entitiesSpawned % 10==0 && boss==null){
                this.add(makeBossOrc(),(Integer)120);
            }
            else{
                this.add(makeCommonOrc(),(Integer)110);
            }
        }
    }

    private Orc makeBossOrc(){
        int playerLevel =Wish.getPlayer().getLevel();
        Orc bossOrc = new Orc(10,10,playerLevel);
        bossOrc.setEntitySizeMultiplier(3F);
        float scaleSpeedPercent = (100-playerLevel)/100F;
        bossOrc.setMovementSpeedMultiplier(40F * scaleSpeedPercent);
        bossOrc.isTriggered(true);
        bossOrc.enemyFound=true;
        bossOrc.enemy=Wish.getPlayer();
        bossOrc.maxHealthPoints( playerLevel * 100);
        bossOrc.currentHealthPoints(playerLevel * 100);
        bossOrc.increaseBonusArmor(playerLevel * 35);
        bossOrc.setAttackRange(3.5F);
        bossOrc.attackRangeMultiplier();
        bossOrc.expForKill(playerLevel * 200);
        entities.add(bossOrc);
        boss=bossOrc;
        bossOrc.distanceSearchForEnemy(1000);
        return bossOrc;
    }

    private Orc makeCommonOrc(){
        Orc orc= new Orc(
                new Random().nextInt(90) +5,
                new Random().nextInt(90) +5,
                new Random().nextInt(Wish.getPlayer().getLevel()) +1
        );
        orc.isTriggered(false);
        entities.add(orc);
        return orc;
    }
}
