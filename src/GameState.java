/**
 * The GameState class, contains information about the current state of the game.
 * @author Oskar Eriksson and Gustave Rousselet
 * @version 0.1
 */

public class GameState {

    private boolean inventoryOpen;

    public GameState() {
        inventoryOpen = false;
    }

    public void toggleInventory() {
        inventoryOpen = !inventoryOpen;
    }

    public boolean isInventoryOpen() {
        return inventoryOpen;
    }
}
