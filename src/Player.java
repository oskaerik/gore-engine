import org.newdawn.slick.Graphics;
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

    private double xPos;
    private double yPos;
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
    private Animation defaultAnimation;

    /**
     * Constructor of the player class
     * @param xPos center x position of the player in relation to the map
     * @param yPos center y position of the player in relation to the map
     * @param width width of the player
     * @param height height of the player
     * @param speed speed of the player (pixels/frame)
     */
    public Player(double xPos, double yPos, double width, double height, double speed, double radius) throws SlickException {
        this.xPos = xPos;
        this.yPos = yPos;
        this.width = width;
        this.height = height;
        this.speed = speed;
        rect = new Rectangle(Game.WIDTH/2, Game.HEIGHT/2, (int) this.width, (int) this.height);
        range = new Circle(Game.WIDTH/2, Game.HEIGHT/2, (float)radius);
        inventory = new Inventory();
        upAnimation = createUpAnimation();
        downAnimation = createDownAnimation();
        rightAnimation = createRightAnimation();
        leftAnimation = createLeftAnimation();
        defaultAnimation = createDefaultAnimation();
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

    public void setPlayerXPosition(double xPos) {
        this.xPos  = xPos;
    }

    public void setPlayYPosition(double yPos) {
        this.yPos = yPos;
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

    public Animation createDefaultAnimation() throws SlickException {
        Image default1 = new Image("res/player/player-down-3.png");
        Animation defaultAnimation = new Animation();
        defaultAnimation.addFrame(default1, 100);
        return defaultAnimation;
    }

    public Animation getUpAnimation() { return upAnimation; }

    public Animation getDownAnimation() { return downAnimation; }

    public Animation getRightAnimation() { return rightAnimation; }

    public Animation getLeftAnimation() { return leftAnimation; }

    public Animation getDefaultAnimation() { return defaultAnimation; }
}
