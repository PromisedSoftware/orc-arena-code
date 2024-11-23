package MyComponents;

import javax.swing.*;
import java.awt.*;

public class ProgressBar extends JPanel {

    private int value;
    private int maxValue;
    private int minValue;

    private JLayeredPane mainContainer;
    private JPanel valuePanel;
    private String text;
    private JLabel label;
    private Font font;

    public ProgressBar(){
        this.setOpaque(false);
        this.setLayout(null);
        super.setSize(0,0);
        minValue=0;
        maxValue=0;
        value=0;
        text="";

        mainContainer=new JLayeredPane();
        valuePanel=new JPanel();
        label=new JLabel();
        font=new Font(Font.SANS_SERIF,Font.PLAIN,this.getHeight());

        mainContainer.setOpaque(true);
        mainContainer.setLayout(null);
        mainContainer.setLocation(0,0);
        mainContainer.setSize(0,0);
        mainContainer.setBackground(Color.WHITE);
        mainContainer.setVisible(true);

        valuePanel.setOpaque(true);
        valuePanel.setLocation(0,0);
        valuePanel.setSize(0,0);
        valuePanel.setBackground(Color.LIGHT_GRAY);
        valuePanel.setVisible(true);

        label.setFont(font);
        label.setLocation(0,0);
        label.setSize(0,0);
        label.setOpaque(false);
        label.setVisible(true);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);

        mainContainer.add(valuePanel,(Integer)1);
        mainContainer.add(label,(Integer)2);
        this.add(mainContainer);
        this.setVisible(true);

    }

    // = = = = = = = = = = = = = =    METHODS    = = = = = = = = = = = = = =
    public void setMaxValue(int maxValue){
        this.maxValue=maxValue;
    }

    public void setValue(int value){
        this.value=value;
        float percentValue=((float)value/(float)maxValue);
        valuePanel.setSize(
                (int)(mainContainer.getWidth()*percentValue),
                mainContainer.getHeight());
    }

    public void setValueColor(Color color){
        valuePanel.setBackground(color);
    }

    public void setBarColor(Color color){
        mainContainer.setBackground(color);
    }

    public void setTextColor(Color color){
        label.setForeground(color);
    }

    public void setTextFont(Font font){
        this.font=new Font(font.getFontName(),font.getStyle(),this.getHeight());
        label.setFont(this.font);}

    public void setBarOpaque(boolean isOpaque){
        mainContainer.setOpaque(isOpaque);
    }

    public void setValueBarOpaque(boolean isOpaque){
        valuePanel.setOpaque(isOpaque);
    }

    public void setText(String text){
        this.text=text;
        label.setText(text);
        label.setFont(font);
    }

    @Override
    public void setSize(int x,int y) {
        super.setSize(x, y);
        mainContainer.setSize(x, y);
        valuePanel.setSize(x, y);
        font=new Font(font.getFontName(),font.getStyle(),this.getHeight());
        label.setSize(x, y);
        label.setFont(font);
    }
}
