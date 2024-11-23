package MainPackage;

import Entities.Entity;
import Entities.EntityNums;
import Entities.Player;
import Maps.Terrain;
import MyComponents.BJPanel;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Helpful tools to make this application easier
 */
public abstract class Wish {
    // - - - - - - - - - - - - - -    VARIABLES   - - - - - - - - - - - - - - - -
    private static MainWindow mainWindow;
    private static SettingsWindow settingsWindow;
    private static GamePanel gamePanel;
    private static ArrayList<BJPanel> panels=new ArrayList<>();
    private static ArrayList<Entity> entities=new ArrayList<>();
    private static Player player;
    private static Terrain terrain;
    private static ArrayList<Thread> threads=new ArrayList<>();
    private static Game game;

    //= = = = = = = = = = = = = = =    METHODS    = = = = = = = = = = = = = = = = = = = = = = =

    //- - - - - - - - - - - - - - -   UPDATE COMPONENTS    - - - - - - - - - - - - - - - - - - - -

    /**
     * Updates size of every component of this application
     */
    public static void updateResolution(){
        //- - - - - -    RESIZE PANELS    - - - - -
        for(BJPanel bjp:panels){
            bjp.updateElement();
        }
        if(gamePanel!=null) gamePanel.updateElement();

        //- - - - - -    RESIZE FRAMES    - - - - -
        if(mainWindow !=null) mainWindow.updateElement();
        if(settingsWindow!=null) settingsWindow.updateElement();
        updateTerrainTextures();
        updateActorTextures();
        EntityNums.currentWidthPanel=Settings.GAME_PANEL_WIDTH;
        EntityNums.currentHeightPanel=Settings.GAME_PANEL_HEIGHT;

    }

    //- - - - - - - -   UPDATE ACTOR TEXTURES - - - - - - - - - - -

    /**
     * Updates every actor quality and size
     */
    public static void updateActorTextures(){
        for(Entity e:entities) e.updateIt();
    }

    //- - - - - - - - - - -    ACTORS SPEED    - - - - - - - - - - - -

    /**
     * Update every actor move speed
     */
    public static void updateSpeedActors(){
        for(Entity e:entities) e.updateSpeed();
    }

    //- - - - - - - - -  UPDATE LANGUAGE - - - - - - - - - - - - - -

    /**
     * Update language of every component
     */
    public static void updateLanguage(){
        for(BJPanel p:panels) p.updateLanguage();
    }

    //- - - - - - - - - - - - -    UPDATE TERRAIN TEXTURES    - - - - - - - - -

    /**
     * Update every terrain component
     */
    public static void updateTerrainTextures(){
        if(terrain!=null){terrain.updateElement();}
    }

    /**
     * render every terrain
     */
    public static void renderTerrain(){
        if(terrain!=null) terrain.repaint();
    }

    //- - - - - - - - - -    START GAME LOOP    - - - - - - - - - -

    /**
     * Start game loop
     */
    public static void startGameLoop(){
        if(game!=null)game.startGameLoop();
    }

    //- - - - - - - - - - -    EXIT GAME    - - - - - - - -

    /**
     * Prepare everything for this application to close.
     * kill every thread, dispose windows etc.
     */
    public static void exit(){
        try{
            settingsWindow.dispose();
        }catch(Exception e){System.out.println("Wish.exit() option window");}

        try{
            mainWindow.dispose();
        }catch(Exception e){System.out.println("Wish.exit()  main window");}
        Game.setGameON(false);
        System.exit(0);
    }

    //- - - - - - - - - - -    ADD LISTENERS    - - - - - - - - -- - - -

    /**
     * Add game listeners to game panel
     */
    public static void addListenersToGame(){
        gamePanel.addListeners();
    }

    //- - - - - - - - - - - -    GETTERS AND SETTERS    - - - - - - - - - - - -
    public static void setWindowAsElement(MainWindow frame){mainWindow =frame;}
    public static void setWindowAsElement(SettingsWindow frame){
        settingsWindow=frame;
    }
    public static void putElementToArray(BJPanel panel){if(!panels.contains(panel)) panels.add(panel);}
    public static void putElementToArray(GamePanel panel){
        gamePanel=panel;
    }
    public static void playerToArray(Player PLAYER){player=PLAYER;}
    public static void putElementToArray(Entity entity){if(!entities.contains(entity)) entities.add(entity);}
    public static void putElementToArray(Thread THREAD){if(!threads.contains(THREAD)) threads.add(THREAD);}
    public static void putElementToArray(Game gameSource){game=gameSource;}
    public static void putElementToArray(Terrain addTerrain){terrain=addTerrain;}

    public static MainWindow getMainWindow(){return mainWindow;}
    public static SettingsWindow getSettingsWindow(){
        return settingsWindow;
    }
    public static ArrayList<Entity> getAllEntities(){return entities;}
    public static Player getPlayer(){return player;}
    public static Terrain getTerrain(){return terrain;}

    public static void removePanel(BJPanel bjpanel){
        panels.remove(bjpanel);

    }
}