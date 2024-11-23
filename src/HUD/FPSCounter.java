package HUD;
import MainPackage.Settings;
import javax.swing.*;
import java.awt.*;

/**
 * Basic FPS counter on the top left side of the window
 */
public class FPSCounter extends JPanel {
    private JLabel textLabel;
    private Font font;
    float fontSize=1.0f;

    /**
     * Creates FPS counter on upper left corner of the window
     */
     FPSCounter(){
        this.setBounds(0,0, Settings.SIZE_UNIT*3,(int)(Settings.SIZE_UNIT*1.5f));
        this.setOpaque(false);
        textLabel=new JLabel();
        font=new Font(Font.SANS_SERIF,Font.BOLD,(int)(Settings.SIZE_UNIT*fontSize*0.75f));
        textLabel.setFont(font);
        textLabel.setOpaque(false);
        textLabel.setForeground(Color.WHITE);
        add(textLabel);
    }

    /**
     * Sets amount of frames to be displayed on the counter.
     * THIS DOES NOT CHANGE FPS OF THE GAME
     * @param frames amount of frames to be displayed on counter
     */
    void setCounter_FPS(int frames){
        textLabel.setText("FPS:"+frames);
    }

    /**
     * Updates counter
     */
    void updateIt(){
        this.setBounds(0,0, Settings.SIZE_UNIT*3,(int)(Settings.SIZE_UNIT*1.5f));
        font=new Font(Font.SANS_SERIF,Font.BOLD,(int)(Settings.SIZE_UNIT*0.75f));
        textLabel.setFont(font);
    }
}
