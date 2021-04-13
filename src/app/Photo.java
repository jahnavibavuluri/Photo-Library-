package app;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * This class is responsible for creating a Photo Instance which can be populated into a user's albums
 *
 * @author Chiraag Rekhari
 * @author Jahnavi Bavuluri
 */
public class Photo implements Serializable {

    /**
     * The actual file being stored, which the photo is referenced
     */
    public File image;
    /**
     * The calendar used for the date of a photo
     */
    public Calendar calendar;
    /**
     * the date of a photo
     */
    public Date date;
    /**
     * Arraylist of all the tags a photo has
     */
    public ArrayList<Tag> tags;
    /**
     * The caption a particular photo has
     */
    public String caption;

    /**
     * Contructor to instantiate a photo object with the date and a tags array list
     */
    public Photo() {
        this.calendar = new GregorianCalendar();
        this.calendar.set(Calendar.MILLISECOND, 0);
        this.date = this.calendar.getTime();
        image = null;
        this.tags = new ArrayList<Tag>();
    }

    /**
     * Contructor to instantiate a photo object with the date, tags array list, and an Image
     * @param pic
     */
    public Photo(File pic) {
        this.image = pic;
        this.tags = new ArrayList<Tag>();
        this.calendar = new GregorianCalendar();
        this.calendar.set(Calendar.MILLISECOND, 0);
        this.date = this.calendar.getTime();
    }

    /**
     * Getter method that returns the date
     * @return returns the date as a String
     */
    public String getDate() {
        return date.toString();
    }

    /**
     *  Getter method that returns the actual date
     * @return returns the date as a date Object
     */
    public Date getActualDate() { return this.date; }

    /**
     * Returns the caption of a photo
     * @return Returns the caption of a photo as a String
     */
    public String getCaption() {
        return this.caption;
    }

    /**
     * Sets the caption for the first time
     * @param caption String passed in to set the caption of a photo
     * @throws IllegalArgumentException
     */
    public void setCaption(String caption) throws IllegalArgumentException {
        if (caption == null || caption.isEmpty()) {
            throw new IllegalArgumentException("Caption cannot be empty!");
        }
        this.caption = caption;
    }

    /**
     * getter method to get the Tags ArrayList
     * @return Returns the Tags ArrayList
     */
    public ArrayList<Tag> getTags() {
        return this.tags;
    }

    /**
     * getter method to get the File
     * @return Returns the File
     */
    public File getFile() {
        return this.image;
    }

    /**
     * Adds the tag to the Tags ArrayList of a particular photo
     * @param tag The Tag to be added to the Tags ArrayList
     * @throws IllegalArgumentException
     */
    public void addTag(Tag tag) throws IllegalArgumentException {
        String key = tag.getKey().trim();
        String value = tag.getValue().trim();
        for (Tag t: this.tags) {
            if (t.getKey().equals(key) && t.getValue().equals(value)) {
                throw new IllegalArgumentException("You can't add the same tag to a photo");
            }
            if(!t.getX() && t.getKey().equals(key)){
                throw new IllegalArgumentException("This key can NOT have multiple values");
            }
        }
        this.tags.add(tag);
        System.out.println("Tag has been added");
        for (Tag t: this.tags) {
            System.out.println(t.getKey() + " " + t.getValue());
        }
    }

    /**
     * Deletes the tag to the Tags ArrayList of a particular photo
     * @param tag The Tag to be deleted from the Tags ArrayList
     * @throws IllegalArgumentException
     */
    public void deleteTag(Tag tag) throws IllegalArgumentException {
        Iterator<Tag> iter = tags.iterator();
        String key = tag.getKey();
        String value = tag.getValue();
        while (iter.hasNext()) {
            Tag t = iter.next();
            if (t.getKey().equals(key) && t.getValue().equals(value)) {
                tags.remove(t);
                System.out.println(t.getKey() + " " + t.getValue());
                return;
            }
        }
        throw new IllegalArgumentException("Cannot find tag!");

    }

    /**
     * Checks if two images are the same image
     * @param photo Image passed in to check if it is the same
     * @return Returns True if the images are the same and False if they are not
     */
    public boolean sameImage(Photo photo) {
        try {
            if (Files.size(photo.image.toPath()) != Files.size(this.image.toPath())) {
                System.out.println("1: files are not the same");
                return false;
            }

            byte[] first = Files.readAllBytes(photo.image.toPath());
            byte[] second = Files.readAllBytes(this.image.toPath());
            if (Arrays.equals(first,second)) {
                System.out.println("2: files are the same");
            } else System.out.println("3: files are not the same");

            return Arrays.equals(first, second);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("4: files are not the same");
        return false;
    }

    /**
     * Getter method to display the Tags
     * @return Displays the Tags as a String
     */
    public String getDisplayTags() {
        StringBuilder allTags = new StringBuilder();
        for (Tag t: this.tags) {
            allTags.append(t.getTag());
            allTags.append("  ");
        }
        return allTags.toString();
    }
}
