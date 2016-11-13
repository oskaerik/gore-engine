import org.newdawn.slick.*;
import org.newdawn.slick.Graphics;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A game written using Java and Slick2D.
 * @author Oskar Eriksson and Gustave Rousselet
 * @version 0.1
 */
public class Core extends BasicGame {
    // Width and height of the game window
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    // World object that holds object classes and updates the graphics
    private World world;

    /**
     * Constructor for the game class
     * @param title Title of the game window
     */
    public Core(String title) { super(title); }

    public static void main(String[] args) {
        try {
            AppGameContainer appgc = new AppGameContainer(new Core("An Awesome Core"));
            appgc.setDisplayMode(WIDTH, HEIGHT, false);
            appgc.setTargetFrameRate(60);
            appgc.start();
        } catch (SlickException ex) {
            Logger.getLogger(Core.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Initializing method
     * @param gameContainer GameContainer object handling game loop etc
     * @throws SlickException Generic exception
     */
    @Override
    public void init(GameContainer gameContainer) throws SlickException {
        world = new World();
    }

    /**
     * Updates every frame
     * @param gameContainer GameContainer object handling game loop etc
     * @param delta Amount of time that has passed since last updateGraphics (ms)
     * @throws SlickException Generic exception
     */
    @Override
    public void update(GameContainer gameContainer, int delta) throws SlickException {
        world.checkEvents(gameContainer, delta);
    }

    /**
     * Renders every frame
     * @param gameContainer GameContainer object handling game loop etc
     * @param graphics Graphics component used to draw
     * @throws SlickException Generic exception
     */
    @Override
    public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
        world.updateGraphics(graphics);
        /**for (Character character : world.getCurrentRoom().getCharacters()) {
            graphics.fill(character.getRect());
        }
        for (Projectile fireball : world.getCurrentRoom().getProjectiles()) {
            graphics.fill(fireball.getRect());
        }
        graphics.fill(world.getPlayer().getRect());
        graphics.fill(world.getCurrentRoom().getProjectiles().get(0).getRect());*/
    }
}
