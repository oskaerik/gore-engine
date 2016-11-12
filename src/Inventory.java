import java.util.ArrayList;

/**
 * The player class
 * @author Oskar Eriksson and Gustave Rousselet
 * @version 0.1
 */
public class Inventory {

    private ArrayList<Item> items;

    public Inventory() {
        items = new ArrayList<Item>();
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
}
