package app;

import javafx.scene.image.Image;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

public class Photo implements Serializable {

    public Image image;
    public Date date;
    public ArrayList<Tag> tags;
    public String caption;

    public Photo() {
        this.date = Calendar.getInstance().getTime();
        image = null;
        this.tags = new ArrayList<Tag>();
    }

    public Photo(Image image) {
        this.image = image;
        this.tags = new ArrayList<Tag>();
        this.date = Calendar.getInstance().getTime();
    }

    public String getDate() {
        return date.toString();
    }

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

    public Image getImage() {
        return this.image;
    }

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

}
