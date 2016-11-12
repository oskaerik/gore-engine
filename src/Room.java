import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.Image;

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

        generateWorldObjects();
    }

    /**
     * Renders the map
     * @param playerXPosition Player's current x-position
     * @param playerYPosition Player's current y-position
     */
    public void render(double playerXPosition, double playerYPosition) {
        map.render((int)playerXPosition-spawnX, (int)playerYPosition-spawnY);
    }

    /**
     * @return Returns an ArrayList of blocks, objects that the player collides with
     */
    public ArrayList<Rectangle> getBlocks() { return blocks; }

    /**
     * @return Returns an ArrayList of exits, objects that the player collides with
     */
    public ArrayList<Exit> getExits() { return exits; }


    public ArrayList<Item> getItems() {return items;}
    /**
     * @return Returns the TiledMap object
     */
    public TiledMap getMap() { return map; }

    /**
     * @return Returns the player's spawn x-position
     */
    public int getSpawnX() { return spawnX; }

    /**
     * @return Returns the player's spawn y-position
     */
    public int getSpawnY() { return spawnY; }

    public String getName() { return name;}

    public void updateRectanglesX(double xChange) {
        for (Rectangle block : blocks) {
            block.setX(block.getX() + (float)xChange);
        }
        for (Exit exit : exits) {
            exit.getRectangle().setX(exit.getRectangle().getX() + (float)xChange);
        }
        for (Item item : items) {
            item.getRectangle().setX(item.getRectangle().getX() + (float)xChange);
        }
    }

    public void updateRectanglesY(double yChange) {
        for (Rectangle block : blocks) {
            block.setY(block.getY()+ (float)yChange);
        }
        for (Exit exit : exits) {
            exit.getRectangle().setY(exit.getRectangle().getY() + (float)yChange);
        }
        for (Item item : items) {
            item.getRectangle().setY(item.getRectangle().getY() + (float)yChange);
        }
    }

    private void generateWorldObjects() throws SlickException {
        for (int i = 0; i < map.getWidth(); i++) {
            for (int j = 0; j < map.getHeight(); j++) {
                int tileID = map.getTileId(i, j, 0);
                String value = map.getTileProperty(tileID, "Blocked", "false");
                if (value.equals("true")) {
                    blocks.add(new Rectangle((float)i * map.getTileWidth()+Game.WIDTH/2-spawnX,
                            (float)j * map.getTileHeight()+Game.HEIGHT/2-spawnY,
                            map.getTileWidth(), map.getTileHeight()));
                }

                String destination = map.getTileProperty(tileID, "Exit", "");
                if (!destination.equals("")) {
                    int spawnXPosition = Integer.parseInt(map.getTileProperty(tileID, "SpawnX", ""));
                    int spawnYPosition = Integer.parseInt(map.getTileProperty(tileID, "SpawnY", ""));
                    exits.add(new Exit(new Rectangle((float)i * map.getTileWidth()+Game.WIDTH/2-spawnX,
                            (float)j * map.getTileHeight()+Game.HEIGHT/2-spawnY,
                            map.getTileWidth(), map.getTileHeight()), destination, spawnXPosition, spawnYPosition));
                }
                tileID = map.getTileId(i, j, 1);
                String itemDirectory = map.getTileProperty(tileID, "ItemDirectory", "");
                String itemName = map.getTileProperty(tileID, "ItemName", "");
                if (!itemDirectory.equals("")) {
                    items.add(new Item(new Rectangle((float)i * map.getTileWidth()+Game
                            .WIDTH/2-spawnX, (float)j * map.getTileHeight()+Game.HEIGHT/2-spawnY,
                            map.getTileWidth(), map.getTileHeight()), new Image(itemDirectory),
                            itemDirectory, itemName));
                }
            }
        }
    }

    public void removeItems(ArrayList<Item> intersectedItems) {
        Iterator<Item> it = items.iterator();
        while (it.hasNext()) {
            Item nextItem = it.next();
            if (intersectedItems.contains(nextItem)) {
                it.remove();
            }
        }
    }

    public void addItem(Item item) {
        items.add(item);
    }
}
