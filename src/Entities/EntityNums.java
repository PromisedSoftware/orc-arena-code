package Entities;

import java.awt.*;

/**
 * Class Replacement of Enums with numbers assigned to animations
 * and team colors
 */
public class EntityNums {
    public static int currentWidthPanel;
    public static int currentHeightPanel;

    public static final int IDLE=0;
    public static final int WALK=1;
    public static final int ATTACK=2;
    public static final int DIE=3;
    public static final int SPELL_Q=4;
    public static final int SPELL_W=5;
    public static final int SPELL_E=6;

    public static final Color TEAM_ENEMY=new Color(255, 76, 76);
    public static final Color TEAM_ALLY=new Color(84, 214, 255);
    public static final Color TEAM_CIVILIAN=new Color(200,200,200);
    public static final Color TEAM_PLAYER=new Color(67, 250, 0);
}
