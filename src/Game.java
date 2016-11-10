import org.newdawn.slick.*;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A game written using Java and Slick2D.
 * @author Oskar Eriksson and Gustave Rousselet
 * @version 0.1
 */
public class Game extends BasicGame {
    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;

    private Player player;
    private World theWorld;

    /**
     * Constructor for the game class
     * @param title Title of the game window
     */
    public Game(String title) { super(title); }

    /**
     * Initializing method
     * @param gameContainer GameContainer object handling game loop etc
     * @throws SlickException Generic exception
     */
    @Override
    public void init(GameContainer gameContainer) throws SlickException {
        generateWorld("res/maps/center.tmx", 150, 100);
    }

    /**
     * Updates every frame
     * @param gameContainer GameContainer object handling game loop etc
     * @param delta Amount of time that has passed since last update (ms)
     * @throws SlickException Generic exception
     */
    @Override
    public void update(GameContainer gameContainer, int delta) throws SlickException {
        checkKeyPress(gameContainer, delta);

        for (Exit exit : theWorld.getExits()) {
            if (player.getRect().intersects(exit.getRectangle())) {
                generateWorld(exit.getDestination(), exit.getXSpawnPosition(), exit.getYSpawnPosition());
            }
        }
    }

    /**
     * Renders every frame
     * @param gameContainer GameContainer object handling game loop etc
     * @param graphics Graphics component used to draw
     * @throws SlickException Generic exception
     */
    @Override
    public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
        theWorld.render(player.getXPosition(), player.getYPosition());
        graphics.fill(player.getRect());
    }

    public static void main(String[] args) {
        try {
            AppGameContainer appgc = new AppGameContainer(new Game("An Awesome Game"));
            appgc.setDisplayMode(WIDTH, HEIGHT, false);
            appgc.setTargetFrameRate(145);
            appgc.start();
        } catch (SlickException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Checks for key presses and executes commands accordingly
     * @param gameContainer GameContainer object handling game loop etc
     * @param delta Amount of time that has passed since last update (ms)
     */
    private void checkKeyPress(GameContainer gameContainer, int delta) {
        if (gameContainer.getInput().isKeyDown(Input.KEY_UP)) {
            theWorld.updateRectanglesY(player.movement("up", delta));
            for (Rectangle block : theWorld.getBlocks()) {
                if (player.getRect().intersects(block)) {
                    theWorld.updateRectanglesY(player.movement("down", delta));
                }
            }
        }
        if (gameContainer.getInput().isKeyDown(Input.KEY_DOWN)) {
            theWorld.updateRectanglesY(player.movement("down", delta));
            for (Rectangle block : theWorld.getBlocks()) {
                if (player.getRect().intersects(block)) {
                    theWorld.updateRectanglesY(player.movement("up", delta));
                }
            }
        }
        if (gameContainer.getInput().isKeyDown(Input.KEY_LEFT)) {
            theWorld.updateRectanglesX(player.movement("left", delta));
            for (Rectangle block : theWorld.getBlocks()) {
                if (player.getRect().intersects(block)) {
                    theWorld.updateRectanglesX(player.movement("right", delta));
                }
            }
        }
        if (gameContainer.getInput().isKeyDown(Input.KEY_RIGHT)) {
            theWorld.updateRectanglesX(player.movement("right", delta));
            for (Rectangle block : theWorld.getBlocks()) {
                if (player.getRect().intersects(block)) {
                    theWorld.updateRectanglesX(player.movement("left", delta));
                }
            }
        }
    }

    private void generateWorld(String worldName, int spawnX, int spawnY) throws SlickException {
        player = new Player(WIDTH/2, HEIGHT/2, 16, 16, 0.2);
        theWorld = new World(worldName, spawnX, spawnY);
    }
}
