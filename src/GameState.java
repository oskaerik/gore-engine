import java.util.Date;

/**
 * The GameState class, contains information about the current state of the game. If inventory is
 * up, if the player is in dialogue etc.
 *
 * @author Oskar Eriksson and Gustave Rousselet
 * @version 0.1
 */

public class GameState {

    private String currentMode;
    // Records the time at the moment when the player has died
    private long endTime;

    /**
     * Constructor of the GameState object.
     *
     * @param currentMode String the current mode that the game is in.
     */
    public GameState(String currentMode) { this.currentMode = currentMode; }

    /**
     * Method used to get the current state of the game.
     *
     * @return String current state of the game.
     */
    public String getCurrentState() { return currentMode; }

    /**
     * Method used to toggle the gamestate into inventory mode. Toggled when inventory is opened/
     * closed.
     */
    public void toggleInventory() {
        if (currentMode.equals("inventory")) {
            currentMode = "default";
        }
        else if (currentMode.equals("default")) {
            currentMode = "inventory";
        }
    }

    /**
     * Method used to toggle dialogue mode to true/false based on if character is in dialogue or not.
     *
     * @param value Boolean based on if in dialogue or not.
     */
    public void setDialogueMode(boolean value) {
        if (value) {
            currentMode = "dialogue";
        } else {
            currentMode = "default";
        }
    }

    /**
     * Method used to set the gamestate to the startgame mode.
     */
    public void startGame() {
        currentMode = "default";
    }

    /**
     * Method used to toggle the gameover state of the game when the player dies.
     */
    public void gameOver() {
        currentMode = "gameover";
        endTime = new Date().getTime();
    }

    /**
     * Method used to get the end time (time when the player has died).
     *
     * @return Time when the player has died (in milliseconds).
     */
    public long getEndTime() {
        return endTime;
    }
}
