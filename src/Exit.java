import org.newdawn.slick.geom.Rectangle;

/**
 * The Exit class, containing exits. Exits have rectangles which are used for player intersection.
 * They also contain information about the x and y spawn location of their corresponding exit.
 *
 * @author Oskar Eriksson and Gustave Rousselet
 * @version 0.1
 */
public class Exit {
    private Rectangle rectangle;
    private String destination;
    private int spawnX;
    private int spawnY;

    /**
     * Constructor of the Exit object.
     *
     * @param rectangle Rectangle of the exit, for player intersection.
     * @param destination Name of the room the exit leads to.
     * @param spawnX X spawn location of the room the exit leads to.
     * @param spawnY Y spawn location of the room the exit leads to.
     */
    public Exit(Rectangle rectangle, String destination, int spawnX, int spawnY) {
        this.rectangle = rectangle;
        this.destination = destination;
        this.spawnX = spawnX;
        this.spawnY = spawnY;
    }

    /**
     * Method used to get the Rectangle of the exit.
     *
     * @return Rectangle of the exit.
     */
    public Rectangle getRect() {
        return rectangle;
    }

    /**
     * Method used to get the String name of the destination room.
     *
     * @return String name of the destination room.
     */
    public String getDestination() {
        return destination;
    }

    /**
     * Method used to get the x spawn location of the player of the destination room.
     *
     * @return Int x spawn location.
     */
    public int getSpawnX() { return spawnX; }

    /**
     * Method used to get the y spawn location of the player of the destination room.
     *
     * @return Int y spawn location.
     */
    public int getSpawnY() {
        return spawnY;
    }
}
