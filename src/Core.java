import org.newdawn.slick.*;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Core class handles handles the core structure of the game. It has a method to set up the
 * game window, a method that updates the game world every frame, and a method that renders
 * every frame.
 * @author Oskar Eriksson and Gustave Rousselet
 * @version 0.1
 */
public class Core extends BasicGame {
    // Reads settings from setting file
    public static final int WIDTH = Tools.readSettings("res/settings.txt", "WIDTH");
    public static final int HEIGHT = Tools.readSettings("res/settings.txt", "HEIGHT");
    public static final int FPS_LIMIT = Tools.readSettings("res/settings.txt", "FPS_LIMIT");
    public static final int DEBUG_ENABLED = Tools.readSettings("res/settings.txt", "DEBUG_ENABLED");

    // The World object handles the game world
    private World world;

    // GameState object, holding information about the state of the game,
    // if the inventory is open, if the player is in a dialogue etc
    private GameState gameState;

    // Time handling
    private Date date;
    private long startTime;

    // Start and end screen image
    private Image startScreen;
    private Image endScreen;

    /**
     * Constructor for the Core class
     * @param title Title of the game window
     */
    public Core(String title) { super(title); }

    /**
     * The main method, sets up the game window and starts the game
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        try {
            // Set up game window and start the game
            AppGameContainer appGC = new AppGameContainer(new Core("slick-game"));
            appGC.setDisplayMode(WIDTH, HEIGHT, false);
            appGC.setTargetFrameRate(FPS_LIMIT);
            appGC.start();
        } catch (SlickException ex) {
            Logger.getLogger(Core.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Initializing method, runs when the game is set up
     * @param gameContainer GameContainer object handling game loop etc
     * @throws SlickException Generic exception
     */
    @Override
    public void init(GameContainer gameContainer) throws SlickException {
        // Create GameState
        gameState = new GameState();
        gameState.toggleStartScreen();
        System.out.println(gameState.getCurrentState());

        date = new Date();
        startTime = date.getTime();
        startScreen = new Image("res/startscreen/startscreen.png");
        endScreen = new Image("res/startscreen/endscreen.png");

        world = null;
    }

    /**
     * Update method that runs every frame
     * @param gameContainer GameContainer An object handling the game mechanics
     * @param delta Amount of time that has passed since last updateGraphics (ms)
     * @throws SlickException Generic exception
     */
    @Override
    public void update(GameContainer gameContainer, int delta) throws SlickException {
        if (world != null) {
            // Updates the game world
            world.updateWorld(gameContainer, delta);
        }
    }

    /**
     * Renders every frame
     * @param gameContainer GameContainer An object handling the game mechanics
     * @param graphics Graphics component used to draw to the screen
     * @throws SlickException Generic exception
     */
    @Override
    public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
        if (gameState.getCurrentState().equals("startscreen")) {
            startScreen.draw(0,0);
            System.out.println(new Date().getTime());
            System.out.println(startTime);
            System.out.println(new Date().getTime() - startTime);
            if ((new Date().getTime() - startTime) > 5000) {
                System.out.println("HERE");
                world = new World(gameState);
                gameState.toggleStartGame();
            }
        } else if (gameState.getCurrentState().equals("gameover")) {
            endScreen.draw();
            if ((new Date().getTime() - world.getGameState().getEndTime()) > 5000) {
                gameContainer.exit();
            }
        } else {
            // Updates the graphics of the game world
            if (world != null) {
                world.updateGraphics(graphics);
            }
        }
    }
}