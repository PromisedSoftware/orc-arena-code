package Inputs;

import Entities.EntityNums;
import Entities.Player;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseInputs implements MouseListener, MouseMotionListener {

    private Player player;
    private static int mouseX=0;
    private static int mouseY=0;
    public MouseInputs(Player player){
        this.player=player;
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton()==3){
            player.enemy=null;
            player.setDestination(e.getX(),e.getY());
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(SwingUtilities.isRightMouseButton(e)){
            player.enemy=null;
            player.setDestination(e.getX(),e.getY());
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX=e.getX();
        mouseY=e.getY();
    }
    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {
    }

    public static int mouseX(){return mouseX;}
    public static void mouseX(int X){mouseX = X;}
    public static int mouseY(){return mouseY;}
    public static void mouseY(int Y){mouseY = Y;}
}
