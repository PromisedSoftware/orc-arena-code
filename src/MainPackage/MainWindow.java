package MainPackage;
import javax.swing.*;

/**
 * Main borderless window with every element of this application
 */
public class MainWindow extends JFrame {

    private GamePanel gamePanel;
    private MainMenuPanel mainMenuPanel;
    private JPanel centerPanel;
    private JPanel bottomPanel;

    //= = = = = = = = = = = = = = = = = = = =    v CONSTRUCTOR v     = = = = = = = = = = = = = = = = = = =

    /**
     * Creates window of this application
     * @param gamePanel main game panel with every game object
     */
    public MainWindow(GamePanel gamePanel){
        Wish.setWindowAsElement(this);
        gamePanel.setVisible(false);
        this.setUndecorated(true);
        this.setDefaultCloseOperation(2);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setTitle("Main Menu");
        this.setResizable(false);
        this.setVisible(true);

        mainMenuPanel=new MainMenuPanel(this,gamePanel);
        bottomPanel=mainMenuPanel.getBottomPanel();
        centerPanel=mainMenuPanel.getCenterPanel();
        this.gamePanel=gamePanel;
        this.add(mainMenuPanel);
    }

    //= = = = = = = = = = = = = = = = = = = = =    METHODS    = = = = = = = = = = = = = = = = = = = = = = = = = =

    /**
     * Starts game
     */
    public void startGame(){
        startNewGame();
        Wish.addListenersToGame();
        Wish.startGameLoop();
    }

    /**
     * Starts a new game
     */
    public void startNewGame(){
        this.remove(mainMenuPanel);
        this.add(gamePanel);
        gamePanel.setVisible(true);
        mainMenuPanel.setOpaque(false);
        mainMenuPanel.setVisible(false);
        Game.setIsPaused(false);
    }

    /**
     * Display created main menu buttons
     */
    public void showMainMenuButtons(){
        GamePanel.menuShowed=true;
        Game.setIsPaused(true);
        gamePanel.add(centerPanel,(Integer)3);
        gamePanel.add(bottomPanel,(Integer)4);
        SwingUtilities.updateComponentTreeUI(gamePanel);
    }

    /**
     * Hides main menu buttons
     */
    public void hideMainMenuButtons() {
        GamePanel.menuShowed=false;
        Game.setIsPaused(false);
        gamePanel.remove(centerPanel);
        gamePanel.remove(bottomPanel);
        SwingUtilities.updateComponentTreeUI(gamePanel);
    }

    /**
     * sets amount of current FPS
     * @param fps current amount FPS
     */
    public void setCounter_FPS(int fps){
        gamePanel.setCounter_FPS(fps);
    }

    /**
     * Make window big or small enough to pack panels
     */
    public void updateElement() {
        this.pack();
        this.setLocationRelativeTo(null);
    }
}
