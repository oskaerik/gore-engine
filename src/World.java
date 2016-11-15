import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The World class contains all the objects in the world, updates them and checks for key input
 * @author Oskar Eriksson and Gustave Rousselet
 * @version 0.1
 */
public class World {
    // The player object
    private Player player;

    // Variables for rooms
    private Room currentRoom;
    private HashMap<String, Room> rooms;

    // GameState object, holding information about the state of the game,
    // if the inventory is open, if the player is in a dialogue etc
    private GameState gameState;

    // Debugging variable
    private boolean debug;

    /**
     * Constructor for the World class
     * @throws SlickException Generic exception
     */
    public World() throws SlickException {
        // The player object, takes parameters: width, height, speed, radius of range
        player = new Player(new Rectangle(Core.WIDTH/2 - 8, Core.HEIGHT/2 - 8, 16, 16), 0.2f, 48);

        // Create GameState
        gameState = new GameState();

        // Add rooms to HashMap rooms and set the starting room to current room
        rooms = new HashMap<>();
        rooms.put("center", new Room("res/rooms/center.tmx", "center"));
        rooms.put("north", new Room("res/rooms/north.tmx", "north"));
        currentRoom = rooms.get("center");

        debug = false;
    }

    /**
     * Checks for key presses and executes commands accordingly
     * @param gameContainer GameContainer object handling game loop etc
     * @param delta Amount of time that has passed since last updateGraphics (ms)
     */
    public void checkKeyPresses(GameContainer gameContainer, int delta) throws SlickException {
        // Depending on whether the inventory is open or not, move player or move the inventory marker
        if (gameState.getCurrentState().equals("default")) {
            player.setFrozen(true);
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
            Item drop = player.removeFromInventory(
                    player.getInventory().getInventorySelectedItemNumber());
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
            for (Item item : player.getItemsInRange(currentRoom.getItems())) {
                if (player.tryAddToInventory(item)) {
                    currentRoom.removeItem(item);
                }
            }
        }

        // Opens/closes inventory
        if (gameContainer.getInput().isKeyPressed(Input.KEY_I)) {
            player.getInventory().resetInventorySelectedItemNumber();
            gameState.toggleInventory();
            player.setFrozen(gameState.getCurrentState().equals("inventory"));
        }

        // Shoots fireball
        if (gameContainer.getInput().isKeyPressed(Input.KEY_M)) {
            for (Projectile projectile : currentRoom.getProjectiles()) {
                if (!projectile.isShot()
                        && gameState.getCurrentState().equals("default")
                        && projectile.getBelongsTo().equals("player")) {
                    projectile.shoot(player.getRect().getCenterX(), player.getRect().getCenterY(),
                            player.getLastDirection());
                }
            }
        }

        // Shoots enemy fireballs
        if (gameContainer.getInput().isKeyPressed(Input.KEY_N)) {
            for (Projectile fireball : currentRoom.getProjectiles()) {
                if (!fireball.isShot() && gameState.getCurrentState().equals("default")
                        && !fireball.getBelongsTo().equals("player")) {
                    fireball.shoot(currentRoom.getCharacterByName(fireball.getBelongsTo()).getRect().getCenterX(),
                            currentRoom.getCharacterByName(fireball.getBelongsTo()).getRect().getCenterY(),
                            currentRoom.getCharacterByName(fireball.getBelongsTo()).getLastDirection());
                }
            }
        }

        // Engage in dialogue
        if (gameContainer.getInput().isKeyPressed(Input.KEY_D)) {
            Character intersectedCharacter =
                    player.getCharacterInRange(currentRoom.getCharacters());
            if (gameState.getCurrentState().equals("default")
                    && intersectedCharacter != null) {
                gameState.toggleDialogue();
                Character inDialogueWith = intersectedCharacter;
                inDialogueWith.setInDialogue(true);
                player.setInDialogueWith(inDialogueWith);
            } else if (gameState.getCurrentState().equals("dialogue")) {
                for (Character character : currentRoom.getCharacters()) {
                    if (character.getInDialogue()) {
                        if (!character.increaseDialogIndex()) {
                            gameState.toggleDialogue();
                            character.setInDialogue(false);
                        }
                    }
                }
                player.setInDialogueWith(null);
            }
            player.setFrozen(gameState.getCurrentState().equals("dialogue"));
        }

        // Toggle debugging mode
        if (gameContainer.getInput().isKeyPressed(Input.KEY_P) && Core.DEBUG_ENABLED == 1) {
            debug = !debug;
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
                break;
            case "down":
                yMovement = -player.getSpeed() * delta;
                break;
            case "left":
                xMovement = player.getSpeed() * delta;
                break;
            case "right":
                xMovement = -player.getSpeed() * delta;
                break;
            default:
                break;
        }
        player.setLastDirection(direction);
        // Check if the movement will create any intersections with blocks
        ArrayList<Rectangle> newBlocks = isBlocked(xMovement, yMovement);
        if (newBlocks != null) {
            player.setFrozen(false);
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
            currentRoom.updateSpawnOffset(exit.getSpawnX(), exit.getSpawnY());
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
    public void updateWorld(GameContainer gameContainer, int delta) throws SlickException {
        checkEvents(gameContainer, delta);
        if (gameState.getCurrentState().equals("default")) {
            currentRoom.freezeEntities(false);
            currentRoom.updateEntities(delta, player, gameState);
        } else {
            currentRoom.freezeEntities(true);
        }
        if (gameState.getCurrentState().equals("gameover")) {
            gameContainer.exit(); 
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
        player.getAnimation(player).draw(player.getRect().getX()
                        +(player.getRect().getWidth()-player.getAnimation(player).getCurrentFrame().getWidth())/2,
                player.getRect().getY()
                        +(player.getRect().getHeight()-player.getAnimation(player).getCurrentFrame().getHeight())/2);

        // Highlight items in player range
        currentRoom.highlightItems(player.getRange());

        // Draw inventory depending of if inventory is open
        if (gameState.getCurrentState().equals("inventory")) {
            player.getInventory().drawInventory(graphics);
        }

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
        for (Rectangle block : currentRoom.getBlocks()) {
            graphics.fill(block);
        }
        for (Character character : currentRoom.getCharacters()) {
            graphics.fill(character.getRect());
        }
        for (Projectile fireball : currentRoom.getProjectiles()) {
            graphics.fill(fireball.getRect());
        }
        graphics.fill(getPlayer().getRect());

        if (getPlayer().getInDialogueWith() != null) {
            // If player is in dialogue, it prints which way the
            // player and the character that also is in dialogue is facing.
            ArrayList<String> facing = Tools.getFacing(
                    getPlayer().getRect(), getPlayer().getInDialogueWith().getRect());
            System.out.println("Player facing: " + facing.get(0) + "   "
                    + getPlayer().getInDialogueWith().getName() + " facing: " + facing.get(1));
        } else {
            // If not in dialogue, prints the top left corner coordinates. This makes it easier
            // to place world objects and exits in the right positions when creating worlds
            System.out.println("Top left corner coordinates  | "
                    + " x: " + currentRoom.getOffset()[0]
                    + " y: " + currentRoom.getOffset()[1]);
        }
    }
}
