package MyComponents;

import MainPackage.Settings;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Implementation of JPanel that make it easier to add images and text
 * and manage them.
 */

public abstract class BJPanel extends JPanel{
    private JLabel labelWithBackground;
    private JPanel texturePanel;
    private JPanel descriptionPanel;
    private JLabel textLabel;
    public String name;
    public BJPanel(String name){
        this.name=name;
    }

    public void removeImages(){
        clearImages();
    }
    public void setImage(String filePath, boolean isOpaque){
        this.setLayout(null);
        if(filePath==null) filePath ="";
        clearImages();
        if(new File(filePath).exists()){
            createBackground(0,0,filePath,isOpaque);
        }
        else{
            createNoBackground(0,0);
        }
    }

    public void setImage(int width,int height,String filePath, boolean isOpaque){
        if(filePath==null) filePath ="";
        clearImages();
        if(new File(filePath).exists()){
            createBackground(width,height,filePath,isOpaque);
        }
        else{
            createNoBackground(width,height);
        }
    }

    // - - - - - - -    sub methods of setImage() - - - - - - - - - - v v v v v
    private void clearImages(){
        if(labelWithBackground!=null){
            this.remove(labelWithBackground);
            labelWithBackground=new JLabel();
        }
        if(texturePanel !=null){
            this.remove(texturePanel);
            texturePanel=new JPanel();
        }
    }
    private void createBackground(int width, int height, String filePath, boolean isOpaque){
        if(width<=0) width= Settings.GAME_PANEL_WIDTH;
        if(height<=0) height=Settings.GAME_PANEL_HEIGHT;

        labelWithBackground=new JLabel();
        ImageIcon imageIcon=new ImageIcon(filePath);

        labelWithBackground.setIcon(imageIcon);
        labelWithBackground.setOpaque(isOpaque);
        labelWithBackground.setSize(width,height);
        labelWithBackground.setLocation(0,0);

        this.add(labelWithBackground);
        labelWithBackground.setVisible(true);
    }
    private void createNoBackground(int width, int height){
        if(width<=0) width=Settings.GAME_PANEL_WIDTH;
        if(height<=0) height=Settings.GAME_PANEL_HEIGHT;

        String missingTexture="MISSING TEXTURE MISSING TEXTURE";
        missingTexture+=missingTexture+missingTexture+missingTexture;

        texturePanel =new JPanel();
        JLabel missingTextureTextLabel=new JLabel();

        missingTextureTextLabel.setFont(new Font(Font.SANS_SERIF,Font.BOLD,Settings.SIZE_UNIT));
        missingTextureTextLabel.setText(missingTexture);
        texturePanel.setLocation(0,0);
        texturePanel.setBackground(Color.MAGENTA);
        texturePanel.setOpaque(true);
        texturePanel.setSize(new Dimension(width,height));
        texturePanel.setLocation(0,0);
        texturePanel.add(missingTextureTextLabel);

        this.add(texturePanel);
        texturePanel.setVisible(true);
    }
    // - - - - - - -    end of sub methods of setImage() - - - - - - - - - - ^ ^ ^ ^ ^

    //- - - - - - - - - -    SET LABEL    - - - - - - - - - - -
    public void setLabel(JLabel label,String bl){
        descriptionPanel =new JPanel();
        if(bl==null) descriptionPanel.setLayout(null);
        else descriptionPanel.setLayout(new BorderLayout());
        descriptionPanel.setSize(label.getSize());
        descriptionPanel.setOpaque(false);
        descriptionPanel.add(label,bl);
        this.remove(labelWithBackground);
        if(bl==null) this.add(descriptionPanel);
        else this.add(descriptionPanel,bl);
        this.add(labelWithBackground);
        descriptionPanel.setVisible(true);
    }

    //- - - - - - - - -   SET TEXT - - - - - - - - -
    public void setText(String text,float fontSize){
        try{
            textLabel.setText(text);
            if(fontSize>0){
                textLabel.setFont(
                        new Font(Font.SANS_SERIF,Font.BOLD,(int)(Settings.SIZE_UNIT*fontSize)));
            }
        }catch(Exception e){
            Font font=new Font(Font.SANS_SERIF,Font.BOLD,(int)(Settings.SIZE_UNIT*fontSize));
            textLabel=new JLabel(text);
            textLabel.setFont(font);
            textLabel.setVisible(true);
            textLabel.setOpaque(true);
            textLabel.setBackground(new Color(0,0,0,125));
            textLabel.setForeground(Color.WHITE);
            this.add(textLabel);
        }

    }

    public void clearLabels(){
        try{
            this.remove(descriptionPanel);
        }catch (Exception e){}
    }
    public void setLabelLocation(int x,int y){
        try{
            this.setLayout(null);
            descriptionPanel.setLocation(x,y);
        }catch(NullPointerException e){System.out.println("BJPanel.setLabelLocation() NullPointerException");}
    }

    public Dimension getLabelSize(){return descriptionPanel.getSize();}
    public JLabel getBackgroundImage(){return labelWithBackground;}
    public String getName(){return null;}

    public abstract void updateElement();

    public abstract void updateLanguage();
}
