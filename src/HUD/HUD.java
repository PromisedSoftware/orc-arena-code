package HUD;

import Entities.Player;
import MainPackage.Settings;
import MainPackage.Wish;
import MyComponents.BJPanel;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

/**
 * HUD of the game
 */
public class HUD extends BJPanel {

    private Player player;
    PlayerAttributesBar pab;
    PlayerBar playerBar;
    FPSCounter fpsCounter;
    JTextArea description;
    Font font;

    /**
     * Creates HUD of the game
     */
    public HUD(){
        super("HUD");
        Wish.putElementToArray(this);
        setLayout(null);
        setBounds(0,0, Settings.GAME_PANEL_WIDTH,Settings.GAME_PANEL_HEIGHT);
        setBackground(Color.MAGENTA);
        setOpaque(false);
        setVisible(true);
        fpsCounter=new FPSCounter();
        add(fpsCounter);

        description=new JTextArea(1,12);
        font=new Font(Font.SANS_SERIF,Font.BOLD,Settings.SIZE_UNIT/2);
        description.setFont(font);
        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        description.setVisible(false);
        add(description);

    }

    /**
     * Triggers player to use spell
     * @param skill letter assigned to skill 'q', 'w', 'e'
     */
    public void castSpell(char skill){
        switch(skill){
            case 'q'->{playerBar.castSpellQ();}
            case 'w'->{playerBar.castSpellW();}
            case 'e'->{playerBar.castSpellE();}
        }
    }

    /**
     * Display amount of frames on FPS counter
     * @param fps amount of FPS to be set on the counter
     */
    public void setCounter_FPS(int fps){
        fpsCounter.setCounter_FPS(fps);
    }

    /**
     * Adds Player to HUD to read data from
     * player instance and make it able to use skills
     * @param player Player object
     */
    public void addPlayer(Player player){
        this.player=player;
        if(player!=null){
            playerBar=new PlayerBar(player,this);
            pab=new PlayerAttributesBar(player);
            add(playerBar);
            add(pab);
        }
        add(player);
    }

    /**
     * Update player bar to match his current status
     */
    public void updatePlayerBar(){
        if(playerBar!=null) {
            playerBar.updateHealthBar();
            playerBar.updateManaBar();
            playerBar.updateCDs();
            pab.updateAttributesInfo();
        }
    }

    /**
     * Displays description to be displayed on HUD
     * @param text Text to be displayed on HUD
     */
    public void setDescription(String text){
        this.setLayout(new FlowLayout());
        description.setText(text);
        Dimension size=description.getPreferredSize();
        description.setSize(size);
        size=description.getPreferredSize();
        description.setSize(size);
        description.setVisible(true);
        this.setLayout(null);
    }

    /**
     * Sets location where description should be placed
     * @param x X location on HUD
     * @param y Y location on HUD
     */
    public void setDescriptionLocation(int x,int y){
        description.setLocation(x,y);
    }

    /**
     *
     * @return Dimension of description
     */
    public Dimension getDescriptionSize(){return description.getSize();}

    /**
     * Removes currently displayed description
     */
    public void removeDescription(){
        description.setVisible(false);
    }

    /**
     * Updates HUD language
     */
    @Override
    public void updateLanguage() {
    }

    /**
     * Updates HUD
     */
    @Override
    public void updateElement() {
        font=new Font(Font.SANS_SERIF,Font.BOLD,Settings.SIZE_UNIT/2);
        description.setFont(font);
        this.setBounds(0,0, Settings.GAME_PANEL_WIDTH,Settings.GAME_PANEL_HEIGHT);
        try{
            fpsCounter.updateIt();
            playerBar.updateIt();
            pab.updateIt();
        }catch(Exception e){
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }
}
