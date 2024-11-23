package MainPackage;

import LanguageStrings.Language;
import MyComponents.BJPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Panel with settings of the game
 */
public class SettingsPanel extends BJPanel {


    private static JComboBox languagePicker,resolutionPicker;
    private static JComboBox refreshFramePicker, actorTextureQualityPicker,terrainTextureQualityPicker;
    private static ArrayList<JComboBox> pickers;

    private static JLabel languageLabel,resolutionLabel;
    private static JLabel refreshLabel, actorTextureQualityLabel,terrainTextureQualityLabel;
    private static ArrayList<JLabel> labels;

    private static Font generalFont =new Font(Font.SANS_SERIF,Font.BOLD,(int)(Settings.SIZE_UNIT));
    private static String[] languageStringArray={"English","Polski"};
    private static String[] resolutionsStringArray={"1280x720","1920x1080","2560x1440","3840x2160"};
    private static Integer[] refreshStringArray={15,30,60,120,144};
    private static String[] textureQualityStringArray= {"Low","Medium","High","Ultra"};

    //= = = = = = = = = = = = = = = = = = = =    v CONSTRUCTOR v     = = = = = = = = = = = = = = = = = = =

    /**
     * Creates panel with buttons and other stuff
     * to manage settings of the game
     */
    public SettingsPanel(){
        super("SettingsPanel");
        Wish.putElementToArray(this);
        this.setLayout(new GridLayout(22,4));
        if(!Settings.initializedFlag)setDefaultSize(16,50);
        else setCustomSize(32,100);

        //- - - - - - - -    other    * * * CONSTRUCTOR
        this.add(languageLabel);
        this.add(languagePicker);
        this.add(resolutionLabel);
        this.add(resolutionPicker);
        this.add(refreshLabel);
        this.add(refreshFramePicker);
        this.add(actorTextureQualityLabel);
        this.add(actorTextureQualityPicker);
        this.add(terrainTextureQualityLabel);
        this.add(terrainTextureQualityPicker);
        updateLanguage();
        this.setVisible(true);
    }

    //= = = = = = = = = = = = = = = = = = = = =    METHODS    = = = = = = = = = = = = = = = = = = = = = = = = = =

    //- - - - - - - - - - - -     SET DEFAULT SIZE    - - - - - - - - - - - - - - - - - -

    /**
     * If game is run first time it creates a new default size
     * of the game window
     * @param widthPercent percentage width of a screen
     * @param heightPercent percentage height of a screen
     */
    private void setDefaultSize(int widthPercent, int heightPercent){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();
        this.setPreferredSize(new Dimension(
                (int)(width*widthPercent/100)
                ,(int)(height*heightPercent/100
        )));

        //- - - - - -   boxes sliders etc   * * * setDefaultSize();
        width=this.getPreferredSize().getWidth();
        height=this.getPreferredSize().getHeight();

        try{
            resolutionPicker.setPreferredSize(new Dimension(
                        (int)(width*0.8), (int)(height*0.1)
            ));
            refreshFramePicker.setPreferredSize(resolutionPicker.getPreferredSize());
            actorTextureQualityPicker.setPreferredSize(resolutionPicker.getPreferredSize());

            //- - - - - - - -    fonts    * * * setDefaultSize();
            generalFont =new Font(Font.SANS_SERIF,Font.BOLD,(int)(Settings.SIZE_UNIT));
            for(int i=0; i<pickers.size();i++)
                pickers.get(i).setFont(generalFont);
            for(int i=0; i<labels.size();i++)
                labels.get(i).setFont(generalFont);

        }catch(Exception e){
            e.printStackTrace();}
    }

    //- - - - - - - - - - - -     SET CUSTOM SIZE    - - - - - - - - - - - - - - -

    /**
     * If game is run first time it creates a new default size
     * of the game window
     * @param widthPercent percentage width of a screen
     * @param heightPercent percentage height of a screen
     */
    public void setCustomSize(int widthPercent, int heightPercent){
        double width = Settings.SCREEN_RESOLUTION_WIDTH;
        double height = Settings.SCREEN_RESOLUTION_HEIGHT;

        this.setPreferredSize(new Dimension(
                (int)(width*widthPercent/100)
                ,(int)(height*heightPercent/100
        )));

        //- - - - - -   boxes sliders etc  * * * setCustomSize();
        width=this.getPreferredSize().getWidth();
        height=this.getPreferredSize().getHeight();
        resolutionPicker.setPreferredSize(new Dimension(
                (int)(width*0.8),
                (int)(height*0.1)
        ));

        //- - - - - - - -   fonts   * * * setCustomSize();
        generalFont =new Font(Font.SANS_SERIF,Font.BOLD,(int)(Settings.SIZE_UNIT));
        for(int i=0; i<pickers.size();i++){
            pickers.get(i).setFont(generalFont);
        }
        for(int i=0; i<labels.size();i++)
            labels.get(i).setFont(generalFont);
    }

    //- - - - - - - - - - - - - - -     SAVE SETTINGS    - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * Save changes of game settings
     */
    public static synchronized void saveSettings(){
        ArrayList<String> settings=new ArrayList<>();
        for(int i=0; i<pickers.size();i++){
                settings.add(pickers.get(i).getSelectedItem().toString());
        }
        Tools.writeFile(settings,Settings.filePath);
    }

    //- - - - - - - - - - - - - - -     LOAD SETTINGS    - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * Load game settings application is started
     */
    public static void loadSettings(){
        ArrayList<String> settings=Tools.readFile(Settings.filePath);
        if(settings.size()==5){
            switch (settings.get(0)){
                case "Polski"->{
                    Language.setPolish();}
                default -> {
                    Language.setEnglish();}
            }
            setSettingsPickers();
            for(int i=0;i<settings.size();i++){
                if(i==2) pickers.get(i).setSelectedItem(Integer.parseInt(settings.get(i)));
                else pickers.get(i).setSelectedItem(settings.get(i));
            }
        }
        else{
            setSettingsPickers();}
    }

    //- - - - - - - - - - - - -    CREATE PICKERS IN SETTINGS

    /**
     * Creates boxes to be clicked and descriptions
     */
    public static void setSettingsPickers(){
        languageLabel = new JLabel("      "+ Language.Menu.language);
        resolutionLabel = new JLabel("      "+ Language.Menu.resolution);
        refreshLabel = new JLabel("      "+"FPS");
        actorTextureQualityLabel = new JLabel("      "+ Language.Menu.actorQuality);
        terrainTextureQualityLabel = new JLabel("      "+ Language.Menu.terrainQuality);
        labels=new ArrayList<>(Arrays.asList(
                languageLabel,resolutionLabel,refreshLabel,
                actorTextureQualityLabel,terrainTextureQualityLabel
        ));

        languagePicker=new JComboBox<>(languageStringArray);
        resolutionPicker=new JComboBox<>(resolutionsStringArray);
        refreshFramePicker=new JComboBox<>(refreshStringArray);
        actorTextureQualityPicker =new JComboBox<>(textureQualityStringArray);
        terrainTextureQualityPicker=new JComboBox<>(textureQualityStringArray);
        pickers=new ArrayList<>(Arrays.asList(
                languagePicker,resolutionPicker,refreshFramePicker,
                actorTextureQualityPicker,terrainTextureQualityPicker
        ));

        //- - - - - - - -    combo boxes    * * * setSettingsPickers()
        for(int i=0; i<pickers.size();i++){
            pickers.get(i).setFocusable(false);
            pickers.get(i).setEditable(false);
            pickers.get(i).addActionListener(new SettingsListener());
            pickers.get(i).setFont(generalFont);
        }
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * Check which box or button is clicked and perform action
     */
    private static class SettingsListener implements ActionListener{

        /**
         * Check which box or button is clicked and perform action
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource()==languagePicker){
                switch(languagePicker.getSelectedIndex()){
                    case 1-> {
                        Language.setPolish();
                        }
                    default -> {
                        Language.setEnglish();
                        }
                }
                renamePickers();
                Wish.updateLanguage();
            }

            if(e.getSource()==resolutionPicker){
                switch(resolutionPicker.getSelectedIndex()){
                    case 1->{
                        Settings.setResolution(1920,1080);}
                    case 2->{
                        Settings.setResolution(2560,1440);}
                    case 3->{
                        Settings.setResolution(3840,2160);}
                    default -> {
                        Settings.setResolution(1280,720);}
                }
                Wish.updateResolution();
            }

            else if(e.getSource()==refreshFramePicker){
                Settings.FPS = (Integer) refreshFramePicker.getSelectedItem();
                Wish.updateSpeedActors();
            }

            else if(e.getSource()==actorTextureQualityPicker){
                switch(actorTextureQualityPicker.getSelectedIndex()){
                    case 1->{Settings.ACTOR_TEXTURE_QUALITY ="Medium";}
                    case 2->{Settings.ACTOR_TEXTURE_QUALITY ="High";}
                    case 3->{Settings.ACTOR_TEXTURE_QUALITY ="Ultra";}
                    default -> {Settings.ACTOR_TEXTURE_QUALITY ="Low";}
                }
                Wish.updateActorTextures();
            }

            else if(e.getSource()==terrainTextureQualityPicker){
                switch(terrainTextureQualityPicker.getSelectedIndex()){
                    case 1->{Settings.TERRAIN_TEXTURE_QUALITY ="Medium";}
                    case 2->{Settings.TERRAIN_TEXTURE_QUALITY ="High";}
                    case 3->{Settings.TERRAIN_TEXTURE_QUALITY ="Ultra";}
                    default -> {Settings.TERRAIN_TEXTURE_QUALITY ="Low";}
                }
                Wish.updateTerrainTextures();
            }
            saveSettings();
        }
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * Set size of settings panel
     */
    @Override
    public void updateElement() {
        setCustomSize(32,100);
    }

    /**
     * Update language of settings panel
     */
    @Override
    public void updateLanguage() {
        textureQualityStringArray=Language.Menu.textures;
        languageLabel.setText("      "+ Language.Menu.language);
        resolutionLabel.setText("      "+ Language.Menu.resolution);
        refreshLabel.setText("      "+"FPS");
        actorTextureQualityLabel.setText("      "+ Language.Menu.actorQuality);
        terrainTextureQualityLabel.setText("      "+ Language.Menu.terrainQuality);

    }

    /**
     * Rename boxes labels, used when language is changed
     * to rename everything to other language
     */
    private static void renamePickers() {
        int actorIndexSelected=actorTextureQualityPicker.getSelectedIndex();
        int terrainIndexSelected=terrainTextureQualityPicker.getSelectedIndex();

        for(int i=0;i<actorTextureQualityPicker.getActionListeners().length;i++){
            actorTextureQualityPicker.removeActionListener(actorTextureQualityPicker.getActionListeners()[i]);
        }
        for(int i=0;i<terrainTextureQualityPicker.getActionListeners().length;i++){
            terrainTextureQualityPicker.removeActionListener(terrainTextureQualityPicker.getActionListeners()[i]);
        }
        actorTextureQualityPicker.removeAllItems();
        terrainTextureQualityPicker.removeAllItems();

        for(int i=0;i<Language.Menu.textures.length;i++){
            actorTextureQualityPicker.addItem(Language.Menu.textures[i]);
            terrainTextureQualityPicker.addItem(Language.Menu.textures[i]);
        }
        actorTextureQualityPicker.setSelectedIndex(actorIndexSelected);
        terrainTextureQualityPicker.setSelectedIndex(terrainIndexSelected);
        actorTextureQualityPicker.addActionListener(new SettingsListener());
        terrainTextureQualityPicker.addActionListener(new SettingsListener());
    }
}
