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
        animation = createAnimation();
        intersects = false;
        direction = null;
    }

    public Animation createAnimation() throws SlickException {
        Image fireball1 = new Image("res/fireball/fireball-1.png");
        Image fireball2 = new Image("res/fireball/fireball-2.png");
        Image fireball3 = new Image("res/fireball/fireball-3.png");
        Image fireball4 = new Image("res/fireball/fireball-4.png");
        Image fireball5 = new Image("res/fireball/fireball-5.png");
        Image fireball6 = new Image("res/fireball/fireball-6.png");
        Image fireball7 = new Image("res/fireball/fireball-7.png");
        Image fireball8 = new Image("res/fireball/fireball-8.png");
        Image fireball9 = new Image("res/fireball/fireball-9.png");
        Image fireball10 = new Image("res/fireball/fireball-10.png");

        Animation returnAnimation = new Animation();

        returnAnimation.addFrame(fireball1, 100);
        returnAnimation.addFrame(fireball2, 100);
        returnAnimation.addFrame(fireball3, 100);
        returnAnimation.addFrame(fireball4, 100);
        returnAnimation.addFrame(fireball5, 100);
        returnAnimation.addFrame(fireball6, 100);
        returnAnimation.addFrame(fireball7, 100);
        returnAnimation.addFrame(fireball8, 100);
        returnAnimation.addFrame(fireball9, 100);
        returnAnimation.addFrame(fireball10, 100);

        return returnAnimation;
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
