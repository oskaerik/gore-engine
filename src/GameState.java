import java.util.Date;

/**
 * The GameState class, contains information about the current state of the game.
 * @author Oskar Eriksson and Gustave Rousselet
 * @version 0.1
 */

public class GameState {

    private String currentMode;
    private long endTime;

    public GameState() { currentMode = "default"; }

    public void toggleInventory() {
        if (currentMode.equals("inventory")) {
            currentMode = "default";
        }
        else if (currentMode.equals("default")) {
            currentMode = "inventory";
        }
    }

    public void toggleDialogue() {
        if (currentMode.equals("default")) {
            currentMode = "dialogue";
        }
        else if (currentMode.equals("dialogue")) {
            currentMode = "default";
        }
    }

    public String getCurrentState() { return currentMode; }

    public void toggleGameOver() {
        currentMode = "gameover";
        endTime = new Date().getTime();
    }

    public void toggleStartScreen() {
        currentMode = "startscreen";
    }

    public void toggleStartGame() {
        currentMode = "default";
    }

    public long getEndTime() {
        return endTime;
    }
}
