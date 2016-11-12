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
    private String lastDirection;
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
        animation = player.getStandingPlayer("down");
        lastDirection = "down";
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
    public void checkKeyPresses(GameContainer gameContainer, int delta) throws SlickException {
        // Movement of the player
        boolean isMoving = false;
        if (gameContainer.getInput().isKeyDown(Input.KEY_UP)) {
            lastDirection = "up";
            movement(lastDirection, delta);
            isMoving = true;
        }
        if (gameContainer.getInput().isKeyDown(Input.KEY_DOWN)) {
            lastDirection = "down";
            movement(lastDirection, delta);
            isMoving = true;
        }
        if (gameContainer.getInput().isKeyDown(Input.KEY_LEFT)) {
            lastDirection = "left";
            movement(lastDirection, delta);
            isMoving = true;
        }
        if (gameContainer.getInput().isKeyDown(Input.KEY_RIGHT)) {
            lastDirection = "right";
            movement(lastDirection, delta);
            isMoving = true;
        }
        // If player is standing still, display the default player image
        if (!isMoving) {
            animation = player.getStandingPlayer(lastDirection);
        }

        // Adds items in range to inventory
        if (gameContainer.getInput().isKeyPressed(Input.KEY_SPACE)) {
            ArrayList<Item> intersectedItems = player.getIntersectedItems(currentRoom.getItems());
            currentRoom.removeItems(intersectedItems);
            for (Item item : intersectedItems) {
                player.addItemToInventory(item);
            }
        }

        // Drops items from inventory
        if (gameContainer.getInput().isKeyPressed(Input.KEY_A)) {
            if (!player.getInventory().checkIfEmpty()) {
                currentRoom.addItem(player.removeItemFromInventory());
            }
        }
    }

    /**
     * Handles the player movement, or rather the movement of the world itself
     * @param direction The direction the player should be moving
     */
    private void movement(String direction, int delta) {
        switch (direction) {
            case "up":
                currentRoom.updateRectanglesY(player.movement("up", delta));
                for (Rectangle block : currentRoom.getBlocks()) {
                    if (player.getRect().intersects(block)) {
                        currentRoom.updateRectanglesY(player.movement("down", delta));
                    }
                }
                animation = player.getUpAnimation();
                break;
            case "down":
                currentRoom.updateRectanglesY(player.movement("down", delta));
                for (Rectangle block : currentRoom.getBlocks()) {
                    if (player.getRect().intersects(block)) {
                        currentRoom.updateRectanglesY(player.movement("up", delta));
                    }
                }
                animation = player.getDownAnimation();
                break;
            case "left":
                currentRoom.updateRectanglesX(player.movement("left", delta));
                for (Rectangle block : currentRoom.getBlocks()) {
                    if (player.getRect().intersects(block)) {
                        currentRoom.updateRectanglesX(player.movement("right", delta));
                    }
                }
                animation = player.getLeftAnimation();
                break;
            case "right":
                currentRoom.updateRectanglesX(player.movement("right", delta));
                for (Rectangle block : currentRoom.getBlocks()) {
                    if (player.getRect().intersects(block)) {
                        currentRoom.updateRectanglesX(player.movement("left", delta));
                    }
                }
                animation = player.getRightAnimation();
                break;
            default:
                break;
        }

    }

    /**
     * Checks if the player has intersected any exits and changes the room if that is the case
     */
    public void checkIntersectedExit() {
        Exit exit = player.getIntersectedExit(currentRoom.getExits());
        if (exit != null) {
            currentRoom = rooms.get(exit.getDestination());
            player.setPlayerX(exit.getXSpawnPosition());
            player.setPlayerY(exit.getYSpawnPosition());
        }
    }

    /**
     * Draws the inventory outline and the objects in the inventory on the screen
     * @param graphics Graphics component used to draw
     */
    public void drawInventory(Graphics graphics) {
        graphics.draw(inventoryOutline);
        for (int i = 0; i < player.getInventory().getItems().size(); i ++) {
            graphics.drawImage(player.getInventory().getItems().get(i).getItemImage(), 16 * i, 500);
        }
    }

    /**
     * Draws the items on screen
     * @param graphics Graphics component used to draw
     */
    public void drawItems(Graphics graphics) {
        for (Item item : currentRoom.getItems()) {
            graphics.drawImage(item.getItemImage(), item.getRectangle().getX(), item.getRectangle().getY());
        }
    }

    /**
     * Draws highlighting on items that intersects with player's range
     */
    public void drawItemHighlighting() {
        for (Item item : player.getIntersectedItems(currentRoom.getItems())) {
            item.getItemFont().drawString(
                    item.getRectangle().getX(), item.getRectangle().getY(), item.getName(), Color.blue
            );
        }
    }

    /**
     * Checks for events, such as key presses and intersected exits
     * @param gameContainer
     * @param delta
     * @throws SlickException
     */
    public void checkEvents(GameContainer gameContainer, int delta) throws SlickException {
        checkKeyPresses(gameContainer, delta);
        checkIntersectedExit();
    }

    /**
     * Updates the graphics of the game world
     * @param graphics Graphics component used to draw
     */
    public void updateGraphics(Graphics graphics) {
        // Draw the world
        // CHANGE SO THAT PLAYER.GETX() IS LIKE WORLD OFFSET OR SMTH
        currentRoom.render(player.getX(), player.getY());

        // Draw the player animation
        animation.draw(player.getRect().getX()-16, player.getRect().getY()-16);

        drawItems(graphics);
        drawItemHighlighting();
        drawInventory(graphics);
    }
}
