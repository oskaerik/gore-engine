import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import java.util.ArrayList;

/**
 * The player class
 * @author Oskar Eriksson and Gustave Rousselet
 * @version 0.1
 */
public class Inventory {

    private ArrayList<Item> items;
    private Rectangle inventoryOutline;
    private Rectangle inventoryItemOutline;
    private int inventorySelectedItemNumber;

    public Inventory() {
        items = new ArrayList<Item>();
        inventoryOutline = new Rectangle(Core.WIDTH/2 + 100, Core.HEIGHT/2 - 200, 100, 160);
        inventoryItemOutline = new Rectangle(Core.WIDTH/2 + 100, Core.HEIGHT/2 - 200, 16, 16);
        inventorySelectedItemNumber = 0;
    }

    public Item getItemNumber(int itemNumber) {
        return items.get(itemNumber);
    }

    public boolean checkIfEmpty() {
        return items.isEmpty();
    }

    public int getNumberOfItems() {
        return items.size();
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public Item removeItemNumber(int itemNumber) {
        return items.remove(itemNumber);
    }

    public ArrayList<Item> getItems() { return items;}

    public void increaseInventorySelectedItemNumber() {
        inventorySelectedItemNumber++;
    }

    public void decreaseInventorySelectedItemNumber() {
        inventorySelectedItemNumber--;
    }

    public void resetInventorySelectedItemNumber() {
        inventorySelectedItemNumber = 0;
    }

    public int getInventorySelectedItemNumber() {
        return inventorySelectedItemNumber;
    }
    /**
     * Draws the inventory outline and the objects in the inventory on the screen
     * @param graphics Graphics component used to draw
     */
    public void drawInventory(Graphics graphics) {
        graphics.setColor(Color.yellow);
        graphics.draw(inventoryOutline);
        graphics.draw(inventoryItemOutline);
        inventoryItemOutline.setY(Core.HEIGHT/2-200 + inventorySelectedItemNumber*16);
        for (int i = 0; i < getItems().size(); i ++) {
            Item itemDisplayed = getItems().get(i);
            graphics.drawImage(itemDisplayed.getItemImage(), Core
                    .WIDTH/2 + 100, Core.HEIGHT/2 - 200 + 16*i);
            itemDisplayed.getFont().drawString(Core.WIDTH/2 + 100 + 16, Core
                    .HEIGHT/2 - 200 + 16*i, itemDisplayed.getName(), Color
                    .blue);
        }
    }
}
