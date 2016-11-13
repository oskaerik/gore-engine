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
        animation = Tools.getFreezeAnimation(player.getAnimationArray(), "down");
        lastDirection = "down";


        // Create GameState
        gameState = new GameState();

        // Add rooms to HashMap rooms
        rooms = new HashMap<>();
        rooms.put("center", new Room("res/maps/center.tmx", "center", 0, 0));
        rooms.put("north", new Room("res/maps/north.tmx", "north", 0, 0));
        currentRoom = rooms.get("center");
    }

    /**
     * Checks for key presses and executes commands accordingly
     * @param gameContainer GameContainer object handling game loop etc
     * @param delta Amount of time that has passed since last updateGraphics (ms)
     */
    public void checkKeyPresses(GameContainer gameContainer, int delta) throws SlickException {
        animation = Tools.getFreezeAnimation(player.getAnimationArray(), lastDirection);

        // Depending on whether the inventory is open or not, move player or move the inventory marker
        if (gameState.getCurrentState().equals("default")) {
            keyMovement(gameContainer, delta);
        } else if (gameState.getCurrentState().equals("inventory")) {
            keyInventory(gameContainer, delta);
        }

        keyActions(gameContainer, delta);
    }


    public void keyMovement(GameContainer gameContainer, int delta) throws SlickException {
        if (gameContainer.getInput().isKeyDown(Input.KEY_UP)) {
            movement("up", delta);
        }
        if (gameContainer.getInput().isKeyDown(Input.KEY_DOWN)) {
            movement("down", delta);
        }
        if (gameContainer.getInput().isKeyDown(Input.KEY_LEFT)) {
            movement("left", delta);
        }
        if (gameContainer.getInput().isKeyDown(Input.KEY_RIGHT)) {
            movement("right", delta);
        }
    }

    private void keyInventory(GameContainer gameContainer, int delta) {
        if (gameContainer.getInput().isKeyPressed(Input.KEY_UP) &&
                player.getInventory().getInventorySelectedItemNumber() > 0) {
            player.getInventory().decreaseInventorySelectedItemNumber();
        }
        if (gameContainer.getInput().isKeyPressed(Input.KEY_DOWN)) {
            if (player.getInventory().getInventorySelectedItemNumber() < player.getInventory().getNumberOfItems()-1) {
                player.getInventory().increaseInventorySelectedItemNumber();
            }
        }
        // Drops items from inventory
        if (gameContainer.getInput().isKeyPressed(Input.KEY_A)) {
            Item drop = player.removeFromInventory(player.getInventory().getInventorySelectedItemNumber());
            if (drop != null) {
                drop.getRect().setX(player.getRect().getX());
                drop.getRect().setY(player.getRect().getY());
                currentRoom.addItem(drop);
            }
            // If the inventory contains items, the marker should change position
            if (player.getInventory().getInventorySelectedItemNumber() == 0) {
                player.getInventory().resetInventorySelectedItemNumber();
            }
            //If the inventory contains no items, marker should stay on first position
            if (player.getInventory().getInventorySelectedItemNumber() > 0) {
                player.getInventory().decreaseInventorySelectedItemNumber();
            }
        }
    }

    private void keyActions(GameContainer gameContainer, int delta) throws SlickException {
        // Adds items in range to inventory if player is carrying less than allowed amount of items
        if (gameContainer.getInput().isKeyPressed(Input.KEY_SPACE)) {
            for (Item item : player.getIntersectedItems(currentRoom.getItems())) {
                if (player.addToInventory(item)) {
                    currentRoom.removeItem(item);
                }
            }
        }

        // Opens/closes inventory
        if (gameContainer.getInput().isKeyPressed(Input.KEY_I)) {
            player.getInventory().resetInventorySelectedItemNumber();

            gameState.toggleInventory();
        }

        // Shoots fireball
        if (gameContainer.getInput().isKeyPressed(Input.KEY_M)) {
            if (!currentRoom.getProjectiles().get(0).isShot()
                    && gameState.getCurrentState().equals("default")) {
                for (Projectile projectile : currentRoom.getProjectiles()) {
                    projectile.shoot(player.getRect().getCenterX(), player.getRect().getCenterY(),
                            lastDirection);
                }
            }
        }

        // Engage in dialogue
        if (gameContainer.getInput().isKeyPressed(Input.KEY_D)) {
            ArrayList<Character> intersectedCharacters =
                    player.getIntersectedCharacters(currentRoom.getCharacters());
            if (gameState.getCurrentState().equals("default")
                    && intersectedCharacters.size() > 0) {
                gameState.toggleDialogue();
                intersectedCharacters.get(0).setInDialogue(true);
                player.setInDialogue(intersectedCharacters.get(0));
            } else if (gameState.getCurrentState().equals("dialogue")) {
                gameState.toggleDialogue();
                for (Character character : currentRoom.getCharacters()) {
                    character.setInDialogue(false);
                }
                player.setInDialogue(null);
            }
        }
    }

    /**
     * Handles the player movement, or rather the movement of the world itself
     * @param direction The direction the player should be moving
     */
    private void movement(String direction, int delta) throws SlickException {
        double xMovement = 0;
        double yMovement = 0;
        switch (direction) {
            case "up":
                yMovement = player.getSpeed() * delta;
                lastDirection = "up";
                break;
            case "down":
                yMovement = -player.getSpeed() * delta;
                lastDirection = "down";
                break;
            case "left":
                xMovement = player.getSpeed() * delta;
                lastDirection = "left";
                break;
            case "right":
                xMovement = -player.getSpeed() * delta;
                lastDirection = "right";
                break;
            default:
                break;
        }
        // Check if the movement will create any intersections with blocks
        ArrayList<Rectangle> newBlocks = isBlocked(xMovement, yMovement);
        if (newBlocks != null) {
            animation = player.getAnimation(lastDirection);
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
     * Checks for events, such as key presses and intersected exits
     * @param gameContainer
     * @param delta
     * @throws SlickException
     */
    public void checkEvents(GameContainer gameContainer, int delta) throws SlickException {
        checkKeyPresses(gameContainer, delta);
        checkIntersectedExit();
    }

    public Room getCurrentRoom() { return currentRoom; }
    public Player getPlayer() { return player; }

    /**
     * Updates the world, like entity positions
     * @param delta Amount of time that has passed since last updateGraphics (ms)
     * @throws SlickException Generic exception
     */
    public void updateWorld(int delta) throws SlickException {
        if (gameState.getCurrentState().equals("default")) {
            currentRoom.freezeEntities(false);
            currentRoom.updateEntities(delta);
        } else {
            currentRoom.freezeEntities(true);
        }
    }

    /**
     * Updates the graphics of the game world
     * @param graphics Graphics component used to draw
     */
    public void updateGraphics(Graphics graphics) {
        // Draw the world
        currentRoom.render(
                currentRoom.getBlocks().get(0).getX(), currentRoom.getBlocks().get(0).getY());

        // Render the entities (characters, items, projectiles) in room
        currentRoom.renderEntities(graphics, player, gameState);

        // Draw the player animation
        if (player.getInDialogue() != null) {
            animation = player.getAnimation(lastDirection);
        }
        animation.draw(player.getRect().getX()
                        +(player.getRect().getWidth()-animation.getCurrentFrame().getWidth())/2,
                player.getRect().getY()
                        +(player.getRect().getHeight()-animation.getCurrentFrame().getHeight())/2);

        // Highlight items in player range
        currentRoom.highlightItems(player.getRange());

        // Draw inventory depending of if inventory is open
        if (gameState.getCurrentState().equals("inventory")) {
            player.getInventory().drawInventory(graphics);
        }
    }
}
