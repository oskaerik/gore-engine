import org.newdawn.slick.geom.Circle;

/**
 * The player class
 * @author Oskar Eriksson and Gustave Rousselet
 * @version 0.1
 */
public class Player extends Entity{

    private double speed;
    private Circle range;

    /**
     * Constructor of the player class
     * @param xPos center x position of the player in relation to the map
     * @param yPos center y position of the player in relation to the map
     * @param width width of the player
     * @param height height of the player
     * @param speed speed of the player (pixels/frame)
     */
    public Player(double xPos, double yPos, double width, double height, double speed, double radius) {
        super(xPos,yPos,width,height);
        this.speed = speed;
        range = new Circle(Game.WIDTH/2, Game.HEIGHT/2, (float)radius);
    }

    /**
     * Changes the players position according to input
     * @param input A string saying which direction to go. "up", "down", "left", "right"
     */
    public double movement(String input, int delta) {
        switch (input) {
            case "up":
                setYPosition(speed, delta);
                return speed * delta;
            case "down":
                setYPosition(-speed, delta);
                return -speed * delta;
            case "left":
                setXPosition(speed, delta);
                return speed * delta;
            case "right":
                setXPosition(-speed, delta);
                return -speed * delta;
            default:
                throw new IllegalArgumentException("Player class received invalid movement instructions");
        }
    }

    public Circle getRange() {
        return range;
    }
}
