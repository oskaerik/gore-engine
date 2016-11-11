import org.newdawn.slick.geom.Rectangle;

/**
 * The player class
 * @author Oskar Eriksson and Gustave Rousselet
 * @version 0.1
 */
public class Entity {

    public Rectangle rect;
    public double xPos;
    public double yPos;
    public double width;
    public double height;

    public Entity(double xPos, double yPos, double width, double height) {
            this.xPos = xPos;
            this.yPos = yPos;
            this.width = width;
            this.height = height;
            rect = new Rectangle(Game.WIDTH / 2, Game.HEIGHT / 2, (int) this.width, (int) this.height);
    }

    /**
     * @return The entity rectangle
     */
    public Rectangle getRect() { return rect; }

    /**
     * @return Returns the x-position of entity's center in relation to the map
     */
    public double getXPosition() {
        return xPos;
    }

    /**
     * @return Returns the y-position of entity's center in relation to the map
     */
    public double getYPosition() {
        return yPos;
    }

    /**
     * @return Returns the width of the entity's rectangle
     */
    public double getWidth() {
        return width;
    }

    /**
     * @return Returns the height of the entity's rectangle
     */
    public double getHeight() {return height;}
}
