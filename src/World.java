import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

/**
 * The World class is the current map the player is in
 * @author Oskar Eriksson and Gustave Rousselet
 * @version 0.1
 */
public class World {
    private TiledMap map;
    private final int spawnX;
    private final int spawnY;

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
    }

    public void render(double playerXPosition, double playerYPosition) {
        map.render((int)playerXPosition-spawnX, (int)playerYPosition-spawnY);
    }

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
}
