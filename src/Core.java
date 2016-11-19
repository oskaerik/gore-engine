import org.newdawn.slick.*;
import org.newdawn.slick.Graphics;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Core class handles the core structure of the game. It has a method to set up the
 * game window, a method that updates the game world every frame, and a method that renders
 * every frame.
 *
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

    // Time handling for start and end game screens
    private long startTime;

    // Start and end screen image
    private Image startScreen;
    private Image endScreen;

    /**
     * Constrcutor of the Core class.
     *
     * @param title The game title.
     */
    public Core(String title) { super(title); }

    /**
     * The main method, sets up the game window and starts the game.
     *
     * @param args Command-line arguments.
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
     * Initializing method, runs when the game is set up. Used to create the gameState.
     *
     * @param gameContainer GameContainer object handling game loop etc.
     * @throws SlickException Generic exception.
     */
    @Override
    public void init(GameContainer gameContainer) throws SlickException {
        // Create GameState
        gameState = new GameState("startscreen");

        // Start and end screen
        startTime = new Date().getTime();
        startScreen = new Image("res/startscreen/startscreen.png");
        endScreen = new Image("res/startscreen/endscreen.png");

        // Set the world to null before the start screen is gone
        world = null;
    }

    /**
     * Update method that runs every frame
     *
     * @param gameContainer GameContainer An object handling the game mechanics.
     * @param delta Amount of time that has passed since last updateGraphics (ms).
     * @throws SlickException Generic exception.
     */
    @Override
    public void update(GameContainer gameContainer, int delta) throws SlickException {
        // Updates the game world (if not still on the start screen)
        if (world != null) {
            world.updateWorld(gameContainer, delta);
        }
    }

    /**
     * Renders every frame during the game runtime.
     *
     * @param gameContainer GameContainer An object handling the game mechanics.
     * @param graphics Graphics component used to draw to the screen.
     * @throws SlickException Generic exception.
     */
    @Override
    public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
        if (gameState.getCurrentState().equals("startscreen")) {
            // Render start screen until start screen time has elapsed
            startScreen.draw();
            if ((new Date().getTime() - startTime)
                    > Tools.readSettings("res/settings.txt", "START_SCREEN_TIME")) {
                world = new World(gameState);
                gameState.startGame();
            }
        } else if (gameState.getCurrentState().equals("gameover")) {
            // If game over, draw the end screen, then quit game
            endScreen.draw();
            if ((new Date().getTime() - gameState.getEndTime())
                    > Tools.readSettings("res/settings.txt", "END_SCREEN_TIME")); {
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