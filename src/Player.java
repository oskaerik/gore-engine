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
    private Animation upAnimation;
    private Animation downAnimation;
    private Animation rightAnimation;
    private Animation leftAnimation;

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
        upAnimation = createUpAnimation();
        downAnimation = createDownAnimation();
        rightAnimation = createRightAnimation();
        leftAnimation = createLeftAnimation();
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

    public Item removeItemFromInventory() {
        return inventory.removeItem();
    }

    public Circle getRange() {
        return range;
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

    public Animation createUpAnimation() throws SlickException {
        Image up1 = new Image("res/player/player-up-1.png");
        Image up2 = new Image("res/player/player-up-2.png");
        Image up3 = new Image("res/player/player-up-3.png");
        Image up4 = new Image("res/player/player-up-4.png");
        Animation upAnimation = new Animation();
        upAnimation.addFrame(up1, 100);
        upAnimation.addFrame(up2, 100);
        upAnimation.addFrame(up3, 100);
        upAnimation.addFrame(up4, 100);
        return upAnimation;
    }

    public Animation createDownAnimation() throws SlickException {
        Image down1 = new Image("res/player/player-down-1.png");
        Image down2 = new Image("res/player/player-down-2.png");
        Image down3 = new Image("res/player/player-down-3.png");
        Image down4 = new Image("res/player/player-down-4.png");
        Animation downAnimation = new Animation();
        downAnimation.addFrame(down1, 100);
        downAnimation.addFrame(down2, 100);
        downAnimation.addFrame(down3, 100);
        downAnimation.addFrame(down4, 100);
        return downAnimation;
    }

    public Animation createRightAnimation() throws SlickException {
        Image right1 = new Image("res/player/player-right-1.png");
        Image right2 = new Image("res/player/player-right-2.png");
        Image right3 = new Image("res/player/player-right-3.png");
        Image right4 = new Image("res/player/player-right-4.png");
        Animation rightAnimation = new Animation();
        rightAnimation.addFrame(right1, 100);
        rightAnimation.addFrame(right2, 100);
        rightAnimation.addFrame(right3, 100);
        rightAnimation.addFrame(right4, 100);
        return rightAnimation;
    }

    public Animation createLeftAnimation() throws SlickException {
        Image left1 = new Image("res/player/player-left-1.png");
        Image left2 = new Image("res/player/player-left-2.png");
        Image left3 = new Image("res/player/player-left-3.png");
        Image left4 = new Image("res/player/player-left-4.png");
        Animation leftAnimation = new Animation();
        leftAnimation.addFrame(left1, 100);
        leftAnimation.addFrame(left2, 100);
        leftAnimation.addFrame(left3, 100);
        leftAnimation.addFrame(left4, 100);
        return leftAnimation;
    }

    public Animation getStandingPlayer(String direction) throws SlickException {
        String imagePath = "";
        switch (direction) {
            case "down":
                imagePath = "res/player/player-down-3.png";
                break;
            case "up":
                imagePath = "res/player/player-up-3.png";
                break;
            case "left":
                imagePath = "res/player/player-left-3.png";
                break;
            case "right":
                imagePath = "res/player/player-right-3.png";
                break;
        }
        Animation standingAnimation = new Animation();
        standingAnimation.addFrame(new Image(imagePath), 100);
        return standingAnimation;
    }

    public Animation getUpAnimation() { return upAnimation; }

    public Animation getDownAnimation() { return downAnimation; }

    public Animation getRightAnimation() { return rightAnimation; }

    public Animation getLeftAnimation() { return leftAnimation; }
}
