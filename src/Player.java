import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;

import java.util.ArrayList;

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
    public Player(String name, double xPos, double yPos, double width, double height, double speed,
                  double radius) {
        super(name, xPos, yPos, width, height);
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

    public void update(Graphics graphics, ArrayList<Item> items, ArrayList<Exit> exits) {
        graphics.fill(getRect());
        for (Item item : getIntersectedItems(items)) {
            graphics.fill(item.getRectangle());
            item.getItemFont().drawString(item.getRectangle().getX(), item.getRectangle().getY(),
                    item.getName(), Color.blue);
        }
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
