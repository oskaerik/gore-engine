import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import java.util.ArrayList;

/**
 * The Inventory class. This class is used to hold all the items that a character would have in its
 * inventory. The inventory handles drawing itself.
 *
 * @author Oskar Eriksson and Gustave Rousselet
 * @version 0.1
 */
public class Inventory {
    private ArrayList<Item> items;
    private Rectangle inventoryOutline;
    private Rectangle inventoryItemOutline;
    private int inventorySelectedItemNumber;

    /**
     * Constructor of the Inventory class. Creates the inventory object. All inventorys are the same.
     */
    public Inventory() {
        items = new ArrayList<>();
        inventoryOutline = new Rectangle(Core.WIDTH/2 + 100, Core.HEIGHT/2 - 200, 100, 160);
        inventoryItemOutline = new Rectangle(Core.WIDTH/2 + 100, Core.HEIGHT/2 - 200, 16, 16);
        inventorySelectedItemNumber = 0;
    }

    /**
     * Method used to get the number of items currently stored in the inventory.
     *
     * @return Int the number of items in the inventory.
     */
    public int getNumberOfItems() {
        return items.size();
    }

    /**
     * Method used to an an Item to the inventory.
     *
     * @param item Item to be added to inventory.
     */
    public void addItem(Item item) {
        items.add(item);
    }

    /**
     * Method used to get an ArrayList of Items currently in Inventory.
     *
     * @return ArrayList of items currently in Inventory.
     */
    public ArrayList<Item> getItems() { return items;}

    /**
     * Method used to check if an Item is in the inventory based on String itemName.
     *
     * @param itemName String the name of the Item being checked if in Inventory.
     * @return Boolean based on if Item is in Inventory.
     */
    public boolean checkIfInventoryContains(String itemName) {
        boolean contains = false;
        for (Item item : items) {
            if (item.getName().equals(itemName)) {
                contains = true;
            }
        }
        return contains;
    }

    /**
     * Method used to increase the selected Item number when scrolling through Inventory.
     */
    public void increaseInventorySelectedItemNumber() {
        inventorySelectedItemNumber++;
    }

    /**
     * Method used to decrease the selected Item number when scrolling through Inventory.
     */
    public void decreaseInventorySelectedItemNumber() {
        inventorySelectedItemNumber--;
    }

    /**
     * Method used to reset the selected Item number when closing/opening inventory.
     */
    public void resetInventorySelectedItemNumber() {
        inventorySelectedItemNumber = 0;
    }

    /**
     * Method used to get the currently selected Item in inventory.
     *
     * @return Int currently selected item number.
     */
    public int getInventorySelectedItemNumber() {
        return inventorySelectedItemNumber;
    }

    /**
     * Draws the inventory outline and the objects in the inventory on the screen.
     *
     * @param graphics Graphics component used to draw.
     */
    public void drawInventory(Graphics graphics) {
        // Fill inventory color
        graphics.setColor(Color.lightGray);
        graphics.fill(inventoryOutline);
        // Set itemOutline color and draw inventory outline and item outline
        graphics.setColor(Color.green);
        graphics.draw(inventoryOutline);
        graphics.draw(inventoryItemOutline);
        // Move selection based on the selected item number
        inventoryItemOutline.setY(Core.HEIGHT/2-200 + inventorySelectedItemNumber*16);
        for (int i = 0; i < getItems().size(); i ++) {
            // Draw all items in inventory, moving down 1 image per item 
            Item itemDisplayed = getItems().get(i);
            graphics.drawImage(itemDisplayed.getAnimationArray().get(0).getImage(0), Core
                    .WIDTH/2 + 100, Core.HEIGHT/2 - 200 + 16*i);
            itemDisplayed.getFont().drawString(Core.WIDTH/2 + 100 + 16, Core
                    .HEIGHT/2 - 200 + 16*i, itemDisplayed.getName(), Color
                    .blue);
        }
    }
}
