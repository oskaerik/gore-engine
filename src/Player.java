import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.Animation;

import java.util.ArrayList;

/**
 * The Player class, the Player object is a child of the Character object. It also has a range
 * and holds information about who the player is in dialogue with.
 * @author Oskar Eriksson and Gustave Rousselet
 * @version 0.1
 */
public class Player extends Character {
    // The range of the player, how far the player can reach when picking up items
    // and engaging in dialogue
    private Circle range;

    // The player can be in dialogue with a specific character, this is null if not in dialogue
    private Character inDialogueWith;

    /**
     * Constructor of the Player class
     * @param rectangle The player's rectangle
     * @param speed The player's speed, how fast the world objects move
     * @param radius The radius of the range of the player
     * @throws SlickException Generic exception
     */
    public Player(Rectangle rectangle, float speed, float radius) throws SlickException {
        super(rectangle, "player", "The player", speed);

        // Creates the range circle and places it in the middle of the screen
        range = new Circle((Core.WIDTH - getRect().getWidth())/2,
                (Core.HEIGHT - getRect().getHeight())/2, radius);
        inDialogueWith = null;
    }

    /**
     * @return The player's range circle
     */
    public Circle getRange() {
        return range;
    }

    /**
     * Tries to add an item to the players inventory, returns true of possible, returns false if not
     * @param item The item that should be added
     * @return A boolean indicating whether it was possible to add the item to the player's inventory
     */
    public boolean tryAddToInventory(Item item) {
        if (getInventory().getItems().size() < 10) {
            getInventory().addItem(item);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Removes an item from the player's inventory, based on the index, and returns it
     * @param itemIndex The index of the item to be removed from the inventory
     * @return The item that was removed from the inventory
     */
    public Item removeFromInventory(int itemIndex) {
        if (getInventory().getItems().size() > 0 && getInventory().getItems().size() > itemIndex) {
            return getInventory().getItems().remove(itemIndex);
        } else {
            return null;
        }
    }

    /**
     * Returns an ArrayList of the items that the player's range intersects
     * @param items An ArrayList containing the items to be checked
     * @return An ArrayList with the intersected items
     */
    public ArrayList<Item> getItemsInRange(ArrayList<Item> items) {
        ArrayList<Item> intersectedItems = new ArrayList<>();
        for (Item item : items) {
            if (range.intersects(item.getRect())) {
                intersectedItems.add(item);
            }
        }
        return intersectedItems;
    }

    /**
     * Returns an ArrayList of the characters that the player's range intersects
     * @param characters An ArrayList containing the characters to be checked
     * @return The first character found
     */
    public Character getCharacterInRange(ArrayList<Character> characters) {
        for (Character character : characters) {
            if (range.intersects(character.getRect())) {
                return character;
            }
        }
        return null;
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
        if (inDialogueWith == null) {
            switch (direction) {
                case ("up"):
                    return getAnimationArray().get(0);
                case ("down"):
                    return getAnimationArray().get(1);
                case ("left"):
                    return getAnimationArray().get(2);
                case ("right"):
                    return getAnimationArray().get(3);
                default:
                    return getAnimationArray().get(1);
            }
        } else {
            return faceCharacter();
        }
    }

    private Animation faceCharacter() {
        if (inDialogueWith != null) {
            return Tools.getFreezeAnimation(getAnimationArray(),
                    Tools.getFacing(getRect(), inDialogueWith.getRect()).get(0));
        } else {
            return Tools.getFreezeAnimation(getAnimationArray(), "down");
        }
    }

    public void setInDialogueWith(Character character) {
        inDialogueWith = character;
    }

    public Character getInDialogueWith() { return inDialogueWith; }
}
