import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.tiled.TiledMap;

import java.util.ArrayList;
import java.util.Collections;

/**
 * The World class is the current map the player is in
 * @author Oskar Eriksson and Gustave Rousselet
 * @version 0.1
 */
public class World {
    private TiledMap map;
    private final int spawnX;
    private final int spawnY;
    private boolean blocked[][];
    private ArrayList<Rectangle> blocks;
    /**
     * Constructor for the World class
     * @param mapName Name of the map .tmx-file in the map-folder
     * @param spawnX Player's spawn x-position
     * @param spawnY Player's spawn y-position
     */
    public World(String mapName, int spawnX, int spawnY) throws SlickException {
        map = new TiledMap(mapName);
        this.spawnX = spawnX;
        this.spawnY = spawnY;

        blocked = new boolean[map.getWidth()][map.getHeight()];
        blocks = new ArrayList<>();
        for (int i = 0; i < map.getWidth(); i++) {
            for (int j = 0; j < map.getHeight(); j++) {
                int tileID = map.getTileId(i, j, 0);
                String value = map.getTileProperty(tileID, "Blocked", "false");
                if (value.equals("true")) {
                    blocked[i][j] = true;
                    blocks.add(new Rectangle((float)i * map.getTileWidth(), (float)j * map.getTileHeight(),
                            map.getTileWidth(), map.getTileHeight()));
                }
            }
        }
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

    public void updateCollisionRectangleX(double xChange) {
        for (Rectangle block : blocks) {
            block.setX(block.getX() + (float)xChange);
        }
    }

    public void updateCollisionRectangleY(double yChange) {
        for (Rectangle block : blocks) {
            block.setY(block.getY()+ (float)yChange);
        }
    }
}
