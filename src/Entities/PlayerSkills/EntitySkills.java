package Entities.PlayerSkills;
import Entities.Player;
import MainPackage.Settings;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * Basic skills to be implemented
 */
public abstract class EntitySkills {

    public long Q_lastTimeUsed;
    public static long Q_skillCD;
    long Q_duration;
    public boolean Q_isActive;
    public boolean Q_CD_wearOff;
    public float Q_manaCost;

    public long W_lastTimeUsed;
    public static long W_skillCD;
    long W_duration;
    public boolean W_isActive;
    public boolean W_CD_wearOff;
    public float W_manaCost;

    public long E_lastTimeUsed;
    public static long E_skillCD;
    long E_duration;
    public boolean E_isActive;
    public boolean E_CD_wearOff;
    public float E_manaCost;

    protected Player player;
    public EntitySkills(Player player){
        this.player=player;
    }

    public abstract void castQ();
    public abstract void castW();
    public abstract void castE();

    public abstract String getDescription_Q();
    public abstract String getDescription_W();
    public abstract String getDescription_E();

    /**
     * Method is called in the virtual thread of the Entity
     * object. Implementation should check ability
     * to use skills and check skills timers
     */
    public abstract void checkSkills();

    /**
     * Method is called in rendering thread
     * It's responsible for rendering component using paint component method
     * @see Graphics2D
     */
    public abstract void render();


    /**
     * @return Current time of skill is yet blocked from using
     */
    public long getQ_skillCD(){
        return Q_lastTimeUsed+Q_skillCD>System.currentTimeMillis()?
                Q_lastTimeUsed+Q_skillCD-System.currentTimeMillis():0;
    }

    /**
     * @return Current time of skill is yet blocked from using
     */
    public long getW_skillCD(){
        return W_lastTimeUsed+W_skillCD>System.currentTimeMillis()?
                W_lastTimeUsed+W_skillCD-System.currentTimeMillis():0;
    }

    /**
     * @return Current time of skill is yet blocked from using
     */
    public long getE_skillCD(){
        return E_lastTimeUsed+E_skillCD>System.currentTimeMillis()?
                E_lastTimeUsed+E_skillCD-System.currentTimeMillis():0;
    }
}
