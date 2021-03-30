package app;

import javafx.scene.image.Image;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

public class Photo {

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
    }

    public String getDate() {
        return date.toString();
    }

    public String getCaption() {
        return this.caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getTags() {
        return tags.toString();
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
        }
        this.tags.add(tag);
    }

    public void deleteTag(Tag tag) throws IllegalArgumentException {
        Iterator<Tag> iter = tags.iterator();
        String key = tag.getKey();
        String value = tag.getValue();
        while (iter.hasNext()) {
            Tag t = iter.next();
            if (t.getKey().equals(key) && t.getValue().equals(value)) {
                tags.remove(t);
                return;
            }
        }
        throw new IllegalArgumentException("Cannot fine tag!");
    }

}
