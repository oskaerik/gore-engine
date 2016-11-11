import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Rectangle;

import java.awt.*;

/**
 * The Exit class, containing exits
 * @author Oskar Eriksson and Gustave Rousselet
 * @version 0.1
 */
public class Item {

    private TrueTypeFont font;
    private Rectangle  rectangle;
    private String name;

    public Item (Rectangle rectangle, String itemName) {
        Font textFont = new Font("Arial", Font.BOLD, 10);
        font = new TrueTypeFont(textFont, true);
        this.rectangle = rectangle;
        name = itemName;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public String getName() {
        return name;
    }

    public TrueTypeFont getItemText() {
        return font;
    }
}
