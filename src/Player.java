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
     * @param width width of the player
     * @param height height of the player
     * @param speed speed of the player (pixels/frame)
     */
    public Player(double width, double height, double speed, double radius) throws SlickException {
        this.width = width;
        this.height = height;
        this.speed = speed;
        rect = new Rectangle(Core.WIDTH/2, Core.HEIGHT/2, (int) this.width, (int) this.height);
        range = new Circle(Core.WIDTH/2, Core.HEIGHT/2, (float)radius);
        inventory = new Inventory();
        animationArray = Character.createAnimation("player");
    }

    /**
     * @return The entity rectangle
     */
    public Rectangle getRect() { return rect; }

    /**
     * @return Speed of the player
     */
    public double getSpeed() { return speed; }

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

    public Animation getUpAnimation() { return animationArray.get(0); }

    public Animation getDownAnimation() { return animationArray.get(1); }

    public Animation getLeftAnimation() { return animationArray.get(2); }

    public Animation getRightAnimation() { return animationArray.get(3); }
}
