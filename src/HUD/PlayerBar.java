package HUD;

import Entities.Player;
import MainPackage.Settings;
import MyComponents.ProgressBar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * Contains skills information, heath, mana, etc.
 */
public class PlayerBar extends JPanel implements MouseListener {

    ArrayList<SkillPanel> skillPanels;
    Player player;
    HUD HUD;
    private PlayerBar playerBar;
    private ProgressBar healthBar;
    private ProgressBar manaBar;
    private HealthManaBar HandM;

    /**
     * Creates player bar stat storage
     * information about skills, health, mana and attributes
     * @param player Player instance
     * @param hud HUD instance
     */
    PlayerBar(Player player,HUD hud){
        this.player=player;
        this.HUD=hud;
        playerBar=this;
        setLayout(null);
        setOpaque(false);

        skillPanels=new ArrayList<>();
        HandM=new HealthManaBar();
        this.add(HandM);
        updateIt();
        for(int i=0;i<3;i++){
            skillPanels.add(new SkillPanel(i));
            skillPanels.get(i).addMouseListener(this);
            this.add(skillPanels.get(i));
        }
        HandM.setLocation(0, skillPanels.get(0).getY()+skillPanels.get(0).getHeight()
        );
        HandM.setBackground(Color.red);
    }
    //= = = = = = = = = = = =     METHODS    = = = = = = = = = = = = = =

    /**
     * Updates status of current health of the player
     * that is displayed as a red bar on HUD player bar
     */
    void updateHealthBar(){
        healthBar.setMaxValue((int)player.maxHealthPoints());
        healthBar.setValue((int)player.currentHealthPoints());
        float CHP=(float)Math.round(player.currentHealthPoints()*100)/100;
        float MHP=(float)Math.round(player.maxHealthPoints()*100)/100;
        healthBar.setText(CHP+" / "+MHP);
        SwingUtilities.updateComponentTreeUI(healthBar);
    }

    /**
     * Updates status of current mana of the player
     * that is displayed as a blue bar on HUD player bar
     */
    void updateManaBar(){
        manaBar.setMaxValue((int)player.getMaxManaPoints());
        manaBar.setValue((int)player.getCurrentManaPoints());
        float CMP=(float)Math.round(player.getCurrentManaPoints()*100)/100;
        float MHP=(float)Math.round(player.getMaxManaPoints()*100)/100;
        manaBar.setText(CMP+" / "+MHP);
        SwingUtilities.updateComponentTreeUI(manaBar);
    }

    /**
     * Displays current cooldown of the skills on this bar
     */
    void updateCDs(){
        skillPanels.get(0).setTime(player.Q_getCD());
        skillPanels.get(1).setTime(player.W_getCD());
        skillPanels.get(2).setTime(player.E_getCD());
    }

    /**
     * Updates size and location of this bar
     */
    void updateIt() {
        this.setSize((int) (Settings.SIZE_UNIT*9), (int) (Settings.SIZE_UNIT*4));
        this.setLocation(
                (int) (Settings.GAME_PANEL_WIDTH/2)-getWidth(),
                (int) (Settings.GAME_PANEL_HEIGHT - this.getHeight()));
        for(SkillPanel sp:skillPanels){
            sp.updateIt();
        }
        HandM.updateIt();
        if(!skillPanels.isEmpty())HandM.setLocation(
                0,skillPanels.get(0).getY()+skillPanels.get(0).getHeight());
    }

    //- - - - - - - - - - - - -     CAST SPELLS   - - - - - - - - - - - - - - -

    /**
     * Triggers player to cast spell
     */
    void castSpellQ(){
        player.castSpellQ();
    }

    /**
     * Triggers player to cast spell
     */
    void castSpellW(){
        player.castSpellW();
    }

    /**
     * Triggers player to cast spell
     */
    void castSpellE(){
        player.castSpellE();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    /**
     * When cursor is over image of the skill it displays information
     * about that skill
     * @param e the event to be processed
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        if(e.getSource()==skillPanels.get(0)){
            HUD.setDescription(player.Q_getSkillDescription());
            int x=skillPanels.get(0).onPanelX;
            int y=skillPanels.get(0).onPanelY-(int)HUD.getDescriptionSize().getHeight();
            HUD.setDescriptionLocation(x,y);
        }

        else if(e.getSource()==skillPanels.get(1)){
            HUD.setDescription(player.W_getSkillDescription());
            int x=skillPanels.get(1).onPanelX;
            int y=skillPanels.get(1).onPanelY-(int)HUD.getDescriptionSize().getHeight();
            HUD.setDescriptionLocation(x,y);
        }

        else if(e.getSource()==skillPanels.get(2)){
            HUD.setDescription(player.E_getSkillDescription());
            int x=skillPanels.get(2).onPanelX;
            int y=skillPanels.get(2).onPanelY-(int)HUD.getDescriptionSize().getHeight();
            HUD.setDescriptionLocation(x,y);
        }

    }

    /**
     * When cursor is moving away from skill icon it removes information
     * that is currently displayed on HUD
     * @param e the event to be processed
     */
    @Override
    public void mouseExited(MouseEvent e) {
        HUD.removeDescription();
    }


    //= = = = = = = = = = = = = = =    CLASSES     = = = = = = = = = = = = =

    /**
     * Panel containing skills icons
     */
    private class SkillPanel extends JLayeredPane {
        boolean isCooling=false;
        int onPanelX;
        int onPanelY;
        private int skillNumber;
        private String iconPath;
        private JLabel timeLabel,imageLabel;
        private Font font;
        private JPanel separator;

        /**
         * Creating panel containing skills set
         * @param skillNumber number of that skill
         */
        SkillPanel(int skillNumber){
            this.skillNumber = skillNumber;
            timeLabel=new JLabel();
            imageLabel=new JLabel();
            separator=new JPanel();

            separator.setOpaque(true);
            separator.setBackground(new Color(0,0,0,90));
            separator.setVisible(false);

            timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
            timeLabel.setVerticalAlignment(SwingConstants.CENTER);

            setLayout(null);
            updateIt();
            add(imageLabel,(Integer)1);
            add(separator,(Integer)2);
            add(timeLabel,(Integer)3);
            timeLabel.setForeground(Color.WHITE);
        }

        /**
         * Updates size and location on HUD
         * of this panel
         */
        void updateIt() {
            setSize(Settings.SIZE_UNIT*2,Settings.SIZE_UNIT*2);
            setLocation(Settings.SIZE_UNIT/2+skillNumber*2*(Settings.SIZE_UNIT+Settings.SIZE_UNIT/2),0);
            iconPath="src\\Images\\PlayerSkills\\Icons\\"+
                            Settings.RESOLUTION_HEIGHT+"\\"+Player.playerClassType+"_"+skillNumber+".jpg";
            onPanelX=(playerBar.getX()+this.getX());
            onPanelY=(playerBar.getY()+this.getY());

            font=new Font(Font.SANS_SERIF,Font.BOLD,(int)(Settings.SIZE_UNIT));
            imageLabel.setBounds(0,0,this.getWidth(),this.getHeight());
            imageLabel.setIcon(new ImageIcon(iconPath));
            timeLabel.setBounds(0,0,this.getWidth(),this.getHeight());
            timeLabel.setFont(font);
            separator.setBounds(0,0,this.getWidth(),this.getHeight());
        }

        /**
         * Set cooldown time on skill
         * @param time
         */
        void setTime(long time){
            if(time>20){
                float t=(float)time/1000;
                String s=""+(float)Math.round(t*10)/10;
                timeLabel.setText(s);
                separator.setVisible(true);
            }
            else{
                timeLabel.setText("");
                separator.setVisible(false);
                isCooling=false;
            }
        }
    }

    /**
     * Mana bar
     */
    private class HealthManaBar extends JPanel{

        /**
         * Creates mana and health bar, make it able to set values
         */
        HealthManaBar(){
            this.setSize(playerBar.getWidth(),Settings.SIZE_UNIT*2);
            this.setOpaque(false);
            this.setLayout(null);
            healthBar=new ProgressBar();
            healthBar.setValueColor(new Color(67, 250, 0));
            healthBar.setBarColor(new Color(0, 0,0, 90));
            healthBar.setMaxValue((int)player.maxHealthPoints());
            healthBar.setValue((int)player.currentHealthPoints());
            healthBar.setSize(playerBar.getWidth(),Settings.SIZE_UNIT);
            healthBar.setLocation(0,0);
            healthBar.setTextColor(Color.WHITE);

            manaBar=new ProgressBar();
            manaBar.setValueColor(new Color(0, 141, 255));
            manaBar.setBarColor(new Color(0, 0,0, 90));
            manaBar.setMaxValue((int)player.getMaxManaPoints());
            manaBar.setValue((int)player.getCurrentManaPoints());
            manaBar.setSize(playerBar.getWidth(),Settings.SIZE_UNIT);
            manaBar.setLocation(0,healthBar.getHeight());
            manaBar.setTextColor(Color.WHITE);

            this.add(healthBar);
            this.add(manaBar);
        }

        /**
         * Updates size and location
         */
        public void updateIt(){
            this.setBounds(0,0,playerBar.getWidth(),Settings.SIZE_UNIT*2);

            healthBar.setSize(playerBar.getWidth(),Settings.SIZE_UNIT);
            healthBar.setLocation(0,0);

            manaBar.setSize(playerBar.getWidth(),Settings.SIZE_UNIT);
            manaBar.setLocation(0,Settings.SIZE_UNIT);
        }
    }
}
