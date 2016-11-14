import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The class for NPC characters
 * @author Oskar Eriksson and Gustave Rousselet
 * @version 0.1
 */
public class Character extends Entity {
    private ArrayList<Animation> animationArray;
    private ArrayList<String> movementPath;
    private ArrayList<String> movementArray;
    private Inventory items;

    String lastDirection;
    float speed;
    int health;

    private ArrayList<String> dialogueArray;
    private HashMap<Item, ArrayList<String>> dialogueMap;
    int dialogueIndex;
    boolean inDialogue;

    /**
     * Constructor for the character class
     * @param name The name of the character
     * @param description The description of the character
     */
    public Character(Rectangle rectangle, String name, String description) throws SlickException {
        super(rectangle, name, description);
        animationArray = Tools.createAnimation("character", getName());

        // The movementPath is the path of the character, does not change
        movementPath = Tools.readInstructions("movement", getName());
        // The movementArray changes when the character moves, resets to movementPath when done
        movementArray = new ArrayList<>(movementPath);

        dialogueArray = Tools.readInstructions("dialogue", getName());
        dialogueMap = new HashMap<>();
        dialogueMap.put(null, dialogueArray);
        dialogueIndex = 0;
        inDialogue = false;
        lastDirection = "down";
        speed = 0.1f;
        health = 100;
        items = new Inventory();
    }

    /**
     * @return Returns the animation according to the direction the character is facing
     */
    public Animation getAnimation(Player player, GameState gameState) {
        if (!isFrozen()) {
            switch (lastDirection) {
                case "up":
                    return animationArray.get(0);
                case "down":
                    return animationArray.get(1);
                case "left":
                    return animationArray.get(2);
                case "right":
                    return animationArray.get(3);
                default:
                    return animationArray.get(1);
            }
        } else {
            if (!inDialogue) {
                return Tools.getFreezeAnimation(animationArray, lastDirection);
            } else {
                return Tools.getFreezeAnimation(animationArray, facePlayer(player));
            }
        }
    }

    /**
     * Moves the character according to the movementArray
     */
    public void updateLocation(int delta) {
        if (movementArray != null) {
            for (int i = 0; i < movementArray.size(); i++) {
                String[] pixelsAndDirection = movementArray.get(i).split(" ");
                int pixels = Integer.parseInt(pixelsAndDirection[0]);
                if (pixels > 0) {
                    switch (pixelsAndDirection[1]) {
                        case "D":
                            getRect().setY(getRect().getY() + speed*delta);
                            lastDirection = "down";
                            break;
                        case "U":
                            getRect().setY(getRect().getY() - speed*delta);
                            lastDirection = "up";
                            break;
                        case "L":
                            getRect().setX(getRect().getX() - speed*delta);
                            lastDirection = "left";
                            break;
                        case "R":
                            getRect().setX(getRect().getX() + speed*delta);
                            lastDirection = "right";
                            break;
                        default:
                            break;
                    }

                    pixels--;
                    movementArray.set(i, Integer.toString(pixels) + " " + pixelsAndDirection[1]);
                    return;
                }
            }
            // Didn't find any more movement to do, reset movement
            movementArray = new ArrayList<>(movementPath);
        }
    }

    /**
     * Renders the character on the screen
     */
    public void renderCharacter(Player player, GameState gameState, Graphics graphics) {
        getAnimation(player, gameState).draw(
                getRect().getX()
                        +(getRect().getWidth()
                        - getAnimation(player, gameState).getCurrentFrame().getWidth())/2,
                getRect().getY()
                        + (getRect().getHeight()
                        - getAnimation(player, gameState).getCurrentFrame().getHeight())/2);
        if (inDialogue) {
            displayDialogue(graphics, player);
        }
    }

    /**
     * Decreases the characters health
     * @param damage The amount of health points to take away
     */
    public void takeDamage(int damage) {
        health -= damage;
    }

    /**
     * @return Current health of the character
     */
    public int getHealth() { return health; }

    public void displayDialogue(Graphics graphics, Player player) {
        // Look in player inventory to see if player is holding any relevant items
        boolean itemFound = false;
        for (Item item : player.getInventory().getItems()) {
            if (dialogueMap.containsKey(item)) {
                itemFound = true;
                dialogueArray = dialogueMap.get(item);
                Item itemRemoved = player.getInventory().removeItem(item);
                items.addItem(itemRemoved);
                dialogueMap.remove(item);
            }
        }
        //No relevant item found
        if (!itemFound) {
            dialogueArray = dialogueMap.get(null);
        }
        // Create dialogue rectangle
        Rectangle dialogueRectangle = new Rectangle(getRect().getCenterX()
                - getFont().getWidth(dialogueArray.get(dialogueIndex))/2,
                getRect().getY() - getFont().getHeight(dialogueArray.get(dialogueIndex)) - 10,
                getFont().getWidth(dialogueArray.get(dialogueIndex)), getFont().getHeight());
        graphics.setColor(Color.black);
        graphics.fill(dialogueRectangle);

        // Draw dialogue text in the rectangle
        getFont().drawString(dialogueRectangle.getX(), dialogueRectangle.getY(),
                dialogueArray.get(dialogueIndex), Color.white);
    }

    /**
     * @return Returns the way to face so that it resembles facing the player the most
     */
    public String facePlayer(Player player) {
        return Tools.getFacing(player.getRect(), getRect()).get(1);
    }

    public void setInDialogue(boolean value) {
        inDialogue = value;
        dialogueIndex++;
        if (dialogueIndex >= dialogueArray.size()) {
            dialogueIndex = 0;
        }
    }
}
