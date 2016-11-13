import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.Animation;
import org.newdawn.slick.geom.Vector2f;

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
    private boolean moving;

    private Character inDialogue;

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
        moving = false;

        inDialogue = null;
    }

    /**
     * @return The player rectangle
     */
    public Rectangle getRect() {
        return rect;
    }

    /**
     * @return The player rectangle
     */
    public Circle getRange() {
        return range;
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

    public boolean addToInventory(Item item) {
        if (inventory.getItems().size() < 10) {
            inventory.addItem(item);
            return true;
        } else {
            return false;
        }
    }

    public Item removeFromInventory(int ItemNumber) {
        if (inventory.getItems().size() > 0) {
            return inventory.removeItemNumber(ItemNumber);
        } else {
            return null;
        }
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

    public ArrayList<Character> getIntersectedCharacters(ArrayList<Character> characters) {
        ArrayList<Character> intersectedCharacters = new ArrayList<>();
        for (Character character : characters) {
            if (range.intersects(character.getRect())) {
                intersectedCharacters.add(character);
            }
        }
        return intersectedCharacters;
    }

    public Exit getIntersectedExit(ArrayList<Exit> exits) {
        for (Exit exit : exits) {
            if (getRect().intersects(exit.getRect())) {
                return exit;
            }
        }
        return null;
    }

    public Animation getAnimation(String direction) {
        if (inDialogue == null) {
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
        } else {
            return faceCharacter();
        }
    }

    private Animation faceCharacter() {
        return Tools.getFreezeAnimation(animationArray, "down");
    }

    public ArrayList<Animation> getAnimationArray() { return animationArray; }

    public void setInDialogue(Character character) {
        inDialogue = character;
    }

    public Character getInDialogue() { return inDialogue; }
}
