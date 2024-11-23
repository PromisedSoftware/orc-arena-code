package MainPackage;

import LanguageStrings.Language;
import MyComponents.BJPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Main menu panel with buttons
 */
public class MainMenuPanel extends BJPanel implements ActionListener {
    private JPanel centerPanel,bottomPanel;
    private GamePanel gamePanel;
    private Font buttonsFont;
    private String filePath;
    private MainWindow mainWindow;
    private ArrayList<JButton> buttons =new ArrayList<>();

    //= = = = = = = = = = = = = = = = = = = =    v CONSTRUCTOR v    = = = = = = = = = = = = = = = = = = =

    /**
     * Creates main menu panel
     * @param mainWindow Window that is created
     * @param gamePanel game panel with game objects
     */
    public MainMenuPanel(MainWindow mainWindow,GamePanel gamePanel){
        super("MainMenuPanel");
        this.gamePanel=gamePanel;
        Wish.putElementToArray(this);
        this.mainWindow = mainWindow;
        this.setOpaque(true);
        this.setLayout(null);
        this.setPreferredSize(new Dimension(
                (int) Settings.GAME_PANEL_WIDTH,
                (int) Settings.GAME_PANEL_HEIGHT
        ));

//- - - - - - - - - - - - - - - - - -     inner panels setup     * * * Constructor
        centerPanel=new JPanel();
        bottomPanel=new JPanel();

        centerPanel.setOpaque(false);
        centerPanel.setVisible(true);
        centerPanel.setLayout(new BorderLayout());
        centerPanel.setSize(
                (int)(Settings.GAME_PANEL_WIDTH*0.3),
                (int)(Settings.GAME_PANEL_HEIGHT*0.3)
        );
        centerPanel.setLocation(
                (Settings.GAME_PANEL_WIDTH-this.centerPanel.getWidth())/2,
                (Settings.GAME_PANEL_HEIGHT-this.centerPanel.getHeight())/2
        );

        bottomPanel.setOpaque(false);
        bottomPanel.setVisible(true);
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.setSize(
                (int)(Settings.GAME_PANEL_WIDTH*0.3),
                (int)(Settings.GAME_PANEL_HEIGHT*0.09)
        );
        bottomPanel.setLocation(
                (int)centerPanel.getLocation().getX(),
                (int)centerPanel.getLocation().getY()+centerPanel.getHeight()
                );

//- - - - - - - - - - - - - - - - - -    create buttons and font    * * * Constructor
        buttonsFont=new Font(Font.SANS_SERIF,1,(int)(Settings.SIZE_UNIT*1.2));
        // buttons[0]==NEW GAME/CONTINUE  [1]==SAVE   [2]==LOAD  [3]==OPTIONS
        fillCenterPanelWithButtons(Language.Menu.newGame,BorderLayout.NORTH);
        fillCenterPanelWithButtons(Language.Menu.howToPlay,BorderLayout.WEST);
        fillCenterPanelWithButtons(Language.Menu.cheats,BorderLayout.EAST);
        fillCenterPanelWithButtons(Language.Menu.settings,BorderLayout.SOUTH);
        bottomPanel.add(createButton(Language.Menu.exit,0,0));

//- - - - - - - - - - - - - - - -     background setup and other     * * * Constructor
        this.add(centerPanel);
        this.add(bottomPanel);
        filePath="src\\Images\\"+Settings.RESOLUTION_HEIGHT +"\\Golem.jpg";
        this.setImage(filePath,true);
        this.setVisible(true);
    }

    //= = = = = = = = = = = = = = = = = = = =    METHODS    = = = = = = = = = = = = = = = = = = = = = = = = =

    //- - - - - - - - - - - - - -     UPDATE SIZE ()   - - - - - - - - - - - - - - - -

    /**
     * Updates size of panels, background etc
     */
    @Override
    public void updateElement(){
        this.setPreferredSize(new Dimension(
                Settings.GAME_PANEL_WIDTH,
                Settings.GAME_PANEL_HEIGHT
        ));

//- - - - -     PANELS * * * updateSize()
        centerPanel.setSize(
                (int)(Settings.GAME_PANEL_WIDTH*0.3),
                (int)(Settings.GAME_PANEL_HEIGHT*0.3)
        );
        centerPanel.setLocation(
                (Settings.GAME_PANEL_WIDTH-this.centerPanel.getWidth())/2,
                (Settings.GAME_PANEL_HEIGHT-this.centerPanel.getHeight())/2
        );

        bottomPanel.setSize(
                (int)(Settings.GAME_PANEL_WIDTH*0.3),
                (int)(Settings.GAME_PANEL_HEIGHT*0.09)
        );
        bottomPanel.setLocation(
                (int)centerPanel.getLocation().getX(),
                (int)centerPanel.getLocation().getY()+centerPanel.getHeight()
        );

//- - - - - BUTTONS AND FONT * * * updateSize()
        buttonsFont=new Font(Font.SANS_SERIF,1,(int)(Settings.SIZE_UNIT*1.2));

        for(int i=0; i<buttons.size();i++){
            buttons.get(i).setPreferredSize(new Dimension(
                    Settings.SIZE_UNIT*10,
                    Settings.SIZE_UNIT*4
            ));
            buttons.get(i).setFont(buttonsFont);
        }

//- - - - - BACKGROUND * * * updateSize()
        if(!Game.isGameON()){
            filePath="src\\Images\\"+Settings.RESOLUTION_HEIGHT +"\\Golem.jpg";
            this.setImage(filePath,true);
        }
        SwingUtilities.updateComponentTreeUI(this);
    }

    /**
     * Updates language of main menu
     */
    @Override
    public void updateLanguage() {
        if(!Game.isGameON())buttons.get(0).setText(Language.Menu.newGame);
        else buttons.get(0).setText(Language.Menu.continueGame);
        buttons.get(1).setText(Language.Menu.howToPlay);
        buttons.get(2).setText(Language.Menu.cheats);
        buttons.get(3).setText(Language.Menu.settings);
        buttons.get(4).setText(Language.Menu.exit);

    }

    //- - - - - - - - - - -    BUTTONS FOR CENTER PANEL    - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * Fill main menu with buttons
     * @param buttonName name of button to be created
     * @param side Side on which should be displayed
     */
    private void fillCenterPanelWithButtons(String buttonName, String side){
        JButton button=createButton(buttonName);
        if(side!=null)centerPanel.add(button,side);
    }

    //- - - - - - - - - - - - - - -      Create button    - - - - - - - - - - - - - - -

    /**
     * Creates button for main menu
     * @param buttonName name of button
     * @param x Location X on panel
     * @param y Location Y on panel
     * @return Button to be clicked
     */
    private JButton createButton(String buttonName,int x,int y){
        JButton button=createButton(buttonName);
        button.setLocation(x,y);
        return button;
    }
    private JButton createButton(String buttonName){
        JButton button=new JButton(buttonName);

        button.addActionListener(this);
        button.setFont(buttonsFont);
        button.setFocusable(false);
        button.setPreferredSize(new Dimension(
                Settings.SIZE_UNIT*10,
                Settings.SIZE_UNIT*4
        ));
        buttons.add(button);
        return button;
    }

    //- - - - - - - - - - - - - - - - - - - - GETTERS AND SETTERS - - - - - - - - - - - - - -

    public JPanel getCenterPanel(){return centerPanel;}
    public JPanel getBottomPanel(){return bottomPanel;}

    //- - - - - - - - - - - - - - - - -   Overrides ActionListener    - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * Check which button is being clicked and perform action for it
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        //- - - - - -   START/CONTINUE BUTTON    * * * actionPerformed();
        if(e.getSource()==buttons.get(0)){
            if( ! Game.isGameON()) {
                Game.setGameON(true);
                mainWindow.startNewGame();
                buttons.get(0).setText(Language.Menu.continueGame);
                gamePanel.playerClassSelector();
            }
            else if(Game.isGameON()){
                mainWindow.hideMainMenuButtons();
            }
        }

        //- - - - - -   HOW TO PLAY BUTTON     * * * actionPerformed();
        else if(e.getSource()==buttons.get(1)){
            new HowToPlayWindow();
        }

        //- - - - - -   CHEATS BUTTON     * * * actionPerformed();
        else if(e.getSource()==buttons.get(2)){
            new CheatsWindow();
        }

        //- - - - - -   SETTINGS GAME BUTTON     * * * actionPerformed();
        else if(e.getSource()==buttons.get(3)){
            try{
                Wish.getSettingsWindow().dispose();
            }catch(Exception exception){}
            finally {
                new SettingsWindow();
            }
        }

        //- - - - - -   EXIT GAME BUTTON     * * * actionPerformed();
        else if(e.getSource()==buttons.get(4)){
            Wish.exit();
        }
    }
}
