package Maps;

import Entities.Entity;
import Entities.EntityNums;
import Entities.Orc;
import Entities.Player;
import MainPackage.*;
import HUD.HUD;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Terrain class with terrain mechanisms and events
 */
public abstract class Terrain extends JLayeredPane implements Runnable{

    private String terrainName;
    private BufferedImage img;
    private Graphics2D g2d;
    private Player player;
    protected Thread thread;
    protected HUD hud;

    protected ArrayList<Entity> entities;

    private Terrain(){} //DEFAULT DISABLED

    /**
     * Creates a terrain
     * @param name String name of terrain
     */
    public Terrain(String name){
        Wish.putElementToArray(this);
        entities=new ArrayList<>();
        terrainName=name;
        EntityNums.currentWidthPanel=Settings.GAME_PANEL_WIDTH;
        EntityNums.currentHeightPanel=Settings.GAME_PANEL_HEIGHT;
        generateMap(name);
        this.setOpaque(true);
        this.setBounds(0,0,(int)Settings.GAME_PANEL_WIDTH,(int)Settings.GAME_PANEL_HEIGHT);
        this.setVisible(true);
        addPlayer();
    }

    //= = = = = = = = = = = = = = = = = = = =    METHODS    = = = = = = = = = = = = = = = = = = = = = = = = =

    /**
     * Add player to the terrain
     */
    private void addPlayer(){
        if(player==null) player=new Player(30,30,1);
        this.add(player,(Integer)100);
    }

    /**
     * Sets terrain appearance
     * @param name
     */
    private void setTerrainTexture(String name){
        String filePath="src\\Images\\Map\\"+Settings.TERRAIN_TEXTURE_QUALITY +"\\"+ name +".jpg";
        try{
            img=ImageIO.read(new File(filePath));
        }catch(Exception e){System.out.println("Image not found Terrain.setTerrainTexture()");}
    }

    /**
     * Renders terrain on the screen
     * @param g the <code>Graphics</code> object to protect
     */
    public void paintComponent(Graphics g){
        g2d=(Graphics2D) g;
        try{
            g2d.drawImage(img,0,0,Settings.GAME_PANEL_WIDTH,Settings.GAME_PANEL_HEIGHT,null);
        }catch(Exception e){
            for(StackTraceElement el: e.getStackTrace())
                System.out.println(el);
        }
    }

    /**
     * Generate given terrain
     * @param name String name of a terrain
     */
    private void generateMap(String name){
        switch (name){
            case "Practice_Field"->{
                setTerrainTexture("Practice_Field");
            }
        }
    }

    /**
     * Update terrain
     */
    public void updateElement() {
        this.setBounds(0,0,(int)Settings.GAME_PANEL_WIDTH,(int)Settings.GAME_PANEL_HEIGHT);
        setTerrainTexture(terrainName);
        repaint();
    }

    /**
     *
     * @return player object
     */
    public Player getPlayer(){
        return player;
    }

    /**
     * Why is it here??
     * @param hud HUD instance
     */
    public void setHUD(HUD hud){
        this.hud=hud;
    }

    /**
     * Tasks that will be to do
     */
    protected abstract void tasks();

    /**
     * Less important tasks that will be to do
     */
    protected abstract void secondaryTasks();


    /**
     * Run a thread of this terrain
     */
    public void runThread(){
        thread=new Thread(this);
        thread.start();
    }

    /**
     * Terrain events loop
     */
    private void terrainLoop() {
        try {
            hud.updatePlayerBar();
        } catch (Exception e) {
            System.out.println("Maps.Terrain Exception");
        }
        tasks();
        secondaryTasks();
    }

    /**
     * Terrain run thread
     */
    @Override
    public void run() {
        terrainLoop();
    }
}
