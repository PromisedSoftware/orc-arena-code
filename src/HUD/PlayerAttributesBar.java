package HUD;

import Entities.Player;
import MainPackage.Settings;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Class with attributes of player to be displayed on HUD
 */
public class PlayerAttributesBar extends JPanel {

    Player player;
    ArrayList<PAB_subPanels> attributePanels=new ArrayList<>();;

    /**
     * Creates bar with current player attributes
     * @param player Player instance
     */
    PlayerAttributesBar(Player player){
        this.player=player;
        setBackground(Color.BLACK);
        setLayout(null);
        attributePanels.add(new PAB_subPanels("attack"));
        attributePanels.add(new PAB_subPanels("armor"));
        attributePanels.add(new PAB_subPanels("move"));
        for(PAB_subPanels p:attributePanels) add(p);
        updateIt();
    }

    /**
     * Updates location and size of attributes bar
     */
    public void updateIt(){
        setSize(Settings.SIZE_UNIT*12,Settings.SIZE_UNIT*2);
        this.setLocation(
                (int) (Settings.GAME_PANEL_WIDTH/2),
                (int) (Settings.GAME_PANEL_HEIGHT - this.getHeight()));
        for(PAB_subPanels p:attributePanels) p.updateIt();
    }

    /**
     * Updates attributes info about player
     */
    public void updateAttributesInfo(){
        for (PAB_subPanels p:attributePanels) p.setAttributesInfo();
    }

    /**
     * Smaller panel inside attribute bar with
     * specific information about player
     */
    private class PAB_subPanels extends JPanel{
        Font font;
        JLabel iconLabel;
        JLabel info;
        String type;

        /**
         * Creates smaller panel with specific information
         * about player
         * @param type Type of information:
         *             "attack", "armor", "move"
         */
        PAB_subPanels(String type){
            this.type=type;
            iconLabel =new JLabel();
            info=new JLabel();
            info.setForeground(Color.WHITE);
            info.setLayout(null);
            setLayout(null);
            setOpaque(false);
            add(info);
            add(iconLabel);
            updateIt();
        }

        /**
         * Updates size and location of this panel
         */
        void updateIt(){
            this.setSize(Settings.SIZE_UNIT*6,Settings.SIZE_UNIT);
            String iconPath="";
            switch(type){
                case "attack"->{
                    this.setLocation(0,0);
                    iconPath="src\\Images\\"+Settings.RESOLUTION_HEIGHT +"\\Attack.jpg";
                }
                case "armor"->{
                    this.setLocation(0,Settings.SIZE_UNIT);
                    iconPath="src\\Images\\"+Settings.RESOLUTION_HEIGHT +"\\Armor.jpg";
                }
                case "move"->{
                    this.setLocation(Settings.SIZE_UNIT*6,0);
                    iconPath="src\\Images\\"+Settings.RESOLUTION_HEIGHT +"\\Move.jpg";
                }
            }
            iconLabel.setSize(Settings.SIZE_UNIT,Settings.SIZE_UNIT);
            iconLabel.setLocation(0,0);

            font=new Font(Font.SANS_SERIF,Font.BOLD,(int)(Settings.SIZE_UNIT*0.5));
            info.setSize(Settings.SIZE_UNIT*4,Settings.SIZE_UNIT);
            info.setLocation(Settings.SIZE_UNIT,0);
            info.setFont(font);
            info.setHorizontalAlignment(SwingConstants.CENTER);
            info.setVerticalAlignment(SwingConstants.CENTER);

            ImageIcon imageIcon = new ImageIcon(iconPath);
            iconLabel.setIcon(imageIcon);
        }

        /**
         * Updates information about player
         */
        void setAttributesInfo(){
            switch(type){
                case "attack"->{info.setText(player.getBasicAttackDamage()+" bonus :"+player.getBonusDamage());}
                case "armor"->{ info.setText(player.getArmorPoints()+" bonus :"+player.getBonusArmorPoints());}
                case "move"->{info.setText(player.getMovementSpeed()+" bonus: "+player.getBonusMovementSpeed());}
            }
        }
    }
}
