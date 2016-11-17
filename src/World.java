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

    // Debugging variable
    private boolean debug;

    private GameState gameState;

    /**
     * Constructor for the World class
     * @throws SlickException Generic exception
     */
    public World(GameState gameState) throws SlickException {
        // Get the GameState object
        this.gameState = gameState;

        // The player object, takes parameters: width, height, speed, radius of range
        player = new Player(new Rectangle(Core.WIDTH/2 - 8, Core.HEIGHT/2 - 8, 16, 16), 0.2f, 48);

        // Add rooms to HashMap rooms and set the starting room to current room
        rooms = generateRoomHashMap();

        debug = false;
    }

    /**
     * Generates a HashMap of the rooms in the rooms.txt-file
     * @return A HashMap of the rooms
     * @throws SlickException Generic exception
     */
    private HashMap<String,Room> generateRoomHashMap() throws SlickException {
        HashMap<String, Room> returnMap = new HashMap<>();

        // Reads the rooms.txt-file and loops through the lines
        ArrayList<String> roomsFile = Tools.readFileToArray("res/rooms/rooms.txt");
        for (String line : roomsFile) {
            String[] nameAndCutscene = line.split(" ");
            // Adds the path to the room to the HashMap
            returnMap.put(nameAndCutscene[0], new Room(
                    "res/rooms/" + nameAndCutscene[0] + ".tmx", nameAndCutscene[0]));
            if (nameAndCutscene.length > 1) {
                returnMap.get(nameAndCutscene[0]).setCutsceneCharacter(nameAndCutscene[1]);
            }
        }
        // Take the first line and set it as the first room
        currentRoom = returnMap.get(roomsFile.get(0).split(" ")[0]);
        return returnMap;
    }

    /**
     * Updates the world, like entity positions
     * @param delta Amount of time that has passed since last updateGraphics (ms)
     * @throws SlickException Generic exception
     */
    public void updateWorld(GameContainer gameContainer, int delta) throws SlickException {
        // Check for cutscene
        triggerCutscene();

        // Freeze the player in case the player isn't moving
        player.setFrozen(true);

        // Checks for key presses
        checkKeyPresses(gameContainer, delta);

        // Checks if the player has intersected any exits
        checkIntersectedExit();

        // Freeze or unfreeze the entities of the world depending on the game state
        if (gameState.getCurrentState().equals("default")) {
            currentRoom.freezeEntities(false);
            currentRoom.updateEntities(delta, player, gameState);
        } else {
            currentRoom.freezeEntities(true);
        }
    }

    /**
     * Checks for key presses and executes commands accordingly
     * @param gameContainer GameContainer object handling game loop etc
     * @param delta Amount of time that has passed since last updateGraphics (ms)
     * @throws SlickException Generic exception
     */
    public void checkKeyPresses(GameContainer gameContainer, int delta) throws SlickException {
        if (gameState.getCurrentState().equals("default")) {
            // If the GameState is default, check for movement key presses
            keyMovement(gameContainer, delta);
        } else if (gameState.getCurrentState().equals("inventory")) {
            // If the GameState is inventory, check for inventory navigation key presses
            keyInventory(gameContainer);
        }

        // Check action key presses
        keyActions(gameContainer, delta);
    }

    /**
     * Checks for movement key presses
     * @param gameContainer GameContainer object handling game loop etc
     * @param delta Amount of time that has passed since last updateGraphics (ms)
     * @throws SlickException Generic exception
     */
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

    /**
     * Checks for key presses for navigation of the inventory
     * @param gameContainer GameContainer object handling game loop etc
     */
    private void keyInventory(GameContainer gameContainer) {
        // Move up if there is an item above to mark
        if (gameContainer.getInput().isKeyPressed(Input.KEY_UP) &&
                player.getInventory().getInventorySelectedItemNumber() > 0) {
            player.getInventory().decreaseInventorySelectedItemNumber();
        }

        // Move down if there is an item below to mark
        if (gameContainer.getInput().isKeyPressed(Input.KEY_DOWN)) {
            if (player.getInventory().getInventorySelectedItemNumber()
                    < player.getInventory().getNumberOfItems()-1) {
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
                        && projectile.getBelongsTo().equals("player")
                        && player.getInventory().checkIfInventoryContains("Chest")) {
                    projectile.shoot(player.getRect().getCenterX(), player.getRect().getCenterY(),
                            player.getLastDirection());
                }
            }
        }

        // Engage in dialogue
        if (gameContainer.getInput().isKeyPressed(Input.KEY_D)) {
            // Get character in range to engage in dialogue with
            Character intersectedCharacter =
                    player.getCharacterInRange(currentRoom.getCharacters());

            // If there was a character in range and the GameState
            if (gameState.getCurrentState().equals("default")
                    && intersectedCharacter != null) {
                // Toggle dialogue GameSate and put player in dialogue with the character
                gameState.setDialogueMode(true);
                Character inDialogueWith = intersectedCharacter;
                inDialogueWith.setInDialogue(true);
                player.setInDialogueWith(inDialogueWith);
            } else if (gameState.getCurrentState().equals("dialogue")) {
                // If there is more dialogue, display the next thing the character says
                for (Character character : currentRoom.getCharacters()) {
                    if (character.getInDialogue()) {
                        if (!character.increaseDialogIndex()) {
                            gameState.setDialogueMode(false);
                            character.setInDialogue(false);

                            // When done with dialogue, set in dialogue with to null
                            player.setInDialogueWith(null);
                        }
                    }
                }

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

    /**
     * Returns a list of blocks with updated locations if they don't intersect with the player
     * @param xMovement The amount of movement in x direction
     * @param yMovement The amount of movement in y direction
     * @return List of blocks
     */
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
     * @return Return player object
     */
    public Player getPlayer() { return player; }

    public GameState getGameState() {
        return gameState;
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
        player.drawHealth();

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
        graphics.fill(player.getRange());
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

    /**
     * Triggers a cutscene in the given room if possible
     */
    public void triggerCutscene() {
        if (!currentRoom.getCutsceneCharacter().equals("")) {
            // Toggle dialogue GameSate and put player in dialogue with the character
            gameState.setDialogueMode(true);
            // Put the Character that the cutscene refers to as in dialogue with the player
            Character inDialogueWith = currentRoom.getCharacterByName(
                    currentRoom.getCutsceneCharacter());
            inDialogueWith.setInDialogue(true);
            player.setInDialogueWith(inDialogueWith);

            // Resets cutscene character in the room, so the cutscene isn't retriggered
            currentRoom.setCutsceneCharacter("");
            }
        }
}
