package MainPackage;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * window with settings panel
 */
public class SettingsWindow extends JFrame implements Responsive, WindowListener{
    SettingsPanel panel;

    /**
     * Creates a new window that contains panel settings
     */
    public SettingsWindow(){
        Wish.setWindowAsElement(this);
        this.setTitle("Settings");
        this.setResizable(true);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        panel=new SettingsPanel();
        this.add(panel);
        addWindowListener(this);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    /**
     * make window fit to content
     */
    public void updateElement() {
        this.pack();
    }
    @Override
    public void updateLanguage() {}
    @Override
    public void windowOpened(WindowEvent e) {}

    /**
     * removes settings panel when this window is closed
     * @param e the event to be processed
     */
    @Override
    public void windowClosing(WindowEvent e) {
        Wish.removePanel(panel);
    }
    @Override
    public void windowClosed(WindowEvent e) {}
    @Override
    public void windowIconified(WindowEvent e) {}
    @Override
    public void windowDeiconified(WindowEvent e) {}

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
