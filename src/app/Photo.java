package app;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Photo implements Serializable {

    //public Image image;
    public File image;
    //public Path filePath;
    public Calendar calendar;
    public Date date;
    public ArrayList<Tag> tags;
    public String caption;

    public Photo() {
        this.calendar = new GregorianCalendar();
        this.calendar.set(Calendar.MILLISECOND, 0);
        this.date = this.calendar.getTime();
        image = null;
        this.tags = new ArrayList<Tag>();
    }

    public Photo(File pic) {
        this.image = pic;
        this.tags = new ArrayList<Tag>();
        this.calendar = new GregorianCalendar();
        this.calendar.set(Calendar.MILLISECOND, 0);
        this.date = this.calendar.getTime();
    }

    public String getDate() {
        return date.toString();
    }

    public Date getActualDate() { return this.date; }

    public String getCaption() {
        return this.caption;
    }

    public void setCaption(String caption) throws IllegalArgumentException {
        if (caption == null || caption.isEmpty()) {
            throw new IllegalArgumentException("Caption cannot be empty!");
        }
        this.caption = caption;
    }

    public ArrayList<Tag> getTags() {
        return this.tags;
    }

    public File getFile() {
        return this.image;
    }

    /*public Path getPath() {
        return this.filePath;
    }*/

    public void addTag(Tag tag) throws IllegalArgumentException {
        String key = tag.getKey();
        String value = tag.getValue();
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

    public String getDisplayTags() {
        StringBuilder allTags = new StringBuilder();
        for (Tag t: this.tags) {
            allTags.append(t.getTag());
            allTags.append("  ");
        }
        return allTags.toString();
    }

    /*
    public boolean sameImage(Image img) {
        boolean samePhoto = true;
        int width = getSmallerWidth(this.image,img);
        int height = getSmallerHeight(this.image,img);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (this.image.getPixelReader().getArgb(j,i) != img.getPixelReader().getArgb(j,i)) {
                    samePhoto = false;
                    break;
                }
            }
        }
        return (samePhoto) ? true:false;
    }


    public int getSmallerWidth(Image p1, Image p2) {
        return p1.getWidth() < p2.getWidth() ? (int)p1.getWidth():(int)p2.getWidth();
    }

    public int getSmallerHeight(Image p1, Image p2) {
        return p1.getHeight() < p2.getHeight() ? (int)p1.getHeight():(int)p2.getHeight();
    }
    */
}
