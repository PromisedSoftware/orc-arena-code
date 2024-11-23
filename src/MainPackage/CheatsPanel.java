package MainPackage;

import LanguageStrings.Language;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Extends JPanel
 * Used for cheats: immortal, infinite mama, disable AI look for enemy
 */
public class CheatsPanel extends JPanel implements ActionListener {
    private static boolean cheatGodBool = false, cheatManaBool=false, cheatAIBool=false;
    private CheatButton cheatButtonHP, cheatButtonMana, cheatButtonAI;

    /**
     * Creates an instance of JPanel object and set values for that panel
     */
    CheatsPanel(){
        this.setLayout(new GridLayout(3,4));
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();
        setPreferredSize(new Dimension(
                (int)(width * 20/100),
                (int)(height * 10/100)
        ));
        cheatButtonHP =new CheatButton(Language.CheatButtons.cheatGod,this);
        cheatButtonMana = new CheatButton(Language.CheatButtons.cheatMana,this);
        cheatButtonAI = new CheatButton(Language.CheatButtons.cheatAI,this);

        if(cheatGodBool) cheatButtonHP.setBackground(Color.GREEN);
        else cheatButtonHP.setBackground(Color.RED);

        if(cheatManaBool) cheatButtonMana.setBackground(Color.GREEN);
        else cheatButtonMana.setBackground(Color.RED);

        if(cheatAIBool) cheatButtonAI.setBackground(Color.GREEN);
        else cheatButtonAI.setBackground(Color.RED);

        add(cheatButtonHP);
        add(cheatButtonMana);
        add(cheatButtonAI);

    }

    public static void setCheatGod(boolean isImmortal){ cheatGodBool = isImmortal;}
    public static void setCheatMana(boolean isManaInfinite){cheatManaBool = isManaInfinite;}
    public static void setCheatAI(boolean cheatAIBool){CheatsPanel.cheatAIBool = cheatAIBool;}

    public static boolean isCheatGodEnabled(){return cheatGodBool;}
    public static boolean isCheatManaEnabled(){return cheatManaBool;}
    public static boolean isCheatAI_Enabled(){return cheatAIBool;}

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == cheatButtonHP){
            cheatGodBool = !cheatGodBool;
            if(cheatGodBool) cheatButtonHP.setBackground(Color.GREEN);
            else cheatButtonHP.setBackground(Color.RED);
        }
        else if(e.getSource() == cheatButtonMana){
            cheatManaBool = !cheatManaBool;
            if(cheatManaBool) cheatButtonMana.setBackground(Color.GREEN);
            else cheatButtonMana.setBackground(Color.RED);
        }
        else if(e.getSource() == cheatButtonAI){
            cheatAIBool = !cheatAIBool;
            if(cheatAIBool) cheatButtonAI.setBackground(Color.GREEN);
            else cheatButtonAI.setBackground(Color.RED);
        }
    }

    /**
     * Class of cheat button that extebds JBUtton
     */
    private class CheatButton extends JButton{

        /**
         * Creates button that do action when clicked
         * @param name Name of button that will be displayed on it.
         * @param cheatsPanel panel that is implemented with ActionListener
         */
        CheatButton(String name,CheatsPanel cheatsPanel){
            setText(name);
            setBackground(Color.red);
            addActionListener(cheatsPanel);
            setFocusable(false);
        }
    }
}
