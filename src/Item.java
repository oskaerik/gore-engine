import org.newdawn.slick.geom.Rectangle;

/**
 * The Exit class, containing exits
 * @author Oskar Eriksson and Gustave Rousselet
 * @version 0.1
 */
public class Item {

    private Rectangle  rectangle;
    private String name;

    public Item (Rectangle rectangle, String itemName) {
        this.rectangle = rectangle;
        name = itemName;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }
}
