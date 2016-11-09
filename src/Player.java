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
     * @param xPos x position of the player
     * @param yPos y position of the player
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
        rect = new Rectangle(0, 0, 0, 0);
    }

    /**
     * Changes the players position according to input
     * @param input A string saying which direction to go. "up", "down", "left", "right"
     */
    public void movement(String input) {
        switch (input) {
            case "up":
                yPos -= speed;
                break;
            case "down":
                yPos += speed;
                break;
            case "left":
                xPos -= speed;
                break;
            case "right":
                xPos += speed;
                break;
        }
    }

    /**
     * Updates and returns the player rectangle
     * @return The player rectangle
     */
    public Rectangle getRect() {
        rect.setCenterX((float)xPos);
        rect.setCenterY((float)yPos);
        rect.setSize((float)width, (float)height);
        return rect;
    }
}
