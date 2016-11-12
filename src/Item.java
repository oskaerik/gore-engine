import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.Image;

import java.awt.Font;

/**
 * The Exit class, containing exits
 * @author Oskar Eriksson and Gustave Rousselet
 * @version 0.1
 */
public class Item extends Entity {
    private TrueTypeFont font;
    private String path;
    private Image image;

    /**
     * Constructor for Item class
     * @param rectangle The rectangle of the item
     * @param path The path to the sprite of the item
     * @param name The name of the item
     * @param description The description of the item
     * @throws SlickException Generic exception
     */
    public Item (Rectangle rectangle, String path, String name, String description) throws SlickException {
        super(rectangle, name, description);
        this.path = path;
        image = new Image(path);
    }

    /**
     * @return The item text object
     */
    // Refactor to getFont() in World!
    public TrueTypeFont getItemFont() {
        return getFont();
    }

    /**
     * @return The item image/sprite
     */
    public Image getItemImage() { return image; }
}
