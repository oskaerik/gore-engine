import java.util.Date;

/**
 * The GameState class, contains information about the current state of the game.
 * @author Oskar Eriksson and Gustave Rousselet
 * @version 0.1
 */

public class GameState {

    private String currentMode;
    private long endTime;

    public GameState(String currentMode) { this.currentMode = currentMode; }

    public void toggleInventory() {
        if (currentMode.equals("inventory")) {
            currentMode = "default";
        }
        else if (currentMode.equals("default")) {
            currentMode = "inventory";
        }
    }

    public void setDialogueMode(boolean value) {
        if (value) {
            currentMode = "dialogue";
        } else {
            currentMode = "default";
        }
    }

    public String getCurrentState() { return currentMode; }

    public void gameOver() {
        currentMode = "gameover";
        endTime = new Date().getTime();
    }

    public long getEndTime() {
        return endTime;
    }

    public void startGame() {
        currentMode = "default";
    }

}
