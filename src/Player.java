import org.newdawn.slick.geom.Rectangle;

/**
 * The player class
 * @author Oskar Eriksson and Gustave Rousselet
 * @version 0.1
 */
public class Player {
    private Rectangle rect;
    private double xPos;
    private double yPos;
    private double width;
    private double height;
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
        this.xPos = xPos;
        this.yPos = yPos;
        this.width = width;
        this.height = height;
        this.speed = speed;
        rect = new Rectangle(Game.WIDTH/2, Game.HEIGHT/2, (int)this.width, (int)this.height);
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

    /**
     * @return The player rectangle
     */
    public Rectangle getRect() { return rect; }

    /**
     * @return Returns the x-position of players center in relation to the map
     */
    public double getXPosition() {
        return xPos;
    }

    /**
     * @return Returns the y-position of players center in relation to the map
     */
    public double getYPosition() {
        return yPos;
    }

    /**
     * @return Returns the width of the player rectangle
     */
    public double getWidth() {
        return width;
    }

    /**
     * @return Returns the height of the player rectangle
     */
    public double getHeight() {
        return height;
    }
}
