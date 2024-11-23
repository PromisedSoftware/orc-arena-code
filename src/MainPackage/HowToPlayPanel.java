package MainPackage;

import LanguageStrings.Language;

import javax.swing.*;
import java.awt.*;

public class HowToPlayPanel extends JPanel {
    HowToPlayPanel(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();
        this.setLayout(new GridLayout(4,4));
        setPreferredSize(new Dimension(
                (int)(width * 20/100),
                (int)(height * 20/100)
        ));

        add(new JLabel(Language.HowToPlay.howToMove));
        add(new JLabel(Language.HowToPlay.howToAttack));
        add(new JLabel(Language.HowToPlay.howToUseSkills));
    }
}
