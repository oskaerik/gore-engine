import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.tiled.TiledMap;

import java.util.Date;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * The Room class is the current map the player is in
 * @author Oskar Eriksson and Gustave Rousselet
 * @version 0.1
 */
public class Room {
    // The map of the room
    private TiledMap map;

    // The blocked tiles of the room, the exits, the items, the players and the projectiles
    private ArrayList<Rectangle> blocks;
    private ArrayList<Exit> exits;
    private ArrayList<Item> items;
    private ArrayList<Character> characters;
    private ArrayList<Projectile> projectiles;

    // Name of the room
    private String name;

    // The current offset of the room, how much it has moved (when the player moves"
    private double xOffset;
    private double yOffset;

    // Date for time handling
    private Date date;

    // The character with whom the cutscene dialogue should be with
    private String cutsceneCharacter;

    // Time when the player entered the room
    private long enterTime;

    /**
     * @param mapDirectory Name of the map .tmx-file in the map-folder
     * @param name Name of the map
     * @throws SlickException Generic exception
     */
    public Room(String mapDirectory, String name) throws SlickException {
        map = new TiledMap(mapDirectory);
        this.name = name;

        blocks = new ArrayList<>();
        exits = new ArrayList<>();
        items = new ArrayList<>();
        characters = new ArrayList<>();

        projectiles = new ArrayList<>();
        // The player's projectile
        projectiles.add(new Projectile(
                new Rectangle(0, 0, 44, 42), "fireball", "player", 10, 0.35f));

        xOffset = 0;
        yOffset = 0;
        date = new Date();
        enterTime = date.getTime();
        cutsceneCharacter = "";

        // Generate the world objects
        generateWorldObjects();
        }

    /**
     * Renders the map
     * @param worldX How much the world has moved in the x-direction
     * @param worldY How much the world has moved in the y-direction
     */
    public void render(double worldX, double worldY) {
        map.render((int)worldX, (int)worldY);
    }

    /**
     * @return Returns an ArrayList of blocks, objects that the player collides with
     */
    public ArrayList<Rectangle> getBlocks() { return blocks; }

    /**
     * @return Returns an ArrayList of exits in the room
     */
    public ArrayList<Exit> getExits() { return exits; }

    /**
     * @return Returns an ArrayList of items in the room
     */
    public ArrayList<Item> getItems() {return items; }

    /**
     * @return Returns an ArrayList of characters in the room
     */
    public ArrayList<Character> getCharacters() {return characters; }

    /**
     * @return Returns an ArrayList of projectiles in the room
     */
    public ArrayList<Projectile> getProjectiles() { return projectiles; }

    /**
     * @return Returns the name of the room
     */
    public String getName() { return name;}

    /**
     * Updates all the rectangles in the rooms, i.e. the blocks, the items
     * the characters and so on
     * @param xMovement How much the rectangles should move in the x-direction
     * @param yMovement How much the rectangles should move in the y-direction
     * @param newBlocks An ArrayList containing the new collision blocks
     */
    public void updateRectangles(double xMovement, double yMovement, ArrayList<Rectangle> newBlocks) {
        // Update blocks
        blocks = newBlocks;
        // Update exits
        for (Exit exit : exits) {
            exit.getRect().setX(exit.getRect().getX() + (float)xMovement);
            exit.getRect().setY(exit.getRect().getY() + (float)yMovement);
        }
        // Update items
        for (Item item : items) {
            item.getRect().setX(item.getRect().getX() + (float)xMovement);
            item.getRect().setY(item.getRect().getY() + (float)yMovement);
        }
        // Update characters
        for (Character character : characters) {
            character.getRect().setX(character.getRect().getX() + (float)xMovement);
            character.getRect().setY(character.getRect().getY() + (float)yMovement);
        }
        //Update projectiles
        for (Projectile projectile : projectiles) {
            projectile.getRect().setX(projectile.getRect().getX() + (float)xMovement);
            projectile.getRect().setY(projectile.getRect().getY() + (float)yMovement);
        }

        xOffset += (float)xMovement;
        yOffset += (float)yMovement;
    }

    /**
     * Renders the entities in the room,  characters and items and so on
     * @param graphics Graphics component used for drawing
     * @param player The Player object
     * @param gameState The GameState object
     */
    public void renderEntities(Graphics graphics, Player player, GameState gameState) {
        // Loop through items and render
        for (Item item : items) {
            item.getAnimationArray().get(0).draw(item.getRect().getX(), item.getRect().getY());
        }

        // Loop through characters and render, also health
        for (Character character : characters) {
            character.renderCharacter(player, graphics);
            character.drawHealth();
        }

        // Loop through projectiles and render
        for (Projectile projectile : projectiles) {
            projectile.render();
        }

        // Run method for enemies shooting fireballs
        EnemyShootFireballs(gameState);
    }

    /** Updates the characters who are moving in the room
     * @param delta Amount of time that has passed since last updateGraphics (ms).
     * @param player Player object
     * @param gamestate GameState object
     */
    public void updateEntities(int delta, Player player, GameState gamestate) {
        // Update character positions
        if (characters.size() > 0) {
            for (Character character : characters) {
                character.updateLocation(delta);
            }
        }

        // Place this in projectiles
        // Update projectiles positions
        for (Projectile projectile : projectiles) {
            Character hitCharacter = projectile.moveProjectile(getBlocks(), getCharacters(), delta);
            if (hitCharacter != null) {
                hitCharacter.takeDamage(projectile.getDamage());
                checkIfAlive(gamestate);
            }
            if (!projectile.getBelongsTo().equals("player")
                    && projectile.getRect().intersects(player.getRect()) && projectile.isShot()) {
                projectile.setShot(false);
                player.takeDamage(projectile.getDamage());
            }
        }
    }

    /**
     * Generates all the objects in the world, looping through the tiles in the map from Tiled
     * @throws SlickException Generic exception
     */
    private void generateWorldObjects() throws SlickException {
        // Loop through all tiles in the map file
        for (int i = 0; i < map.getWidth(); i++) {
            for (int j = 0; j < map.getHeight(); j++) {
                int tileID = map.getTileId(i, j, 1);

                // Check for blocked tiles
                String value = map.getTileProperty(tileID, "Blocked", "false");
                if (value.equals("true")) {
                    blocks.add(new Rectangle((float)i * map.getTileWidth(),
                            (float)j * map.getTileHeight(),
                            map.getTileWidth(), map.getTileHeight()));
                }

                // Check for exit tiles
                String destination = map.getTileProperty(tileID, "Exit", "");
                if (!destination.equals("")) {
                    int spawnXPosition = Integer.parseInt(map.getTileProperty(tileID, "SpawnX", ""));
                    int spawnYPosition = Integer.parseInt(map.getTileProperty(tileID, "SpawnY", ""));
                    exits.add(new Exit(new Rectangle((float)i * map.getTileWidth(),
                            (float)j * map.getTileHeight(),
                            map.getTileWidth(), map.getTileHeight()), destination, spawnXPosition, spawnYPosition));
                }

                // Check for objects on item layer
                tileID = map.getTileId(i, j, 2);
                String itemName = map.getTileProperty(tileID, "ItemName", "");
                if (!itemName.equals("")) {
                    // Create the character rectangle (SET SIZE ACCORDING TO PROPERTIES)
                    Rectangle itemRectangle = new Rectangle(
                            (float)i * map.getTileWidth(), (float)j * map.getTileHeight(),
                            map.getTileWidth(), map.getTileHeight());
                    items.add(new Item(itemRectangle, itemName));
                }

                // Check for characters on characters layer
                tileID = map.getTileId(i, j, 3);
                String characterName = map.getTileProperty(tileID, "CharacterName", "");
                String characterType = map.getTileProperty(tileID, "CharacterType", "");
                if (!characterName.equals("")) {
                    // Create the character rectangle (SET SIZE ACCORDING TO PROPERTIES)
                    Rectangle characterRectangle = new Rectangle(
                            (float)i * map.getTileWidth(), (float)j * map.getTileHeight(),
                            map.getTileWidth(), map.getTileHeight());
                    // Add the character to the room's characters
                    Character characterToBeAdded = new Character(characterRectangle, characterName, characterType, 0.1f);
                    characters.add(characterToBeAdded);
                    projectiles.add(new Projectile(new Rectangle(0, 0, 44, 42),
                            "fireball", characterName, 20, 0.5f));
                }
            }
        }
    }

    /**
     * Removes an item from the room
     * @param item The item to be removed
     */
    public void removeItem(Item item) {
        Iterator<Item> it = items.iterator();
        while (it.hasNext()) {
            Item nextItem = it.next();
            if (item.equals(nextItem)) {
                it.remove();
                return;
            }
        }
    }

    /**
     * Adds an item to the room
     * @param item The item to be added
     */
    public void addItem(Item item) {
        items.add(item);
    }

    /**
     * Checks if all characters are alive, if a characters health is zero or below,
     * the character is removed from the room
     */
    public void checkIfAlive(GameState gameState) {
        Iterator<Character> it = characters.iterator();
        while (it.hasNext()) {
            Character character = it.next();
            if (character.getHealth() <= 0) {
                it.remove();

                // Check if the character was a win condition
                String[] winCondition = Tools.getWinCondition("res/rooms/win.txt");
                if (getName().equals(winCondition[0])
                        && character.getName().equals(winCondition[1])) {
                    gameState.winGame();
                }
            }
        }
    }

    /**
     * Highlight all the items in the player's range
     * @param playerRange The player's range
     */
    public void highlightItems(Circle playerRange) {
        for (Item item : items) {
            item.hightlight(playerRange);
        }
    }

    /**
     * Freeze or unfreeze entities in the room
     * @param frozen Value stating whether the entities should be frozen
     */
    public void freezeEntities(boolean frozen) {
        for (Character character : characters) {
            character.setFrozen(frozen);
        }
        for (Projectile projectile : projectiles) {
            projectile.setFrozen(frozen);
        }
    }

    /**
     * Used when the player changes room, sets the new locations of the blocks and entities
     * in the room to correct location according to the spawn location
     * @param spawnX
     * @param spawnY
     */
    public void updateSpawnOffset(float spawnX, float spawnY) {
        // Set block locations
        for (Rectangle block : blocks) {
            block.setX(block.getX() - (float)xOffset + spawnX);
            block.setY(block.getY() - (float)yOffset + spawnY);
        }
        // Set exit locations
        for (Exit exit : exits) {
            exit.getRect().setX(exit.getRect().getX() - (float)xOffset + spawnX);
            exit.getRect().setY(exit.getRect().getY() - (float)yOffset + spawnY);
        }
        // Set item locations
        for (Item item : items) {
            item.getRect().setX(item.getRect().getX() - (float)xOffset + spawnX);
            item.getRect().setY(item.getRect().getY() - (float)yOffset + spawnY);
        }
        // Set character locations
        for (Character character : characters) {
            character.getRect().setX(character.getRect().getX() - (float)xOffset + spawnX);
            character.getRect().setY(character.getRect().getY() - (float)yOffset + spawnY);
        }
        // Set projectile locations
        for (Projectile projectile : projectiles) {
            projectile.getRect().setX(projectile.getRect().getX() - (float)xOffset + spawnX);
            projectile.getRect().setY(projectile.getRect().getY() - (float)yOffset + spawnY);
        }
        xOffset = spawnX;
        yOffset = spawnY;
    }

    /**
     * @return The current offset, how much the player has moved inside the room
     */
    public double[] getOffset() {
        double[] toReturn = {xOffset, yOffset};
        return toReturn;
    }

    /**
     * Returns a character by the name of the character
     * @param searchName The name of the character to be returned
     * @return The character with a matching name
     */
    public Character getCharacterByName(String searchName) {
        for (Character character : characters) {
            if (character.getName().equals(searchName)) {
                return character;
            }
        }
        return null;
    }

    /**
     * Makes enemies shoot fireballs
     * @param gameState GameState object
     */
    private void EnemyShootFireballs(GameState gameState) {
        for (Projectile projectile : projectiles) {
            if (getCharacterByName(projectile.getBelongsTo()) != null) {
                // If the character's projectile isn't shot and the character is an enemy
                // and the current state is the default sate
                if (!projectile.isShot() && gameState.getCurrentState().equals("default")
                        && getCharacterByName(projectile.getBelongsTo()).getType().equals("enemy")) {
                    date = new Date();
                    // Check if the time elapsed since the last projectile shot is more than
                    // a given amount of time and that the player just didn't enter the room
                    if ((date.getTime() - projectile.getLastShot())
                            > getCharacterByName(projectile.getBelongsTo()).getShootingInterval()
                            && (date.getTime() - enterTime) > 1000) {
                        // Set the current time to "the last time shot" and shoot a new projectile
                        projectile.toggleShotLastTime(date.getTime());
                        projectile.shoot(
                                getCharacterByName(projectile.getBelongsTo()).getRect().getCenterX(),
                                getCharacterByName(projectile.getBelongsTo()).getRect().getCenterY(),
                                getCharacterByName(projectile.getBelongsTo()).getLastDirection());
                    }
                }
            }
        }
    }

    /**
     * Sets the character whom a cutscene should be triggered with upon entering a room
     * @param characterName The name of the character for the cutscene
     */
    public void setCutsceneCharacter(String characterName) {
        cutsceneCharacter = characterName;
    }

    /**
     * @return The cutscene character
     */
    public String getCutsceneCharacter() { return cutsceneCharacter; }

    public void setEnterTime(long enterTime) {
        this.enterTime = enterTime;
    }
}
