import org.newdawn.slick.geom.Rectangle;

/**
 * The Exit class, containing exits
 * @author Oskar Eriksson and Gustave Rousselet
 * @version 0.1
 */
public class Exit {
    private Rectangle rectangle;
    private String destination;
    private int spawnX;
    private int spawnY;

    public Exit(Rectangle rectangle, String destination, int spawnX, int spawnY) {
        this.rectangle = rectangle;
        this.destination = destination;
        this.spawnX = spawnX;
        this.spawnY = spawnY;
    }

    public Rectangle getRect() {
        return rectangle;
    }

    public String getDestination() {
        return destination;
    }

    public int getSpawnX() { return spawnX; }

    public int getSpawnY() {
        return spawnY;
    }
}
