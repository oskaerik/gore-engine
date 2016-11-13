import org.newdawn.slick.Animation;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import java.util.ArrayList;

/**
 * The Projectile class
 * @author Oskar Eriksson and Gustave Rousselet
 * @version 0.1
 */
public class Projectile extends Entity {

    private Animation animation;
    private boolean shot;
    private String direction;
    private float speed;
    private int damage;

    /**
     * Constructor of the entity class
     *
     * @param name        Name of the entity
     * @param description Description of the entity
     */
    public Projectile(Rectangle rectangle, String name, String description) throws SlickException {
        super(rectangle, name, description);
        animation = Tools.createAnimation("projectile", name).get(0);
        shot = false;
        direction = null;
        speed = 6;
        damage = 10;
    }

    public String getDirection() {
        return direction;
    }

    public boolean checkIfIntersects(ArrayList<Rectangle> rectangles) {
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

    public void shoot(float centerX, float centerY, String direction) {
        this.direction = direction;
        getRect().setCenterX(centerX);
        getRect().setCenterY(centerY);
        shot = true;
    }

    public boolean isShot() { return shot; }

    private Character checkIntersection(ArrayList<Rectangle> blocks, ArrayList<Character> characters) {
        for (Rectangle rectangle : blocks) {
            if (getRect().intersects(rectangle)) {
                shot = false;
                return null;
            }
        }
        for (Character character : characters) {
            if (getRect().intersects(character.getRect())) {
                shot = false;
                return character;
            }
        }
        return null;
    }

    public Character moveProjectile(ArrayList<Rectangle> blocks, ArrayList<Character> characters) {
        if (shot) {
            switch (direction) {
                case "up":
                    getRect().setY(getRect().getY() - speed);
                    break;
                case "down":
                    getRect().setY(getRect().getY() + speed);
                    break;
                case "left":
                    getRect().setX(getRect().getX() - speed);
                    break;
                case "right":
                    getRect().setX(getRect().getX() + speed);
                    break;
                default:
                    break;
            }
            getAnimation().draw(getRect().getX(), getRect().getY());
            return checkIntersection(blocks, characters);
        }
        return null;
    }

    public int getDamage() { return damage; }
}