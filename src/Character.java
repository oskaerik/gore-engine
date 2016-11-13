import org.newdawn.slick.Animation;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import java.util.ArrayList;

/**
 * The class for NPC characters
 * @author Oskar Eriksson and Gustave Rousselet
 * @version 0.1
 */
public class Character extends Entity {
    private ArrayList<Animation> animationArray;
    private ArrayList<String> movementPath;
    private ArrayList<String> movementArray;
    String lastDirection;
    int health;

    /**
     * Constructor for the character class
     * @param name The name of the character
     * @param description The description of the character
     */
    public Character(Rectangle rectangle, String name, String description) throws SlickException {
        super(rectangle, name, description);
        animationArray = Tools.createAnimation("character", getName());
        movementPath = Tools.readInstructions("movement", getName());
        movementArray = new ArrayList<>(movementPath);
        lastDirection = "down";
        health = 100;
    }

    public Animation getAnimation() {
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
    }

    public void updateLocation() {
        if (movementArray != null) {
            for (int i = 0; i < movementArray.size(); i++) {
                String[] pixelsAndDirection = movementArray.get(i).split(" ");
                int pixels = Integer.parseInt(pixelsAndDirection[0]);
                if (pixels > 0) {
                    switch (pixelsAndDirection[1]) {
                        case "D":
                            getRect().setY(getRect().getY() + 1);
                            lastDirection = "down";
                            break;
                        case "U":
                            getRect().setY(getRect().getY() - 1);
                            lastDirection = "up";
                            break;
                        case "L":
                            getRect().setX(getRect().getX() - 1);
                            lastDirection = "left";
                            break;
                        case "R":
                            getRect().setX(getRect().getX() + 1);
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

    public void renderCharacter() {
        getAnimation().draw(getRect().getX()+(getRect().getWidth() -
                        getAnimation().getCurrentFrame().getWidth())/2, getRect().getY() +
                (getRect().getHeight()  - getAnimation().getCurrentFrame().getHeight())/2);
    }

    public void takeDamage(int damage) {
        health -= damage;
        System.out.println(getName() + ": " + health);
    }

    public int getHealth() { return health; }
}
