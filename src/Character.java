import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * The class for NPC characters
 * @author Oskar Eriksson and Gustave Rousselet
 * @version 0.1
 */
public class Character extends Entity {
    private ArrayList<String> movementPath;
    private ArrayList<String> movementArray;
    private Inventory inventory;

    private String lastDirection;
    private float speed;
    private int health;

    private ArrayList<String> currentDialogueArray;
    private HashMap<String, ArrayList<String>> dialogueMap;
    private int dialogueIndex;
    private boolean inDialogue;
    private String type;

    /**
     * Constructor for the character class
     *
     * @param name        The name of the character
     * @param description The description of the character
     */
    public Character(Rectangle rectangle, String name, String description, String type, float speed)
            throws SlickException {
        super(rectangle, name, description, "character");

        // The movementPath is the path of the character, does not change
        movementPath = Tools.readFileToArray("res/characters/" + getName() + "/movement.txt");
        // The movementArray changes when the character moves, resets to movementPath when done
        if (!name.equals("player")) {
            movementArray = new ArrayList<>(movementPath);
            dialogueMap = generateDialogueFromArray(
                    Tools.readFileToArray("res/characters/" + getName() + "/dialogue.txt"));
            currentDialogueArray = dialogueMap.get("null");
        }

        dialogueIndex = 0;
        inDialogue = false;
        lastDirection = "down";
        this.speed = speed;
        health = 100;
        inventory = new Inventory();
        this.type = type;
    }

    /**
     * @return Returns the animation according to the direction the character is facing
     */
    public Animation getAnimation(Player player) {
        if (!isFrozen()) {
            return getRegularAnimation();
        } else {
            if (!inDialogue) {
                return Tools.getFreezeAnimation(getAnimationArray(), lastDirection);
            } else {
                return Tools.getFreezeAnimation(getAnimationArray(), facePlayer(player));
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
                            getRect().setY(getRect().getY() + speed * delta);
                            lastDirection = "down";
                            break;
                        case "U":
                            getRect().setY(getRect().getY() - speed * delta);
                            lastDirection = "up";
                            break;
                        case "L":
                            getRect().setX(getRect().getX() - speed * delta);
                            lastDirection = "left";
                            break;
                        case "R":
                            getRect().setX(getRect().getX() + speed * delta);
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
        getAnimation(player).draw(getRect().getX() + (getRect().getWidth()
                        - getAnimation(player).getCurrentFrame().getWidth()) / 2,
                getRect().getY() + (getRect().getHeight()
                        - getAnimation(player).getCurrentFrame().getHeight()) / 2);
        if (inDialogue) {
            displayDialogue(graphics, player);
        }
    }

    /**
     * Decreases the characters health
     *
     * @param damage The amount of health points to take away
     */
    public void takeDamage(int damage) {
        health -= damage;
    }

    /**
     * @return Current health of the character
     */
    public int getHealth() {
        return health;
    }

    public void displayDialogue(Graphics graphics, Player player) {
        // Look in player inventory to see if player is holding any relevant inventory
        Iterator<Item> inventoryIterator = player.getInventory().getItems().iterator();
        while (inventoryIterator.hasNext()) {
            Item currentItem = inventoryIterator.next();
            if (dialogueMap.containsKey(currentItem.getName())) {
                // Relevant Item found
                currentDialogueArray = dialogueMap.get(currentItem.getName());
                dialogueIndex = 0;
                dialogueMap.remove(currentItem.getName());
                inventory.addItem(currentItem);
                inventoryIterator.remove();
            }
        }
        // Create dialogue rectangle
        Rectangle dialogueRectangle = new Rectangle(getRect().getCenterX()
                - getFont().getWidth(currentDialogueArray.get(dialogueIndex)) / 2,
                getRect().getY() - getFont().getHeight(currentDialogueArray.get(dialogueIndex)) - 10,
                getFont().getWidth(currentDialogueArray.get(dialogueIndex)) + 4, getFont().getHeight() + 2);
        graphics.setColor(Color.black);
        graphics.fill(dialogueRectangle);

        // Draw dialogue text in the rectangle
        getFont().drawString(dialogueRectangle.getX() + 2, dialogueRectangle.getY() + 1,
                currentDialogueArray.get(dialogueIndex), Color.white);
    }

    /**
     * @return Returns the way to face so that it resembles facing the player the most
     */
    public String facePlayer(Player player) {
        return Tools.getFacing(player.getRect(), getRect()).get(1);
    }

    public void setInDialogue(boolean value) {
        inDialogue = value;
    }

    public boolean increaseDialogIndex() {
        dialogueIndex++;
        if (dialogueIndex >= currentDialogueArray.size()) {
            dialogueIndex = 0;
            return false;
        }
        return true;
    }

    public boolean getInDialogue() {
        return inDialogue;
    }

    public HashMap generateDialogueFromArray(ArrayList<String> dialogueFileLines) {
        HashMap<String, ArrayList> returnHashMap = new HashMap<>();
        if (!(dialogueFileLines.size() > 0)) {
            return null;
        }
        String firstline = dialogueFileLines.get(0);
        String[] listOfRelevantItems = firstline.split(",");
        dialogueFileLines.remove(0);
        for (String itemName : listOfRelevantItems) {
            ArrayList<String> toAdd = new ArrayList<>();
            dialogueFileLines.remove(0);
            String toRead = dialogueFileLines.get(0);
            while (!toRead.contains(":") && dialogueFileLines.size() > 0) {
                toAdd.add(toRead);
                dialogueFileLines.remove(0);
                if (dialogueFileLines.size() > 0) {
                    toRead = dialogueFileLines.get(0);
                }
            }
            returnHashMap.put(itemName, toAdd);
        }
        return returnHashMap;
    }

    public Animation getFreezeAnimation() {
        return Tools.getFreezeAnimation(getAnimationArray(), lastDirection);
    }

    public float getSpeed() { return speed; }

    public Inventory getInventory() { return inventory; }

    public String getLastDirection() { return lastDirection; }

    public Animation getRegularAnimation() {
        switch (lastDirection) {
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
    }

    public void setLastDirection(String direction) { lastDirection = direction; }

    public String getType() {
        return type;
    }
}
