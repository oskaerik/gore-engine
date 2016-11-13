/**
 * The GameState class, contains information about the current state of the game.
 * @author Oskar Eriksson and Gustave Rousselet
 * @version 0.1
 */

public class GameState {

    private String currentMode;

    public GameState() { currentMode = "default"; }

    public void toggleInventory() {
        if (currentMode.equals("inventory")) {
            currentMode = "default";
        }
        else if (currentMode.equals("default")) {
            currentMode = "inventory";
        }
    }

    public void toggleDialog() {
        if (currentMode.equals("default")) {
            currentMode = "dialog";
        }
        else if (currentMode.equals("dialog")) {
            currentMode = "default";
        }
    }

    public String getCurrentMode() { return currentMode; }
}
