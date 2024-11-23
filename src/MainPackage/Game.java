package MainPackage;
import Entities.Entity;

public class Game implements Runnable{
    public static volatile boolean isGameOn_Flag =false;
    private static volatile boolean isPaused=true;
    private static Thread gameThread;
    private static int CURRENT_FRAMES;
    private static MainWindow mainWindow;
    private GamePanel gamePanel;

    /**
     * Creates new game mechanisms
     */
    public Game(){
        Wish.putElementToArray(this);
        Wish.putElementToArray(gameThread);
        Settings.setResolution(1280,720);
        gamePanel=new GamePanel();
        mainWindow =new MainWindow(gamePanel);
        SettingsPanel.loadSettings();
    }

    //= = = = = = = = = = = = = = = = = = = = =    METHODS    = = = = = = = = = = = = = = = = = = = = = = = = = =

    /**
     * Start game main Thread
     */
    public void startGameLoop(){
        gameThread=new Thread(this);
        gameThread.start();
    }

    /**
     * Game loop that is invoking other threads and manage
     * FPS settings
     */
    private void gameLoop(){
        CURRENT_FRAMES =0;
        double now;
        double lastFrame=System.nanoTime();
        double secondStarted=System.nanoTime();
        while (isGameOn_Flag) {
            while(Game.isPaused()&&Game.isGameON()){}
            now=System.nanoTime();
            if(now-lastFrame>=1000000000/Settings.FPS){
                lastFrame=now;

                for(Entity e:Wish.getAllEntities()) e.runThread();
                gamePanel.runTerrainThread();
                Wish.renderTerrain();
                try{
                    for(Entity e:Wish.getAllEntities()) e.renderSkills();
                }catch (Exception e){
                    System.out.println("Game loop failed to render skills");
                }

                try{
                    for(Entity e:Wish.getAllEntities()) e.renderIt();
                }catch (Exception e){
                    System.out.println("Game loop failed to render entity in array");
                }

                CURRENT_FRAMES++;
            }
            if(now-secondStarted>=1000000000){
                secondStarted=now;
                mainWindow.setCounter_FPS(CURRENT_FRAMES);
                CURRENT_FRAMES =0;
            }
        }
    }
    //- - - - - - - - - - - - - - - - - -    GETTERS AND SETTERS    - - - - - - - - - - - - - - - - - - - - - - -
    public static boolean isGameON(){return isGameOn_Flag;}
    public static boolean isPaused(){return isPaused;}

    public static void setIsPaused(boolean pause){isPaused=pause;}
    public static void setGameON(boolean is){isGameOn_Flag =is;}

    /**
     * Starts game loop
     */
    @Override
    public void run() {
        gameLoop();
    }
}