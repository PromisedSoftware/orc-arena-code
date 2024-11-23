package MainPackage;

import Entities.Player;
import LanguageStrings.Language;
import MyComponents.BJPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Pick player class at start of a new game
 */
public class PlayerClassPicker extends BJPanel implements MouseListener, MouseMotionListener {
    private boolean isDescriptionCreated=false;
    private PlayerClassPickerPanel maleWarriorPanel, maleMagePanel, maleAssassinPanel,topPanel;
    private PlayerClassPickerPanel femaleWarriorPanel, femaleMagePanel, femaleAssassinPanel;
    private JLabel descriptionLabel;

    private String warriorImgPath ="src\\Images\\"+Settings.RESOLUTION_HEIGHT +"\\Male_Warrior_Selection.jpg";
    private String mageImgPath ="src\\Images\\"+Settings.RESOLUTION_HEIGHT +"\\Male_Mage_Selection.jpg";
    private String assassinImgPath ="src\\Images\\"+Settings.RESOLUTION_HEIGHT +"\\Male_Assassin_Selection.jpg";
    private String femWarriorImgPath ="src\\Images\\"+Settings.RESOLUTION_HEIGHT +"\\Female_Warrior_Selection.jpg";
    private String femMageImgPath ="src\\Images\\"+Settings.RESOLUTION_HEIGHT +"\\Female_Mage_Selection.jpg";
    private String femAssassinImgPath ="src\\Images\\"+Settings.RESOLUTION_HEIGHT +"\\Female_Assassin_Selection.jpg";
    private String topPanelImagePath="src\\Images\\"+Settings.RESOLUTION_HEIGHT +"\\top_panel_Image.png";
    private String backgroundPath="src\\Images\\"+Settings.RESOLUTION_HEIGHT +"\\stone_wall.jpg";

    private int topPanelHeight =Settings.SIZE_UNIT*3;
    private int playerImageWidth =Settings.GAME_PANEL_WIDTH/3;
    private int playerImageHeight =Settings.GAME_PANEL_HEIGHT- topPanelHeight;

    private GamePanel gamePanel;

    //= = = = = = = = = = = =    CONSTRUCTOR     = = = = = = = = = = = = = =

    /**
     * Creates a game panel that contains playable character classes for player
     * and make it able to pick
     * @param gamePanel Game panel that contains every game object
     */
    public PlayerClassPicker(GamePanel gamePanel){
        super("PlayerClassPicker");
        Wish.putElementToArray(this);
        this.gamePanel=gamePanel;
        this.setOpaque(false);
        this.setLayout(null);
        this.setSize(new Dimension(
                Settings.GAME_PANEL_WIDTH,
                Settings.GAME_PANEL_HEIGHT));
        this.setLocation(0,0);
        this.setVisible(true);

        //- - - - - - - - - - - - - -Top Panel    * * * Constructor
        topPanel =new PlayerClassPickerPanel();
        topPanel.setLayout(null);
        topPanel.setSize(Settings.GAME_PANEL_WIDTH, topPanelHeight);
        topPanel.setLocation(0, 0);
        topPanel.setVisible(true);

        JPanel panelWithTextLabel=new JPanel();
        panelWithTextLabel.setLayout(new BorderLayout());
        panelWithTextLabel.setSize(Settings.GAME_PANEL_WIDTH,topPanelHeight);
        panelWithTextLabel.setLocation(0, 0);
        panelWithTextLabel.setOpaque(false);
        panelWithTextLabel.setVisible(true);

        JLabel topPanelText=new JLabel(Language.PlayerPicker.selectCharacter);
        topPanelText.setFont(new Font(Font.SANS_SERIF,Font.BOLD,Settings.SIZE_UNIT*2));
        topPanelText.setHorizontalAlignment(JLabel.CENTER);
        topPanelText.setVerticalAlignment(JLabel.CENTER);
        panelWithTextLabel.add(topPanelText,BorderLayout.CENTER);

        topPanel.add(panelWithTextLabel);
        this.add(topPanel);
        topPanel.setImage(0, topPanelHeight,topPanelImagePath,true);

        maleWarriorPanel =new PlayerClassPickerPanel();
        maleWarriorPanel.setSize(playerImageWidth, playerImageHeight);
        maleWarriorPanel.setImage(playerImageWidth, playerImageHeight, warriorImgPath,true);
        maleWarriorPanel.setLocation(0, topPanelHeight);
        maleWarriorPanel.addMouseListener(this);
        maleWarriorPanel.addMouseMotionListener(this);
        maleWarriorPanel.setVisible(true);
        this.add(maleWarriorPanel);

        femaleWarriorPanel =new PlayerClassPickerPanel();
        femaleWarriorPanel.setSize(playerImageWidth, playerImageHeight);
        femaleWarriorPanel.setImage(playerImageWidth, playerImageHeight, femWarriorImgPath,true);
        femaleWarriorPanel.addMouseListener(this);
        femaleWarriorPanel.setVisible(true);

        maleMagePanel =new PlayerClassPickerPanel();
        maleMagePanel.setSize(playerImageWidth, playerImageHeight);
        maleMagePanel.setImage(playerImageWidth, playerImageHeight, mageImgPath,true);
        maleMagePanel.setLocation(playerImageWidth, topPanelHeight);
        maleMagePanel.addMouseListener(this);
        maleMagePanel.addMouseMotionListener(this);
        maleMagePanel.setVisible(true);
        this.add(maleMagePanel);

        femaleMagePanel =new PlayerClassPickerPanel();
        femaleMagePanel.setSize(playerImageWidth, playerImageHeight);
        femaleMagePanel.setImage(playerImageWidth, playerImageHeight, femMageImgPath,true);
        femaleMagePanel.addMouseListener(this);
        femaleMagePanel.setVisible(true);

        maleAssassinPanel =new PlayerClassPickerPanel();
        maleAssassinPanel.setSize(playerImageWidth, playerImageHeight);
        maleAssassinPanel.setImage(playerImageWidth, playerImageHeight, assassinImgPath,true);
        maleAssassinPanel.setLocation(playerImageWidth *2, topPanelHeight);
        maleAssassinPanel.addMouseListener(this);
        maleAssassinPanel.addMouseMotionListener(this);
        maleAssassinPanel.setVisible(true);
        this.add(maleAssassinPanel);

        femaleAssassinPanel =new PlayerClassPickerPanel();
        femaleAssassinPanel.setSize(playerImageWidth, playerImageHeight);
        femaleAssassinPanel.setImage(playerImageWidth, playerImageHeight,femAssassinImgPath,true);
        femaleAssassinPanel.addMouseListener(this);
        femaleAssassinPanel.setVisible(true);
        this.setImage(backgroundPath,true);

    }

    //= = = = = = = = = = = = = = = = =    METHODS    = = = = = = = = = = = = = = = =

    /**
     * Create description for a specific class of player
     */
    public void createWarriorDescription(){
        maleWarriorPanel.setLabel(createLabelDescription(Language.PlayerPicker.warrior,1.2f),BorderLayout.CENTER);
        int middle=(playerImageHeight-maleWarriorPanel.getLabelSize().height)/2;
        maleWarriorPanel.setLabelLocation(0,middle);
    }

    /**
     * Create description for a specific class of player
     */
    public void createMageDescription(){
        maleMagePanel.setLabel(createLabelDescription(Language.PlayerPicker.mage,1.2f),BorderLayout.CENTER);
        int middle=(playerImageHeight-maleMagePanel.getLabelSize().height)/2;
        maleMagePanel.setLabelLocation(0,middle);
    }

    /**
     * Create description for a specific class of player
     */
    public void createAssassinDescription(){
        maleAssassinPanel.setLabel(createLabelDescription(Language.PlayerPicker.agent,1.2f),BorderLayout.CENTER);
        int middle=(playerImageHeight-maleAssassinPanel.getLabelSize().height)/2;
        maleAssassinPanel.setLabelLocation(0,middle);
    }

    /**
     * Removes description for a specific class of player
     */
    public void removeAssassinDescription(){
        if(maleAssassinPanel!=null) maleAssassinPanel.clearLabels();
        SwingUtilities.updateComponentTreeUI(this);
    }

    /**
     * Removes description for a specific class of player
     */
    public void removeWarriorDescription(){
        if(maleWarriorPanel!=null) maleWarriorPanel.clearLabels();
        SwingUtilities.updateComponentTreeUI(this);
    }

    /**
     * Removes description for a specific class of player
     */
    public void removeMageDescription(){
        if(maleMagePanel!=null) maleMagePanel.clearLabels();
        SwingUtilities.updateComponentTreeUI(this);
    }

    /**
     * Creates a new label with player class description
     * @param description String that should be displayed as description
     * @param fontSize Size of the font
     * @return label with description
     */
    private JLabel createLabelDescription(String description,float fontSize){
        Font characterDescriptionFont=new Font(Font.SANS_SERIF,Font.BOLD,(int)(Settings.SIZE_UNIT*fontSize));
        descriptionLabel= new JLabel(description, SwingConstants.CENTER);
        descriptionLabel.setFont(characterDescriptionFont);
        descriptionLabel.setOpaque(true);
        descriptionLabel.setVisible(true);
        descriptionLabel.setSize(playerImageWidth, playerImageHeight);
        descriptionLabel.setLocation(0,0);
        descriptionLabel.setBackground(new Color(0,0,0,125));
        descriptionLabel.setForeground(Color.WHITE);
        return descriptionLabel;
    }

    /**
     * Create a player of chosen class
     * and clears this panel
     */
    private void selectPlayerClass(String characterType){
        int location1=Settings.GAME_PANEL_WIDTH/2-playerImageWidth;
        int location2=Settings.GAME_PANEL_WIDTH/2;
        this.removeImages();
        switch(characterType){
            case "Warrior" ->{
                this.remove(maleMagePanel);
                this.remove(maleAssassinPanel);
                maleWarriorPanel.setLocation(location1, topPanelHeight);
                femaleWarriorPanel.setLocation(location2, topPanelHeight);
                this.add(femaleWarriorPanel);
                Player.playerClassType ="Warrior";
                removeWarriorDescription();
            }
            case "Mage" ->{
                this.remove(maleWarriorPanel);
                this.remove(maleAssassinPanel);
                maleMagePanel.setLocation(location1, topPanelHeight);
                femaleMagePanel.setLocation(location2, topPanelHeight);
                this.add(femaleMagePanel);
                Player.playerClassType ="Mage";
                removeMageDescription();
            }
            case "Agent" ->{
                this.remove(maleWarriorPanel);
                this.remove(maleMagePanel);
                maleAssassinPanel.setLocation(location1, topPanelHeight);
                femaleAssassinPanel.setLocation(location2, topPanelHeight);
                this.add(femaleAssassinPanel);
                Player.playerClassType ="Agent";
                removeAssassinDescription();
            }
        }
        this.setImage(backgroundPath,true);
    }

    //- - - - Overrides MouseListener * * * NewGameCharacterPicker Class

    /**
     * Make class to be able to click with mouse
     * @param e the event to be processed
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        //- - - - - - -    Warrior panel as source
        if(e.getSource()== maleWarriorPanel){
            if(Player.playerClassType.compareTo("")==0)  selectPlayerClass("Warrior");
            else if(e.getSource()==maleWarriorPanel) Player.playerGender="Male";
        }
        else if(e.getSource()==femaleWarriorPanel) Player.playerGender="Female";


        // - - - - - - -    Mage panel as source - - -
        else if(e.getSource()== maleMagePanel){
            if(Player.playerClassType.compareTo("")==0) selectPlayerClass("Mage");
            else if(e.getSource()==maleMagePanel) Player.playerGender="Male";
        }
        else if(e.getSource()==femaleMagePanel) Player.playerGender="Female";

        //- - - - - - -     Assassin panel as source - - -
        else if(e.getSource()== maleAssassinPanel){
            System.out.println("Currently unavailable");
            if(Player.playerClassType.compareTo("")==0) selectPlayerClass("Agent");
            else if(e.getSource()==maleAssassinPanel) Player.playerGender="Male";
        }
        else if(e.getSource()==femaleAssassinPanel) Player.playerGender="Female";

        if(!Player.playerGender.equals("")){
            this.removeImages();
            gamePanel.loadMap();
            Wish.addListenersToGame();
            gamePanel.remove(this);
            Wish.removePanel(this);
            Wish.startGameLoop();
        }
        SwingUtilities.updateComponentTreeUI(this);
    }
    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}

    /**
     * When mouse escape description disappears
     * @param e the event to be processed
     */
    @Override
    public void mouseExited(MouseEvent e) {
        isDescriptionCreated=false;
        if(e.getSource()==maleWarriorPanel){
            removeWarriorDescription();
        }
        else if(e.getSource()==maleMagePanel){
            removeMageDescription();
        }
        else if(e.getSource()==maleAssassinPanel){
            removeAssassinDescription();
        }
    }

    @Override
    public void updateElement() {}
    @Override
    public void updateLanguage() {}
    @Override
    public void mouseDragged(MouseEvent e) {mouseClicked(e);}

    /**
     * When mouse is moving it preventing description
     * from unexpected disappearing
     * @param e the event to be processed
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        if(!isDescriptionCreated){
            isDescriptionCreated=true;
            if(Player.playerClassType.compareTo("")==0){
                if(e.getSource()==maleWarriorPanel){
                    createWarriorDescription();
                }
                if(e.getSource()==maleMagePanel) {
                    createMageDescription();
                }
                if(e.getSource()==maleAssassinPanel) {
                    createAssassinDescription();
                }
                SwingUtilities.updateComponentTreeUI(this);
            }
        }
    }

    //= = = = = = = = = = = = = = = = =     CLASSES    = = = = = = = = = = = = = = = = =

    /**
     * Panels of choice for player class
     */
    public class PlayerClassPickerPanel extends BJPanel{
        /**
         * Create panel of choice for player class
         */
        public PlayerClassPickerPanel(){
            super("PlayerPickerPanel");
            this.setLayout(null);
        }
        @Override public void updateElement(){}
        @Override public void updateLanguage(){}}}
