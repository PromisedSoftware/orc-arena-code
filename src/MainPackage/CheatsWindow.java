package MainPackage;

import javax.swing.*;
import java.awt.event.WindowListener;

public class CheatsWindow extends JFrame {
    CheatsWindow(){
        this.setTitle("Cheats");
        this.setResizable(true);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.add(new CheatsPanel());
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
