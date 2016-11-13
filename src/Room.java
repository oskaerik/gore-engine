import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.tiled.TiledMap;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * The Room class is the current map the player is in
 * @author Oskar Eriksson and Gustave Rousselet
 * @version 0.1
 */
public class Room {
    private TiledMap map;
    private final int spawnX;
    private final int spawnY;
    private ArrayList<Rectangle> blocks;
    private ArrayList<Exit> exits;
    private ArrayList<Item> items;
    private ArrayList<Character> characters;
    private ArrayList<Fireball> fireballs;
    private String name;

    /**
     * Constructor for the Room class
     * @param mapDirectory Name of the map .tmx-file in the map-folder
     * @param spawnX Player's spawn x-position
     * @param spawnY Player's spawn y-position
     */
    public Room(String mapDirectory, String mapName, int spawnX, int spawnY) throws SlickException {
        map = new TiledMap(mapDirectory);
        this.spawnX = spawnX;
        this.spawnY = spawnY;
        name = mapName;

        blocks = new ArrayList<>();
        exits = new ArrayList<>();
        items = new ArrayList<>();
        characters = new ArrayList<>();
        fireballs = new ArrayList<>();

        generateWorldObjects();
    }

    /**
     * Renders the map
     * @param worldX How much the world has moved in the x-direction
     * @param worldY How much the world has moved in the y-direction
     */
    public void render(double worldX, double worldY) {
        map.render((int)worldX-spawnX, (int)worldY-spawnY);
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

    public ArrayList<Fireball> getFireballs() {return fireballs; }

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
        //Update fireballs
        for (Fireball fireball : fireballs) {
            fireball.getRect().setX(fireball.getRect().getX() + (float)xMovement);
            fireball.getRect().setY(fireball.getRect().getY() + (float)yMovement);
        }
    }

    private void generateWorldObjects() throws SlickException {
        // Loop through all tiles in the map file
        for (int i = 0; i < map.getWidth(); i++) {
            for (int j = 0; j < map.getHeight(); j++) {
                int tileID = map.getTileId(i, j, 0);

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

                // Check for objects on object layer
                tileID = map.getTileId(i, j, 1);
                String itemName = map.getTileProperty(tileID, "ItemName", "");
                String itemPath = map.getTileProperty(tileID, "ItemPath", "");
                String itemDescription = map.getTileProperty(tileID, "ItemDescription", "");
                if (!itemPath.equals("")) {
                    items.add(new Item(new Rectangle((float)i * map.getTileWidth(), (float)j * map.getTileHeight(),
                            map.getTileWidth(), map.getTileHeight()), itemPath, itemName, itemDescription));
                }

                // Check for characters on characters layer
                tileID = map.getTileId(i, j, 2);
                String characterName = map.getTileProperty(tileID, "CharacterName", "");
                String characterDescription = map.getTileProperty(tileID, "CharacterDescription", "");
                if (!characterName.equals("")) {
                    characters.add(new Character(
                            new Rectangle((float)i * map.getTileWidth(), (float)j * map.getTileHeight(),
                            map.getTileWidth(), map.getTileHeight()), characterName, characterDescription));
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
}
