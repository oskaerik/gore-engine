import org.newdawn.slick.*;
import org.newdawn.slick.Graphics;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A game written using Java and Slick2D.
 * @author Oskar Eriksson and Gustave Rousselet
 * @version 0.1
 */
public class Game extends BasicGame {
    private TrueTypeFont font;
    private Player player;

    public Game(String title) { super(title); }

    @Override
    public void init(GameContainer gameContainer) throws SlickException {
        player = new Player(300, 200, 20, 20, 2.5);
    }

    @Override
    public void update(GameContainer gameContainer, int i) throws SlickException {
        if (gameContainer.getInput().isKeyDown(Input.KEY_UP)) {
            player.movement("up");
        }
        if (gameContainer.getInput().isKeyDown(Input.KEY_DOWN)) {
            player.movement("down");
        }
        if (gameContainer.getInput().isKeyDown(Input.KEY_LEFT)) {
            player.movement("left");
        }
        if (gameContainer.getInput().isKeyDown(Input.KEY_RIGHT)) {
            player.movement("right");
        }
    }

    @Override
    public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
        graphics.fill(player.getRect());
    }

    public static void main(String[] args) {
        try {
            AppGameContainer appgc = new AppGameContainer(new Game("slick-game"));
            appgc.setDisplayMode(640, 480, false);
            appgc.setTargetFrameRate(60);
            appgc.start();
        } catch (SlickException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
