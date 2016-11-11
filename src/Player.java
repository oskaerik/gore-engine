import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;

import java.util.ArrayList;

/**
 * The player class
 * @author Oskar Eriksson and Gustave Rousselet
 * @version 0.1
 */
public class Player {

    private double xPos;
    private double yPos;
    private double width;
    private double height;
    private double speed;
    private Rectangle rect;
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
        this.xPos = xPos;
        this.yPos = yPos;
        this.width = width;
        this.height = height;
        this.speed = speed;
        rect = new Rectangle(Game.WIDTH/2, Game.HEIGHT/2, (int) this.width, (int) this.height);
        range = new Circle(Game.WIDTH/2, Game.HEIGHT/2, (float)radius);
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

    /**
     * Sets the y-position
     * @param speed The speed to be moved
     * @param delta Delay in ms
     */
    private void setYPosition(double speed, int delta) { yPos += speed * delta; }

    /**
     * Sets the x-position
     * @param speed The speed to be moved
     * @param delta Delay in ms
     */
    private void setXPosition(double speed, int delta) { xPos += speed * delta; }

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

    public void updateGraphics(Graphics graphics, ArrayList<Exit> exits) {
        graphics.fill(getRect());
    }

    public ArrayList<Item> getIntersectedItems(ArrayList<Item> items) {
        ArrayList<Item> intersectedItems = new ArrayList<>();
        for (Item item : items) {
            if (range.intersects(item.getRectangle())) {
                intersectedItems.add(item);
            }
        }
        return intersectedItems;
    }

    public Exit getIntersectedExit(ArrayList<Exit> exits) {
        for (Exit exit : exits) {
            if (getRect().intersects(exit.getRectangle())) {
                return exit;
            }
        }
        return null;
    }
}
