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
    private Rectangle rectangle;
    private String path;
    private Image image;

    public Item (Rectangle rectangle, String path, String name, String description) throws SlickException {
        super(name, description);
        font = new TrueTypeFont(new Font("Arial", Font.BOLD, 10), true);
        this.rectangle = rectangle;
        this.path = path;
        image = new Image(path);
    }

    public Rectangle getRect() {
        return rectangle;
    }

    public TrueTypeFont getItemFont() {
        return font;
    }

    public Image getItemImage() { return image; }
}
