package Inputs;
import Entities.Player;
import MainPackage.Game;
import MainPackage.GamePanel;
import MainPackage.Wish;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import HUD.HUD;
public class KeyboardInputs implements KeyListener {
    private Player player;
    private HUD hud;

    public KeyboardInputs(Player player, HUD hud){
        this.player=player;
        this.hud=hud;
    }
    @Override
    public void keyTyped(KeyEvent e) {
        if(!GamePanel.menuShowed){
            switch(e.getKeyChar()){
                case KeyEvent.VK_ESCAPE->{
                    Game.setIsPaused(true);
                    Wish.getMainWindow().showMainMenuButtons();
                }
                case 'q'->{hud.castSpell('q');}
                case 'w'->{hud.castSpell('w');}
                case 'e'->{hud.castSpell('e');}
            }
        }
        else {
            if(e.getKeyChar()==KeyEvent.VK_ESCAPE) Wish.getMainWindow().hideMainMenuButtons();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
