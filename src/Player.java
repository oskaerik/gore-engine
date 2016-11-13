import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;

import java.util.ArrayList;

/**
 * The player class
 * @author Oskar Eriksson and Gustave Rousselet
 * @version 0.1
 */
public class Player {
    private double width;
    private double height;
    private double speed;
    private Rectangle rect;
    private Circle range;
    private Inventory inventory;
    private ArrayList<Animation> animationArray;

    /**
     * Constructor of the player class
     *
     * @param width  width of the player
     * @param height height of the player
     * @param speed  speed of the player (pixels/frame)
     */
    public Player(double width, double height, double speed, double radius) throws SlickException {
        this.width = width;
        this.height = height;
        this.speed = speed;

        // Creates the player rectangle and places it in the middle of the screen
        rect = new Rectangle((Core.WIDTH - (float) this.width) / 2, (Core.HEIGHT - (float) this.height) / 2,
                (int) this.width, (int) this.height);

        // Creates the range circle and places it in the middle of the screen
        range = new Circle((Core.WIDTH - (float) this.width) / 2, (Core.HEIGHT - (float) this.height) / 2, (float) radius);

        inventory = new Inventory();
        animationArray = Tools.createAnimation("character", "player");
    }

    /**
     * @return The entity rectangle
     */
    public Rectangle getRect() {
        return rect;
    }

    /**
     * @return Speed of the player
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * @return The player's inventory
     */
    public Inventory getInventory() {
        return inventory;
    }

    public void addItemToInventory(Item item) {
        inventory.addItem(item);
    }

    public Item removeItemFromInventory(int ItemNumber) {
        return inventory.removeItemNumber(ItemNumber);
    }

    public ArrayList<Item> getIntersectedItems(ArrayList<Item> items) {
        ArrayList<Item> intersectedItems = new ArrayList<>();
        for (Item item : items) {
            if (range.intersects(item.getRect())) {
                intersectedItems.add(item);
            }
        }
        return intersectedItems;
    }

    public Exit getIntersectedExit(ArrayList<Exit> exits) {
        for (Exit exit : exits) {
            if (getRect().intersects(exit.getRect())) {
                return exit;
            }
        }
        return null;
    }

    public Animation getStandingPlayer(String direction) throws SlickException {
        Image standingImage = null;
        switch (direction) {
            case "up":
                standingImage = animationArray.get(0).getImage(2);
                break;
            case "down":
                standingImage = animationArray.get(1).getImage(2);
                break;
            case "left":
                standingImage = animationArray.get(2).getImage(2);
                break;
            case "right":
                standingImage = animationArray.get(3).getImage(2);
                break;
        }
        Animation standingAnimation = new Animation();
        standingAnimation.addFrame(standingImage, 100);
        return standingAnimation;
    }

    public Animation getAnimation(String direction) {
        switch (direction) {
            case ("up"):
                return animationArray.get(0);
            case ("down"):
                return animationArray.get(1);
            case ("left"):
                return animationArray.get(2);
            case ("right"):
                return animationArray.get(3);
            default:
                return animationArray.get(1);
        }
    }
}
