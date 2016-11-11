
/**
 * The player class
 * @author Oskar Eriksson and Gustave Rousselet
 * @version 0.1
 */
public class Player extends Entity{

    private double speed;

    /**
     * Constructor of the player class
     * @param xPos center x position of the player in relation to the map
     * @param yPos center y position of the player in relation to the map
     * @param width width of the player
     * @param height height of the player
     * @param speed speed of the player (pixels/frame)
     */
    public Player(double xPos, double yPos, double width, double height, double speed) {
        super(xPos,yPos,width,height);
        this.speed = speed;
    }

    /**
     * Changes the players position according to input
     * @param input A string saying which direction to go. "up", "down", "left", "right"
     */
    public double movement(String input, int delta) {
        switch (input) {
            case "up":
                yPos += speed * delta;
                return speed * delta;
            case "down":
                yPos -= speed * delta;
                return -speed * delta;
            case "left":
                xPos += speed * delta;
                return speed * delta;
            case "right":
                xPos -= speed * delta;
                return -speed * delta;
            default:
                throw new IllegalArgumentException("Player class received invalid movement instructions");
        }
    }
}
