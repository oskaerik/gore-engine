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

    private GameState gameState;

    /**
     * Constructor for the World class
     * @throws SlickException Generic exception
     */
    public World() throws SlickException {
        // The player object, takes parameters: width, height, speed, radius of range
        player = new Player(16, 16, 0.2, 32);
        animation = player.getStandingPlayer("down");
        lastDirection = "down";
        inventoryOutline = new Rectangle(0, Core.HEIGHT-100, 300, 100);

        // Create starting room and add all rooms to HashMap rooms
        currentRoom = new Room("res/maps/center.tmx", "center", 0, 0);
        Room north = new Room("res/maps/north.tmx", "north", 0, 0);
        rooms = new HashMap<>();
        rooms.put(currentRoom.getName(), currentRoom);
        rooms.put(north.getName(), north);

        // Create GameState
        gameState = new GameState();
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
                Item itemDrop = player.removeItemFromInventory();
                itemDrop.getRect().setX(player.getRect().getX());
                itemDrop.getRect().setY(player.getRect().getY());
                currentRoom.addItem(itemDrop);
            }
        }
        //Opens/Closes inventory
        if (gameContainer.getInput().isKeyPressed(Input.KEY_I)) {
            gameState.toggleInventory();
        }
    }

    /**
     * Handles the player movement, or rather the movement of the world itself
     * @param direction The direction the player should be moving
     */
    private void movement(String direction, int delta) {
        double xMovement = 0;
        double yMovement = 0;
        switch (direction) {
            case "up":
                yMovement = player.getSpeed() * delta;
                animation = player.getUpAnimation();
                break;
            case "down":
                yMovement = -player.getSpeed() * delta;
                animation = player.getDownAnimation();
                break;
            case "left":
                xMovement = player.getSpeed() * delta;
                animation = player.getLeftAnimation();
                break;
            case "right":
                xMovement = -player.getSpeed() * delta;
                animation = player.getRightAnimation();
                break;
            default:
                break;
        }
        // Check if the movement will create any interceptions with blocks
        ArrayList<Rectangle> newBlocks = isBlocked(xMovement, yMovement);
        if (newBlocks != null) {
            currentRoom.updateRectangles(xMovement, yMovement, newBlocks);
        }
    }

    private ArrayList<Rectangle> isBlocked(double xMovement, double yMovement) {
        // Create a copy of the Blocks ArrayList
        ArrayList<Rectangle> newBlocks = new ArrayList<>();
        for (Rectangle block : currentRoom.getBlocks()) {
            newBlocks.add(new Rectangle(block.getX(), block.getY(), block.getWidth(), block.getHeight()));
        }

        // Updates blocks in newBlocks and checks if it intersects with player
        for (Rectangle block : newBlocks) {
            block.setX(block.getX() + (float)xMovement);
            block.setY(block.getY() + (float)yMovement);
            if (player.getRect().intersects(block)) {
                return null;
            }
        }
        return newBlocks;
    }

    /**
     * Checks if the player has intersected any exits and changes the room if that is the case
     */
    public void checkIntersectedExit() {
        Exit exit = player.getIntersectedExit(currentRoom.getExits());
        if (exit != null) {
            currentRoom = rooms.get(exit.getDestination());
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
            graphics.drawImage(item.getItemImage(), item.getRect().getX(), item.getRect().getY());
        }
    }

    /**
     * Draws highlighting on items that intersects with player's range
     */
    public void drawItemHighlighting() {
        for (Item item : player.getIntersectedItems(currentRoom.getItems())) {
            item.getItemFont().drawString(
                    item.getRect().getX(), item.getRect().getY(), item.getName(), Color.blue
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
        currentRoom.render(currentRoom.getBlocks().get(0).getX(), currentRoom.getBlocks().get(0).getY());

        // Draw the player animation
        animation.draw(player.getRect().getX()-16, player.getRect().getY()-16);

        drawItems(graphics);
        drawItemHighlighting();
        if (gameState.isInventoryOpen()) {
            drawInventory(graphics);
        }
    }

    public Room getCurrentRoom() { return currentRoom; }
}
