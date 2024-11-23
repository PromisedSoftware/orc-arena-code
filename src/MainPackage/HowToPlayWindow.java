package MainPackage;

import MyComponents.BJPanel;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;

public class HowToPlayWindow extends JFrame {
    private static HowToPlayWindow h2pWindow;
    public HowToPlayWindow(){
        this.setTitle("How to play");
        this.setResizable(true);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        this.add(new HowToPlayPanel());

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        h2pWindow=this;
    }

    public static void updateElement() {
        h2pWindow.pack();
    }

    public void updateLanguage() {

    }
}
