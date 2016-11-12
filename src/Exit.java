import org.newdawn.slick.geom.Rectangle;

/**
 * The Exit class, containing exits
 * @author Oskar Eriksson and Gustave Rousselet
 * @version 0.1
 */
public class Exit {
    private Rectangle rectangle;
    private String destination;
    private int xSpawnPosition;
    private int ySpawnPosition;

    public Exit(Rectangle rectangle, String destination, int xSpawnPosition, int ySpawnPosition) {
        this.rectangle = rectangle;
        this.destination = destination;
        this.xSpawnPosition = xSpawnPosition;
        this.ySpawnPosition = ySpawnPosition;
    }

    public Rectangle getRect() {
        return rectangle;
    }

    public String getDestination() {
        return destination;
    }

    public int getXSpawnPosition() {
        return xSpawnPosition;
    }

    public int getYSpawnPosition() {
        return ySpawnPosition;
    }
}
