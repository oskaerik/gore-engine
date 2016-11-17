import org.newdawn.slick.*;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.Image;
import org.w3c.dom.css.Rect;

import java.awt.Font;

/**
 * The Item class, an Item object is a object that can be added to and dropped from the inventory.
 * NPC characters could want an item, or an item could enable a specific player ability.
 * @author Oskar Eriksson and Gustave Rousselet
 * @version 0.1
 */
public class Item extends Entity {
    /**
     * Constructor for Item class
     * @param rectangle The rectangle of the item
     * @param name The name of the item
     * @throws SlickException Generic exception
     */
    public Item (Rectangle rectangle, String name) throws SlickException {
        super(rectangle, name, "item");
    }

    /**
     * Draws highlighting on items that intersects with player's range
     * @param playerRange The player's range circle
     */
    public void hightlight(Circle playerRange) {
        if (getRect().intersects(playerRange)) {
            getFont().drawString(getRect().getX(), getRect().getY(), getName(), Color.blue);
        }
    }
}
