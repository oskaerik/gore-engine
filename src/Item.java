import org.newdawn.slick.*;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.Image;
import org.w3c.dom.css.Rect;

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
     * @return The item image/sprite
     */
    public Image getItemImage() { return image; }

    /**
     * Draws highlighting on items that intersects with player's range
     */
    public void hightlight(Circle playerRange) {
        if (getRect().intersects(playerRange)) {
            getFont().drawString(getRect().getX(), getRect().getY(), getName(), Color.blue);
        }
    }
}
