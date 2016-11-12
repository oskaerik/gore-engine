import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import java.util.ArrayList;

/**
 * The class for NPC characters
 * @author Oskar Eriksson and Gustave Rousselet
 * @version 0.1
 */
public class Character extends Entity {

    /**
     * Constructor for the character class
     * @param name The name of the character
     * @param description The description of the character
     */
    public Character(Rectangle rectangle, String name, String description) {
        super(rectangle, name, description);
    }

    public static ArrayList<Animation> createAnimation(String characterName) throws SlickException {
        ArrayList<Animation> animationArray = new ArrayList<>();
        String[] directions = {"up", "down", "left", "right"};
        for (String direction : directions) {
            Animation directionAnimation = new Animation();
            for (int i = 1; i < 5; i++) {
                String pathToFrame = "res/characters/" + characterName + "/" + direction + "-" + i + ".png";
                directionAnimation.addFrame(new Image(pathToFrame), 100);
            }
            animationArray.add(directionAnimation);
        }
        return animationArray;
    }
}