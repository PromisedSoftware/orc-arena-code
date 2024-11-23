package MainPackage;

import Entities.Entity;

/**
 * Settings for a game
 */
public final class Settings {
    public static final String filePath="src\\DataFiles\\Settings.txt";
    public static boolean initializedFlag=false;

    public volatile static int FPS=30;
    public volatile static int GAME_PANEL_WIDTH;
    public volatile static int GAME_PANEL_HEIGHT;
    public volatile static int SIZE_UNIT;
    public volatile static float SCREEN_RESOLUTION_WIDTH;
    public volatile static float SCREEN_RESOLUTION_HEIGHT;
    public volatile static float SCALE_MULTIPLIER;
    public volatile static String RESOLUTION_HEIGHT;
    public volatile static String ACTOR_TEXTURE_QUALITY ="Low";
    public volatile static String TERRAIN_TEXTURE_QUALITY="Low";

    /**
     * Sets size of game window, and every element inside
     * @param width width of the window
     * @param height height of the window
     */
    public static void setResolution(int width,int height){
        initializedFlag=true;
        Settings.SCREEN_RESOLUTION_WIDTH=width;
        Settings.SCREEN_RESOLUTION_HEIGHT=height;
        Settings.GAME_PANEL_WIDTH=(int)SCREEN_RESOLUTION_WIDTH;
        Settings.GAME_PANEL_HEIGHT=(int)SCREEN_RESOLUTION_HEIGHT;
        Settings.SCALE_MULTIPLIER=SCREEN_RESOLUTION_HEIGHT/720;
        Settings.SIZE_UNIT=(int)(16*SCALE_MULTIPLIER);
        RESOLUTION_HEIGHT =(int)SCREEN_RESOLUTION_HEIGHT+"p";
        System.out.println("Size unit: "+ SIZE_UNIT);
    }

    /**
     * @return current resolution as string
     */
    public static String getResolution(){
        switch(RESOLUTION_HEIGHT){
            case "1080p"->{return "1920x1080";}
            case "1440p"->{return "2560x1440";}
            case "2160p"->{return "3840x2160";}
            default ->{return "1280x720";}
        }
    }
}
