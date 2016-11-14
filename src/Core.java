import org.newdawn.slick.*;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Core class handles setting up the game window. Also updating and the rendering the game.
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

    // Debugging variable
    private boolean debug;

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
        debug = false;
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

        // Toggle debugging mode
        if (gameContainer.getInput().isKeyPressed(Input.KEY_P) && DEBUG_ENABLED == 1) {
            debug = !debug;
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
        // Updates the graphics of the game world
        world.updateGraphics(graphics);

        // If debug is set to true, run the debugging method
        if (debug) {
            runDebug(graphics);
        }
    }

    /**
     * Debugging method, fills rectangles so they're easier to see and prints information
     * about the game world
     * @param graphics Graphics component used to draw to the screen
     */
    private void runDebug(Graphics graphics) {
        // Fills rectangles with white color so they are easier to see
        graphics.setColor(Color.white);
        for (Rectangle block : world.getCurrentRoom().getBlocks()) {
            graphics.fill(block);
        }
        for (Character character : world.getCurrentRoom().getCharacters()) {
            graphics.fill(character.getRect());
        }
        for (Projectile fireball : world.getCurrentRoom().getProjectiles()) {
            graphics.fill(fireball.getRect());
        }
        graphics.fill(world.getPlayer().getRect());

        // If player is in dialogue, it prints which way the player and the character that also is
        // in dialogue is facing. If not in dialogue, prints the top left corner coordinates.
        if (world.getPlayer().getInDialogue() != null) {
            ArrayList<String> facing = Tools.getFacing(
                    world.getPlayer().getRect(), world.getPlayer().getInDialogue().getRect());
            System.out.println("Player facing: " + facing.get(0) + "   "
                    + world.getPlayer().getInDialogue().getName() + " facing: " + facing.get(1));
        } else {
            System.out.println("Top left corner coordinates   x: "
                    + world.getCurrentRoom().getOffset()[0]
                    + " y: " + world.getCurrentRoom().getOffset()[1]);
        }
    }
}