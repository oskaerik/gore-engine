import org.newdawn.slick.Animation;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import java.util.ArrayList;
import java.util.Date;

/**
 * The Projectile class. All projectiles belong to a certain character, this is stored as a String
 * in terms of the Character name. All projectiles have a speed and a direction, amd they all have
 * an animation array and a damage.
 *
 * @author Oskar Eriksson and Gustave Rousselet
 * @version 0.1
 */
public class Projectile extends Entity {
    private ArrayList<Animation> animationArray;
    private boolean shot;
    private String direction;
    private float speed;
    private int damage;
    private String belongsTo;
    private long lastShot;
    private Date date;
    private int frameCounter;

    /**
     * Constructor for the Projectile class.
     *
     * @param rectangle The rectangle of the projectile.
     * @param name The name of the projectile.
     * @throws SlickException Generic exception.
     */
    public Projectile(Rectangle rectangle, String name, String characterName,
                      int damage, float speed) throws SlickException {
        super(rectangle, name, "projectile");
        animationArray = Tools.createAnimation("projectile", name);
        shot = false;
        direction = null;
        this.speed = speed;
        this.damage = damage;
        belongsTo = characterName;
        date = new Date();
        lastShot = date.getTime();
    }

    /**
     * Method used to get the Animation of the Projectile.
     *
     * @return Animation of the Projectile.
     */
    public Animation getAnimation() {
        if (!isFrozen()) {
            // First animatin is always still
            return animationArray.get(0);
        } else {
            return Tools.getFreezeAnimation(animationArray, "");
        }
    }

    /**
     * Method used to initiate the Projectile into shoot mode based on direction and location.
     *
     * @param centerX X location of Projectile center.
     * @param centerY Y location of Projectile center.
     * @param direction Direction of Projectile.
     */
    public void shoot(float centerX, float centerY, String direction) {
        this.direction = direction;
        getRect().setCenterX(centerX);
        getRect().setCenterY(centerY);
        shot = true;
    }

    /**
     * Method used to check if the Projectile is shot.
     *
     * @return Boolean whether Projectile is shot or not.
     */
    public boolean isShot() { return shot; }

    /**
     * Method used to set the Projectile into shot/not shot mode.
     *
     * @param shot Boolean whether projectile is shot or not.
     */
    public void setShot(boolean shot) {
        this.shot = shot;
    }

    /**
     * Method used to check if the Projectile has intersected with any characters or blocks.
     *
     * @param blocks Rectangles representing blocked walls/tiles.
     * @param characters Characters list.
     * @return Returns Character is Projectile hits Character, and null if hits wall or neither.
     */
    private Character checkIntersection(ArrayList<Rectangle> blocks, ArrayList<Character> characters) {
        for (Rectangle rectangle : blocks) {
            if (getRect().intersects(rectangle)) {
                hit();
                return null;
            }
        }
        for (Character character : characters) {
            if (getRect().intersects(character.getRect()) && !character.getName().equals(belongsTo)) {
                hit();
                return character;
            }
        }
        return null;
    }

    /**
     * Method used to control the movement of the Projectile. Moves Projectile according to direction.
     *
     * @param blocks List of blocked Rectangles.
     * @param characters List of Characters.
     * @param delta Variable used to store ms between frames.
     * @return If intersects Character, returns character, otherwise null.
     */
    public Character moveProjectile(
            ArrayList<Rectangle> blocks, ArrayList<Character> characters, int delta) {
        if (shot) {
            switch (direction) {
                case "up":
                    getRect().setY(getRect().getY() - speed*delta);
                    break;
                case "down":
                    getRect().setY(getRect().getY() + speed*delta);
                    break;
                case "left":
                    getRect().setX(getRect().getX() - speed*delta);
                    break;
                case "right":
                    getRect().setX(getRect().getX() + speed*delta);
                    break;
                default:
                    break;
            }
            return checkIntersection(blocks, characters);
        }
        return null;
    }

    /**
     * Method used to render the Projectile.
     */
    public void render() {
        if (shot) {
            getAnimation().draw(getRect().getX(), getRect().getY());
        } else {
            hit();
        }
    }

    /**
     * Method used to get the damage that the Projectile inflicts upon hit.
     *
     * @return Int amount of damage to inflict.
     */
    public int getDamage() { return damage; }

    /**
     * Method used to get the owner (Character) of the Projectile.
     *
     * @return Character that the Projectile belongs to.
     */
    public String getBelongsTo() {
        return belongsTo;
    }

    /**
     * Method used to toggle the time that the Projectile was last shot. lastShot gets set to current
     * time when toggled.
     *
     * @param time Current time.
     */
    public void toggleShotLastTime(long time) {
        lastShot = time;
    }

    /**
     * Method used to get the last time the Projectile was shot.
     *
     * @return Long last time the projectile was shot.
     */
    public long getLastShot() {
        return lastShot;
    }

    /**
     * Method used to initialise the Projectile into hit mode (when Projectile intersects Character).
     */
    public void hit() {
        // If it's a hit, set shot to false and start hit animation
        if (shot) {
            shot = false;
            frameCounter = getAnimationArray().get(1).getFrameCount();
        }

        // Run hit animation for every frame of the animation
        if (frameCounter >= 0) {
            getAnimationArray().get(1).draw(getRect().getX(), getRect().getY());
            frameCounter--;
        }
    }
}
