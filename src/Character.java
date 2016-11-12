import org.newdawn.slick.geom.Rectangle;

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
}
