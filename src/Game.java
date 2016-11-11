import org.newdawn.slick.*;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A game written using Java and Slick2D.
 * @author Oskar Eriksson and Gustave Rousselet
 * @version 0.1
 */
public class Game extends BasicGame {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    private Player player;
    private World currentWorld;
    private HashMap<String, World> worlds;
    private Animation anitmation;
    private boolean isMoving;

    /**
     * Constructor for the game class
     * @param title Title of the game window
     */
    public Game(String title) { super(title); }

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
     * Initializing method
     * @param gameContainer GameContainer object handling game loop etc
     * @throws SlickException Generic exception
     */
    @Override
    public void init(GameContainer gameContainer) throws SlickException {
        player = new Player(WIDTH/2, HEIGHT/2, 16, 16, 0.2, 32);

        currentWorld = new World("res/maps/center.tmx", "center", 150, 100);
        World north = new World("res/maps/north.tmx", "north", 232, 447);

        worlds = new HashMap<>();
        worlds.put(currentWorld.getName(), currentWorld);
        worlds.put(north.getName(), north);
        isMoving = false;
        anitmation = player.getDefaultAnimation();

    }

    /**
     * Updates every frame
     * @param gameContainer GameContainer object handling game loop etc
     * @param delta Amount of time that has passed since last updateGraphics (ms)
     * @throws SlickException Generic exception
     */
    @Override
    public void update(GameContainer gameContainer, int delta) throws SlickException {
        checkKeyPress(gameContainer, delta);
        Exit exit = player.getIntersectedExit(currentWorld.getExits());
        if (exit != null) {
            currentWorld = worlds.get(exit.getDestination());
            player.setPlayerXPosition(exit.getXSpawnPosition());
            player.setPlayYPosition(exit.getYSpawnPosition());
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
        currentWorld.render(player.getXPosition(), player.getYPosition());
        for (Item item : currentWorld.getItems()) {
            graphics.drawImage(item.getItemImage(), item.getRectangle().getX(), item.getRectangle().getY());
        }
        player.updateGraphics(graphics, currentWorld.getExits());
        for (Item item : player.getIntersectedItems(currentWorld.getItems())) {
            item.getItemFont().drawString(item.getRectangle().getX(), item.getRectangle().getY(),
                    item.getName(), Color.blue);
        }
        anitmation.draw(player.getRect().getX()-16, player.getRect().getY()-16);
    }

    /**
     * Checks for key presses and executes commands accordingly
     * @param gameContainer GameContainer object handling game loop etc
     * @param delta Amount of time that has passed since last updateGraphics (ms)
     */
    private void checkKeyPress(GameContainer gameContainer, int delta) throws SlickException {
        if (gameContainer.getInput().isKeyDown(Input.KEY_UP)) {
            currentWorld.updateRectanglesY(player.movement("up", delta));
            for (Rectangle block : currentWorld.getBlocks()) {
                if (player.getRect().intersects(block)) {
                    currentWorld.updateRectanglesY(player.movement("down", delta));
                }
            }
            anitmation = player.getUpAnimation();
        }
        if (gameContainer.getInput().isKeyDown(Input.KEY_DOWN)) {
            currentWorld.updateRectanglesY(player.movement("down", delta));
            for (Rectangle block : currentWorld.getBlocks()) {
                if (player.getRect().intersects(block)) {
                    currentWorld.updateRectanglesY(player.movement("up", delta));
                }
            }
            anitmation = player.getDownAnimation();
        }
        if (gameContainer.getInput().isKeyDown(Input.KEY_LEFT)) {
            currentWorld.updateRectanglesX(player.movement("left", delta));
            for (Rectangle block : currentWorld.getBlocks()) {
                if (player.getRect().intersects(block)) {
                    currentWorld.updateRectanglesX(player.movement("right", delta));
                }
            }
            anitmation = player.getLeftAnimation();
        }
        if (gameContainer.getInput().isKeyDown(Input.KEY_RIGHT)) {
            currentWorld.updateRectanglesX(player.movement("right", delta));
            for (Rectangle block : currentWorld.getBlocks()) {
                if (player.getRect().intersects(block)) {
                    currentWorld.updateRectanglesX(player.movement("left", delta));
                }
            }
            anitmation = player.getRightAnimation();
        }
        if (gameContainer.getInput().isKeyPressed(Input.KEY_SPACE)) {
            ArrayList<Item> intersectedItems = player.getIntersectedItems(currentWorld.getItems());
            currentWorld.removeItems(intersectedItems);
            for (Item item : intersectedItems) {
                player.addItemToInventory(item);
            }
        }
        if (gameContainer.getInput().isKeyPressed(Input.KEY_A)) {
            if (!player.getInventory().checkIfEmpty()) {
                currentWorld.addItem(player.removeItemFromInventory());
            }
        }
        if (!gameContainer.getInput().isKeyDown(Input.KEY_UP) && !gameContainer.getInput()
                .isKeyDown(Input.KEY_DOWN) && !gameContainer.getInput().isKeyDown(Input
                .KEY_RIGHT) && !gameContainer.getInput().isKeyDown(Input.KEY_LEFT)) {
            anitmation = player.getDefaultAnimation();
        }
    }
}
