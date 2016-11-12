/**
 * Entity is the parent class of things in the world like items and objects
 * @author Oskar Eriksson and Gustave Rousselet
 * @version 0.1
 */
public class Entity {
    private String name;
    private String description;

    /** Constructor of the entity class
     * @param name Name of the entity
     * @param description Description of the entity
     */
    public Entity(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * @return Name of the entity
     */
    protected String getName() { return name; }

    /**
     * @return Description of the entity
     */
    protected String getDescription() { return description; }
}
