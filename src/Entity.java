import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Rectangle;

import java.awt.*;

/**
 * Entity is the parent class of things in the world like items and objects
 * @author Oskar Eriksson and Gustave Rousselet
 * @version 0.1
 */
public class Entity {
    private String name;
    private String description;
    private Rectangle rectangle;
    private TrueTypeFont font;
    private boolean frozen;

    /** Constructor of the entity class
     * @param name Name of the entity
     * @param description Description of the entity
     */
    public Entity(Rectangle rectangle, String name, String description) {
        this.name = name;
        this.description = description;
        this.rectangle = rectangle;

        font = new TrueTypeFont(new Font("Arial", Font.BOLD, 10), true);

        frozen = false;
    }

    /**
     * @return The entity rectangle
     */
    public Rectangle getRect() {
        return rectangle;
    }


    /**
     * @return Name of the entity
     */
    protected String getName() { return name; }

    /**
     * @return Description of the entity
     */
    protected String getDescription() { return description; }

    /**
     * @return The font of the entity
     */
    protected TrueTypeFont getFont() { return font; }

    protected boolean isFrozen() { return frozen; }
    protected void setFrozen(boolean value) { frozen = value; }
}
