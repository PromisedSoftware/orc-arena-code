package MainPackage;
import HUD.HUD;
import Inputs.KeyboardInputs;
import Inputs.MouseInputs;
import Maps.Terrain;
import Maps.TrainingField;

import javax.swing.*;
import java.awt.*;

/**
 * Main game panel that every game object is displayed
 */
public class GamePanel extends JLayeredPane {

    public static boolean menuShowed=false;
    private TrainingField terrain;
    private HUD hud;


    /**
     * Creates new main game panel
     */
    public GamePanel(){
        Wish.putElementToArray(this);
        this.setOpaque(false);
        this.setLayout(null);
        this.setPreferredSize(new Dimension(
                Settings.GAME_PANEL_WIDTH,
                Settings.GAME_PANEL_HEIGHT
        ));
        hud=new HUD();
    }
    //= = = = = = = = = = = = = = = = = = = =    METHODS    = = = = = = = = = = = = = = = = = = = = = = = = =

    /**
     * Creates panel with classes of player that can be played
     */
    public void playerClassSelector(){
        PlayerClassPicker selector=new PlayerClassPicker(this);
        this.add(selector);
    }

    /**
     * Adds listeners to make mouse and keyboard work
     */
    public void addListeners(){
        terrain.addKeyListener(
                new KeyboardInputs(
                        terrain.getPlayer(),
                        hud));
        terrain.addMouseListener(new MouseInputs(terrain.getPlayer()));
        terrain.addMouseMotionListener(new MouseInputs(terrain.getPlayer()));
        terrain.requestFocus();

        hud.addPlayer(terrain.getPlayer());

        this.add(hud,(Integer)2);
    }

    /**
     * Load map that is requested
     */
    public void loadMap(){
        if(terrain!=null) this.remove(terrain);

        terrain=new TrainingField();
        terrain.setHUD(hud);
        this.add(terrain,(Integer)1);

        SwingUtilities.updateComponentTreeUI(this);
    }

    /**
     * Runs thread for terrain
     */
    public void runTerrainThread(){
        if(terrain!=null) terrain.runThread();
    }

    //- - - - - - -    Getters and Setters - - - - - - - -

    /**
     * Displays current FPS on a counter
     * @param fps amount to be showed on counter
     */
    public void setCounter_FPS(int fps){
        hud.setCounter_FPS(fps);
    }

    /**
     * Update size of panel
     */
    public void updateElement() {
        this.setPreferredSize(new Dimension(
                Settings.GAME_PANEL_WIDTH,
                Settings.GAME_PANEL_HEIGHT));
    }
}
