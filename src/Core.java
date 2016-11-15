import org.newdawn.slick.*;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import java.util.ArrayList;
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
        world = new World();
    }

    /**
     * Update method that runs every frame
     * @param gameContainer GameContainer An object handling the game mechanics
     * @param delta Amount of time that has passed since last updateGraphics (ms)
     * @throws SlickException Generic exception
     */
    @Override
    public void update(GameContainer gameContainer, int delta) throws SlickException {
        // Updates the game world
        world.checkEvents(gameContainer, delta);
        world.updateWorld(delta);
    }

    /**
     * Renders every frame
     * @param gameContainer GameContainer An object handling the game mechanics
     * @param graphics Graphics component used to draw to the screen
     * @throws SlickException Generic exception
     */
    @Override
    public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
        // Updates the graphics of the game world
        world.updateGraphics(graphics);
    }
}