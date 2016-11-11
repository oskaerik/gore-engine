import org.newdawn.slick.geom.Rectangle;

/**
 * The player class
 * @author Oskar Eriksson and Gustave Rousselet
 * @version 0.1
 */
public class Entity {

    private Rectangle rect;
    private double xPos;
    private double yPos;
    private double width;
    private double height;

    public Entity(double xPos, double yPos, double width, double height) {
            this.xPos = xPos;
            this.yPos = yPos;
            this.width = width;
            this.height = height;
            rect = new Rectangle(Game.WIDTH/2, Game.HEIGHT/2, (int) this.width, (int) this.height);
    }

    /**
     * @return The entity rectangle
     */
    protected Rectangle getRect() { return rect; }

    /**
     * @return Returns the x-position of entity's center in relation to the map
     */
    protected double getXPosition() {
        return xPos;
    }

    /**
     * @return Returns the y-position of entity's center in relation to the map
     */
    protected double getYPosition() {
        return yPos;
    }

    /**
     * @return Returns the width of the entity's rectangle
     */
    protected double getWidth() {
        return width;
    }

    /**
     * @return Returns the height of the entity's rectangle
     */
    protected double getHeight() {return height;}

    /**
     * Sets the y-position
     * @param speed The speed to be moved
     * @param delta Delay in ms
     */
    protected void setYPosition(double speed, int delta) { yPos += speed * delta; }

    /**
     * Sets the x-position
     * @param speed The speed to be moved
     * @param delta Delay in ms
     */
    protected void setXPosition(double speed, int delta) { xPos += speed * delta; }
}
