import java.util.Stack;

/**
 * The player class
 * @author Oskar Eriksson and Gustave Rousselet
 * @version 0.1
 */
public class Inventory {

    private Stack<Item> items;

    public Inventory() {
        items = new Stack<Item>();
    }

    public Item getItem() {
        return items.pop();
    }

    public boolean checkIfEmpty() {
        return items.isEmpty();
    }

    public int getNumberOfItems() {
        return items.size();
    }

    public void addItem(Item item) {
        items.push(item);
    }

    public Item removeItem() {
        return items.pop();
    }

    public Stack<Item> getItems() { return items;}
}
