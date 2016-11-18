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
     * @return Returns an ArrayList of exits, objects that the player collides with
     */
    public ArrayList<Exit> getExits() { return exits; }


    public ArrayList<Item> getItems() {return items; }

    public ArrayList<Character> getCharacters() {return characters; }

    public ArrayList<Projectile> getProjectiles() { return projectiles; }

    public String getName() { return name;}

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

    public void renderEntities(Graphics graphics, Player player, GameState gameState) {
        for (Item item : items) {
            item.getAnimationArray().get(0).draw(item.getRect().getX(), item.getRect().getY());
        }

        for (Character character : characters) {
            character.renderCharacter(player, graphics);
            character.drawHealth();
        }
        for (Projectile projectile : projectiles) {
            projectile.render();
        }
        EnemyShootFireballs(gameState);
    }

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
                checkIfAlive();
            }
            if (!projectile.getBelongsTo().equals("player") && projectile.getRect().intersects(player.getRect())
                    && projectile.isShot()) {
                projectile.setShot(false);
                player.takeDamage(projectile.getDamage());
                if (player.getHealth() <= 0) {
                    gamestate.gameOver();
                }
            }
        }
    }

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

    public void addItem(Item item) {
        items.add(item);
    }

    public void checkIfAlive() {
        Iterator<Character> it = characters.iterator();
        while (it.hasNext()) {
            Character character = it.next();
            if (character.getHealth() <= 0) {
                it.remove();
            }
        }
    }

    public void highlightItems(Circle playerRange) {
        for (Item item : items) {
            item.hightlight(playerRange);
        }
    }

    public void freezeEntities(boolean frozen) {
        for (Character character : characters) {
            character.setFrozen(frozen);
        }
        for (Projectile projectile : projectiles) {
            projectile.setFrozen(frozen);
        }
    }

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

    public double[] getOffset() {
        double[] toReturn = {xOffset, yOffset};
        return toReturn;
    }

    public Character getCharacterByName(String searchName) {
        for (Character character : characters) {
            if (character.getName().equals(searchName)) {
                return character;
            }
        }
        return null;
    }

    private void EnemyShootFireballs(GameState gameState) {
        for (Projectile fireball : projectiles) {
            if (getCharacterByName(fireball.getBelongsTo()) != null) {
                if (!fireball.isShot() && gameState.getCurrentState().equals("default")
                        && getCharacterByName(fireball.getBelongsTo()).getType().equals("enemy")) {
                    date = new Date();
                    if ((date.getTime() - fireball.getLastShot()) > 3000) {
                        fireball.toggleShotLastTime(date.getTime());
                        fireball.shoot(getCharacterByName(fireball.getBelongsTo()).getRect().getCenterX(),
                                getCharacterByName(fireball.getBelongsTo()).getRect().getCenterY(),
                                getCharacterByName(fireball.getBelongsTo()).getLastDirection());
                    }
                }
            }
        }
    }

    public void setCutsceneCharacter(String characterName) {
        cutsceneCharacter = characterName;
    }

    public String getCutsceneCharacter() { return cutsceneCharacter; }
}
