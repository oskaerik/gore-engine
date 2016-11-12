import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.Image;

import java.awt.Font;

/**
 * The Exit class, containing exits
 * @author Oskar Eriksson and Gustave Rousselet
 * @version 0.1
 */
public class Item {

    private TrueTypeFont font;
    private Rectangle  rectangle;
    private String directory;
    private String name;
    private Image image;

    public Item (Rectangle rectangle, Image image, String itemDirectory, String itemName) {
        Font textFont = new Font("Arial", Font.BOLD, 10);
        font = new TrueTypeFont(textFont, true);
        this.rectangle = rectangle;
        name = itemName;
        this.image = image;
        directory = itemDirectory;
    }

    public Rectangle getRect() {
        return rectangle;
    }

    public String getName() {
        return name;
    }

    public TrueTypeFont getItemFont() {
        return font;
    }

    public Image getItemImage() { return image; }

    public String getItemDirectory() { return directory; }
}
