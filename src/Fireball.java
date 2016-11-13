import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import java.util.ArrayList;

/**
 * The Fireball class
 * @author Oskar Eriksson and Gustave Rousselet
 * @version 0.1
 */
public class Fireball extends Entity {

    private Animation animation;
    private boolean intersects;
    private String direction;
    /**
     * Constructor of the entity class
     *
     * @param name        Name of the entity
     * @param description Description of the entity
     */
    public Fireball(Rectangle rectangle, String name, String description) throws SlickException {
        super(rectangle, name, description);
        animation = Tools.createAnimation("projectile", "fireball").get(0);
        intersects = false;
        direction = null;
    }

    public void setDirection(String direction) {
        switch (direction) {
            case ("up"):
                this.direction = "up";
                break;
            case ("down"):
                this.direction = "down";
                break;
            case ("left"):
                this.direction = "left";
                break;
            case ("right"):
                this.direction = "right";
                break;
        }
    }

    public String getDirection() {
        return direction;
    }

    public void toggleIntersect() {
        intersects = !intersects;
    }

    public boolean checkIfintersects(ArrayList<Rectangle> rectangles) {
        boolean intersects = false;
        for (Rectangle rectangle : rectangles) {
            if (getRect().intersects(rectangle)) {
                intersects = true;
            }
        }
        return intersects;
    }

    public Animation getAnimation() {
        return animation;
    }
}
