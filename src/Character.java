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
 *
 * @author Oskar Eriksson and Gustave Rousselet
 * @version 0.1
 */
public class Character extends Entity {
    private String type;
    private Inventory inventory;
    private int health;

    private ArrayList<String> movementPath;
    private ArrayList<String> movementArray;
    private String lastDirection;
    private float speed;

    private ArrayList<String> currentDialogueArray;
    private HashMap<String, ArrayList<String>> dialogueMap;
    private int dialogueIndex;
    private boolean inDialogue;
    private Character inDialogueWith;

    /**
     * Constructor for the Character class.
     *
     * @param rectangle Character rectangle "hitbox".
     * @param name Name of the Character, example: Skeleton1, Goblin2 etc.
     * @param type Type of the character, examples: Skeleton, Goblin etc.
     * @param speed Speed of character, in pixels per frame.
     * @throws SlickException
     */
    public Character(Rectangle rectangle, String name, String type, float speed)
            throws SlickException {
        super(rectangle, name, "character");
        // Starting settings for all characters
        health = 100;
        inventory = new Inventory();
        this.type = type;

        // The movementPath is the path of the character, does not change
        movementPath = Tools.readFileToArray("res/characters/" + getName() + "/movement.txt");
        // All characters that are NOT player need generated dialogue
        if (!name.equals("player")) {
            if (movementPath != null) {
                movementArray = new ArrayList<>(movementPath);
            }
            dialogueMap = generateDialogueFromArray(
                    Tools.readFileToArray("res/characters/"
                            + getName().replaceAll("\\d", "") + "/dialogue.txt"));
            currentDialogueArray = dialogueMap.get("null");
        }
        this.speed = speed;
        lastDirection = "down";


        // Dialogue starts at index 0
        dialogueIndex = 0;
        inDialogue = false;
        inDialogueWith = null;

    }

    /**
     * Method used to update the location of the character based on player movement from keyboard
     * keys pressed.
     *
     * @param delta Variable used for FPS.
     */
    public void updateLocation(int delta) {
        if (movementArray != null) {
            for (int i = 0; i < movementArray.size(); i++) {
                String[] pixelsAndDirection = movementArray.get(i).split(" ");
                int pixels = Integer.parseInt(pixelsAndDirection[0]);
                if (pixels > 0) {
                    switch (pixelsAndDirection[1]) {
                        case "D":
                            setFrozen(false);
                            getRect().setY(getRect().getY() + speed * delta);
                            lastDirection = "down";
                            break;
                        case "U":
                            setFrozen(false);
                            getRect().setY(getRect().getY() - speed * delta);
                            lastDirection = "up";
                            break;
                        case "L":
                            setFrozen(false);
                            getRect().setX(getRect().getX() - speed * delta);
                            lastDirection = "left";
                            break;
                        case "R":
                            setFrozen(false);
                            getRect().setX(getRect().getX() + speed * delta);
                            lastDirection = "right";
                            break;
                        case "S":
                            setFrozen(true);
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
     * Method used to get the speed of the character.
     *
     * @return The speed of the character.
     */
    public float getSpeed() { return speed; }

    /**
     * Method used to set the last direction of the character.
     *
     * @param direction The direction the last direction should be set to.
     */
    public void setLastDirection(String direction) { lastDirection = direction; }

    /**
     * Method used to get the last direction of the character.
     *
     * @return The last direction of the character.
     */
    public String getLastDirection() { return lastDirection; }

    /**
     * Renders the character animation onto the screen. All relative to player position.
     *
     * @param player Player used to get player location as reference point for all rendering.
     * @param graphics Graphics used to draw character.
     */
    public void renderCharacter(Player player, Graphics graphics) {
        getAnimation(player).draw(getRect().getX() + (getRect().getWidth()
                        - getAnimation(player).getCurrentFrame().getWidth()) / 2,
                getRect().getY() + (getRect().getHeight()
                        - getAnimation(player).getCurrentFrame().getHeight()) / 2);
        if (inDialogue) {
            displayDialogue(graphics, player);
        }
    }

    /**
     * Method used to draw onto the screen the health of the character.
     */
    public void drawHealth() {
        Color color = Color.white;
        if (health < 100) {
            // Character is damaged, change color according to how damaged
            if (health >= 75) {
                color = Color.green;
            } else if (health >= 50) {
                color = Color.yellow;
            } else {
                color = Color.red;
            }
            getFont().drawString(getRect().getX() + 1, getRect().getY() - 14,
                    Integer.toString(health) + "%", Color.black);
            getFont().drawString(getRect().getX(), getRect().getY() - 15,
                    Integer.toString(health) + "%", color);
        }
    }

    /**
     * Method used to get the current health of the character.
     *
     * @return Current health points of the character.
     */
    public int getHealth() {
        return health;
    }

    /**
     * Decreases the characters health.
     *
     * @param damage The amount of health points to take away.
     */
    public void takeDamage(int damage) {
        if (!type.equals("friend")) {
            health -= damage;
        }
    }

    /**
     * Method used to get the animation of the character at the current time and current state of
     * the game. If the character is in dialogue with the player, then the character faces the player.
     *
     * @param player The player, used to check if inDialogue is true (in dialogue with player).
     * @return Animation depending on if frozen or if in dialogue with player, or regular.
     */
    public Animation getAnimation(Player player) {
        if (!isFrozen()) {
            return getDefaultAnimation();
        } else {
            if (!inDialogue) {
                return Tools.getFreezeAnimation(getAnimationArray(), lastDirection);
            } else if (!type.equals("player")) {
                return Tools.getFreezeAnimation(getAnimationArray(), faceCharacter(player));
            } else {
                return Tools.getFreezeAnimation(getAnimationArray(), faceCharacter(player));
            }
        }
    }

    /**
     * Method used to get the default animation of the character.
     *
     * @return Animation the default animation of the character.
     */
    public Animation getDefaultAnimation() {
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

    /**
     * Method used to display the dialogue of a character. Dialogue is gotten from dialogue
     * HashMap. The dialogue HashMap is then changed based on if the player is holding an item of
     * interest to the character.
     *
     * @param graphics Graphics used to draw dialogue.
     * @param player Player used to check for items.
     */
    public void displayDialogue(Graphics graphics, Player player) {
        // Look in player inventory to see if player is holding any relevant inventory
        Iterator<Item> inventoryIterator = player.getInventory().getItems().iterator();
        while (inventoryIterator.hasNext()) {
            Item currentItem = inventoryIterator.next();
            if (dialogueMap.containsKey(currentItem.getName())) {
                // Relevant Item found, get relevant dialogue
                currentDialogueArray = dialogueMap.get(currentItem.getName());
                dialogueIndex = 0;
                // Remove dialogue for relevant item since it has been used
                dialogueMap.remove(currentItem.getName());
                // Take item from player and add to character inventory
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
     * Method used to face player when in dialogue.
     *
     * @param player Player that character will face in dialogue.
     * @return String where to face.
     */
    public String faceCharacter(Player player) {
        if (!type.equals("player")) {
            return Tools.getFacing(player.getRect(), getRect()).get(1);
        } else {
            return Tools.getFacing(getRect(), inDialogueWith.getRect()).get(0);
        }
    }

    /**
     * Method used to set character into dialogue state.
     *
     * @param value True or false based on if/if no in dialogue.
     */
    public void setInDialogue(boolean value) {
        inDialogue = value;
    }

    /**
     * Method used to increase dialogueIndex which holds index of sentence in dialogueArray.
     *
     * @return True/false based on if there is more dialogue in the array or not.
     */
    public boolean increaseDialogueIndex() {
        dialogueIndex++;
        if (dialogueIndex >= currentDialogueArray.size()) {
            // No more dialogue in array, reset index to 0
            dialogueIndex = 0;
            return false;
        }
        return true;
    }

    /**
     * Method used to check if a character is in dialogue.
     *
     * @return True/false whether they are in dialogue or not.
     */
    public boolean getInDialogue() {
        return inDialogue;
    }

    /**
     * Method used to generate dialogue from an ArrayList of lines which have been generated from
     * the class Tools which reads from the dialogue text file in the character directory.
     *
     * @param dialogueFileLines ArrayList of Strings which are the read lines from the dialogue.txt
     *                          file in the character directory.
     * @return HashMap dialogueHashMap for character. Generated from dialogue.txt.
     */
    public HashMap<String, ArrayList<String>> generateDialogueFromArray(
            ArrayList<String> dialogueFileLines) {
        HashMap<String, ArrayList<String>> returnHashMap = new HashMap<>();

        if (dialogueFileLines.size() <= 0) {
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

    /**
     * Method used to set the character in dialogue with a certain character.
     *
     * @param character Character character is to be set in dialogue with.
     */
    public void setInDialogueWith(Character character) {
        inDialogueWith = character;
        if (character != null) {
            setFrozen(true);
            setInDialogue(true);
        } else {
            setFrozen(false);
            setInDialogue(false);
        }
    }

    /**
     * The character that the character is currently in dialogue with.
     *
     * @return Character that the character is currently in dialogue with.
     */
    public Character getInDialogueWith() { return inDialogueWith; }

    /**
     * Method used to get the Inventory of the character.
     *
     * @return The inventory of the character.
     */
    public Inventory getInventory() { return inventory; }

    /**
     * Method used to get the type field of the character.
     *
     * @return The type of the character.
     */
    public String getType() {
        return type;
    }
}

