import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The camera class handles the moving of objects
 * @author Oskar Eriksson and Gustave Rousselet
 * @version 0.1
 */
public class World {
    private Player player;
    private Animation animation;
    private Rectangle inventoryOutline;

    private Room currentRoom;
    private HashMap<String, Room> rooms;

    /**
     * Constructor for the World class
     * @throws SlickException Generic exception
     */
    public World() throws SlickException {
        // The player object, takes parameters: width, height, speed, radius of range
        player = new Player(16, 16, 0.2, 32);
        animation = player.getDefaultAnimation();
        inventoryOutline = new Rectangle(0, Game.HEIGHT-100, 300, 100);

        // Create starting room and add all rooms to HashMap rooms
        currentRoom = new Room("res/maps/center.tmx", "center", 150, 100);
        Room north = new Room("res/maps/north.tmx", "north", 232, 447);
        rooms = new HashMap<>();
        rooms.put(currentRoom.getName(), currentRoom);
        rooms.put(north.getName(), north);
    }

    /**
     * Checks for key presses and executes commands accordingly
     * @param gameContainer GameContainer object handling game loop etc
     * @param delta Amount of time that has passed since last updateGraphics (ms)
     */
    public void checkKeyPress(GameContainer gameContainer, int delta) throws SlickException {
        if (gameContainer.getInput().isKeyDown(Input.KEY_UP)) {
            currentRoom.updateRectanglesY(player.movement("up", delta));
            for (Rectangle block : currentRoom.getBlocks()) {
                if (player.getRect().intersects(block)) {
                    currentRoom.updateRectanglesY(player.movement("down", delta));
                }
            }
            animation = player.getUpAnimation();
        }
        if (gameContainer.getInput().isKeyDown(Input.KEY_DOWN)) {
            currentRoom.updateRectanglesY(player.movement("down", delta));
            for (Rectangle block : currentRoom.getBlocks()) {
                if (player.getRect().intersects(block)) {
                    currentRoom.updateRectanglesY(player.movement("up", delta));
                }
            }
            animation = player.getDownAnimation();
        }
        if (gameContainer.getInput().isKeyDown(Input.KEY_LEFT)) {
            currentRoom.updateRectanglesX(player.movement("left", delta));
            for (Rectangle block : currentRoom.getBlocks()) {
                if (player.getRect().intersects(block)) {
                    currentRoom.updateRectanglesX(player.movement("right", delta));
                }
            }
            animation = player.getLeftAnimation();
        }
        if (gameContainer.getInput().isKeyDown(Input.KEY_RIGHT)) {
            currentRoom.updateRectanglesX(player.movement("right", delta));
            for (Rectangle block : currentRoom.getBlocks()) {
                if (player.getRect().intersects(block)) {
                    currentRoom.updateRectanglesX(player.movement("left", delta));
                }
            }
            animation = player.getRightAnimation();
        }
        if (gameContainer.getInput().isKeyPressed(Input.KEY_SPACE)) {
            ArrayList<Item> intersectedItems = player.getIntersectedItems(currentRoom.getItems());
            currentRoom.removeItems(intersectedItems);
            for (Item item : intersectedItems) {
                player.addItemToInventory(item);
            }
        }
        if (gameContainer.getInput().isKeyPressed(Input.KEY_A)) {
            if (!player.getInventory().checkIfEmpty()) {
                currentRoom.addItem(player.removeItemFromInventory());
            }
        }
        if (!gameContainer.getInput().isKeyDown(Input.KEY_UP) && !gameContainer.getInput()
                .isKeyDown(Input.KEY_DOWN) && !gameContainer.getInput().isKeyDown(Input
                .KEY_RIGHT) && !gameContainer.getInput().isKeyDown(Input.KEY_LEFT)) {
            animation = player.getDefaultAnimation();
        }
    }

    public void checkIntersectedExit() {
        Exit exit = player.getIntersectedExit(currentRoom.getExits());
        if (exit != null) {
            currentRoom = rooms.get(exit.getDestination());
            player.setPlayerX(exit.getXSpawnPosition());
            player.setPlayerY(exit.getYSpawnPosition());
        }
    }

    public void drawInventory(Graphics graphics, Rectangle inventoryOutline) {
        graphics.draw(inventoryOutline);
        for (int i = 0; i < player.getInventory().getItems().size(); i ++) {
            graphics.drawImage(player.getInventory().getItems().get(i).getItemImage(), 16 * i, 500);
        }
    }

    public void drawItems(Graphics graphics) {
        for (Item item : currentRoom.getItems()) {
            graphics.drawImage(item.getItemImage(), item.getRectangle().getX(), item.getRectangle().getY());
        }
    }

    public void drawItemHighlighting() {
        for (Item item : player.getIntersectedItems(currentRoom.getItems())) {
            item.getItemFont().drawString(
                    item.getRectangle().getX(), item.getRectangle().getY(), item.getName(), Color.blue
            );
        }
    }

    public void checkEvents(GameContainer gameContainer, int delta) throws SlickException {
        checkKeyPress(gameContainer, delta);
        checkIntersectedExit();
    }

    public void updateGraphics(Graphics graphics) {
        // Draw the world
        // CHANGE SO THAT PLAYER.GETX() IS LIKE WORLD OFFSET OR SMTH
        currentRoom.render(player.getX(), player.getY());

        // Draw the player animation
        animation.draw(player.getRect().getX()-16, player.getRect().getY()-16);

        drawItems(graphics);
        drawItemHighlighting();
        drawInventory(graphics, inventoryOutline);
    }

    private void movement() {

    }
}
