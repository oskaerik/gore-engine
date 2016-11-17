import org.newdawn.slick.Animation;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Rectangle;

import java.awt.*;
import java.util.ArrayList;

/**
 * Entity is the parent class of things in the world like items and objects
 * @author Oskar Eriksson and Gustave Rousselet
 * @version 0.1
 */
public class Entity {
    private String name;
    private Rectangle rectangle;
    private ArrayList<Animation> animationArray;
    private String type;
    private boolean frozen;
    private TrueTypeFont font;

    /** Constructor of the entity class
     * @param rectangle The entity's rectangle
     * @param name Name of the entity
     * @param description Description of the entity
     * @param type The type of the entity
     * @throws SlickException Generic exception
     */
    public Entity(Rectangle rectangle, String name, String type)
            throws SlickException {
        this.name = name;
        this.rectangle = rectangle;
        this.type = type;
        font = new TrueTypeFont(new Font("Arial", Font.BOLD, 10), true);
        frozen = false;

        animationArray = Tools.createAnimation(this.type, this.name);
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
     * @return The font of the entity
     */
    protected TrueTypeFont getFont() { return font; }

    protected boolean isFrozen() { return frozen; }
    protected void setFrozen(boolean value) { frozen = value; }

    protected ArrayList<Animation> getAnimationArray() { return animationArray; }
}
