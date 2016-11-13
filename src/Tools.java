import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

/**
 * Contains static methods like generating animations and such
 * @author Oskar Eriksson and Gustave Rousselet
 * @version 0.1
 */
public class Tools {

    /**
     * @param type Type of the animation, i.e "character"
     * @param name Name of the thing to be animated
     * @return An ArrayList of Animation objects with the indexes 0: "up", 1: "down", 2: "left", 3: "right"
     * @throws SlickException
     */
    public static ArrayList<Animation> createAnimation(String type, String name) throws SlickException {
        ArrayList<Animation> animationArray = new ArrayList<>();
        ArrayList<String> keyWords = new ArrayList<>();
        String pathToFolder = "res/";

        // If it's a character animation the keywords should be it's directions and the folder "res/characters/"
        if (type.equals("character")) {
            String[] directions = {"up", "down", "left", "right"};
            for (String direction : directions) {
                keyWords.add(direction);
            }
            pathToFolder += "characters/" + name + "/";
        }
        // If it's a projectile animation the folder is "res/projectiles/[NAME]" and the keyword is it's name
        else if (type.equals("projectile")) {
            keyWords.add(name);
            pathToFolder += "projectiles/" + name + "/";
        }

        for (String keyWord : keyWords) {
            Animation animation = new Animation();

            // Generate a path to the image file to be added to the animation, loops through the files from
            // [IMAGE NAME]-1.png until it hits a file that doesn't exist (then next keyWord)
            int i = 1;
            String path = pathToFolder + keyWord + "-" + i + ".png";
            File file = new File(path);
            // While there is another file matching the pattern, add frame to animation
            while (file.exists()) {
                animation.addFrame(new Image(path), 100);

                // Increase i by one and check for next file
                i++;
                path = pathToFolder + keyWord + "-" + i + ".png";
                file = new File(path);
            }
            // Adds the created animation to the array of animations
            animationArray.add(animation);
        }
        return animationArray;
    }

    /**
     * Reads a movement file and stores it in an ArrayList
     * The character has a movement.txt-file in the res/characters/[characterName]-folder
     * The movement.txt-file is built like this, starting from line 0
     * Line number: Property [description, not in the actual file]
     * 0: 2D [2 steps DOWN]
     * 1: 3L [3 steps LEFT]
     * @return Returns an ArrayList with the movement pattern (or null if fail)
     */
    public static ArrayList<String> readInstructions(String type, String name) {
        try {
            // Read the file according to character name
            BufferedReader br = new BufferedReader(
                    new FileReader("res/characters/" + name + "/" + type + ".txt"));

            // Read line and add to ArrayList
            ArrayList<String> returnArray = new ArrayList<>();
            String line = br.readLine();
            while (line != null) {
                returnArray.add(line);
                line = br.readLine();
            }
            for (String arrayLine : returnArray) {
                System.out.println("Line: " + arrayLine);
            }
            return returnArray;

            // If there's no file to be found or there was an error reading the file, return null
        } catch (FileNotFoundException FileNotFoundEx) {
            return null;
        } catch (IOException IOEx) {
            return null;
        }
    }
}
