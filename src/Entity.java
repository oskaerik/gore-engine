import org.newdawn.slick.Animation;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Rectangle;

import java.awt.*;
import java.util.ArrayList;

/**
 * Entity is the parent class of the physical things that populate the world such as Item, Character
 * etc.
 *
 * @author Oskar Eriksson and Gustave Rousselet
 * @version 0.1
 */
public class Entity {
    private String name;
    private String type;
    private Rectangle rectangle;
    private ArrayList<Animation> animationArray;
    private boolean frozen;
    private TrueTypeFont font;

    /**
     *
     * @param rectangle The rectangle of the entity.
     * @param name The name of the entity.
     * @param type The type of the entity.
     * @throws SlickException Generic exception.
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
     * Method used to get the rectangle of the entity.
     *
     * @return Rectangle the rectangle of the entity.
     */
    public Rectangle getRect() {
        return rectangle;
    }

    /**
     * Method used to get the name of the entity.
     *
     * @return String name of the entity.
     */
    protected String getName() { return name; }

    /**
     * Method used to get the Font of the entity.
     *
     * @return Font of the entity.
     */
    protected TrueTypeFont getFont() { return font; }

    /**
     * Method used to get if the entity is frozen or not.
     *
     * @return Boolean according to the current frozen/not frozen state of entity.
     */
    protected boolean isFrozen() { return frozen; }

    /**
     * Method used to set an entity into the frozen state.
     *
     * @param value Boolean true/false for fronzen/not frozen.
     */
    protected void setFrozen(boolean value) { frozen = value; }

    /**
     * Method used to get the animation array of an entity.
     *
     * @return ArrayList of animations which the entity has. Usually up/down/left/right/default.
     */
    protected ArrayList<Animation> getAnimationArray() { return animationArray; }
}
