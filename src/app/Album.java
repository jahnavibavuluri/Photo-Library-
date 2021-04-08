package app;

import java.io.Serializable;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javafx.scene.image.Image;
import app.Photo;

public class Album implements Serializable {

    public String name;
    public ArrayList<Photo> photos;

    public Album(String name) {
        this.name = name;
        photos = new ArrayList<Photo>();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Photo> getPhotos() {
        return this.photos;
    }

    public int numPhotos() {
        return photos.size();
    }

    public void addPhoto(Photo photo) throws IllegalArgumentException {
        //checking that the same photo is not being added
        for (Photo p: photos) {
            boolean samePhoto = true;
            int width = getSmallerWidth(p,photo);
            int height = getSmallerHeight(p,photo);
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (p.getImage().getPixelReader().getArgb(j,i) != photo.getImage().getPixelReader().getArgb(j,i)) {
                        samePhoto = false;
                        break;
                    }
                }
            }
            if (samePhoto) throw new IllegalArgumentException("The same photo cannot be added to the same album!");
        }

        photos.add(photo);
    }

    public void deletePhoto(Photo photo) throws IllegalArgumentException {
        Iterator<Photo> iter = photos.iterator();
        while (iter.hasNext()) {
            Photo p = iter.next();
            boolean samePhoto = true;
            int width = getSmallerWidth(p,photo);
            int height = getSmallerHeight(p,photo);
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (p.getImage().getPixelReader().getArgb(j,i) != photo.getImage().getPixelReader().getArgb(j,i)) {
                        samePhoto = false;
                        break;
                    }
                }
            }
            if (samePhoto) {
                photos.remove(p);
                return;
            }
        }

        throw new IllegalArgumentException("Cannot find this photo in the album! (This exception should not be thrown since we know for a fact this photo exists in the album!)");
    }

    public int getSmallerWidth(Photo p1, Photo p2) {
        return p1.getImage().getWidth() < p2.getImage().getWidth() ? (int)p1.getImage().getWidth():(int)p2.getImage().getWidth();
    }

    public int getSmallerHeight(Photo p1, Photo p2) {
        return p1.getImage().getHeight() < p2.getImage().getHeight() ? (int)p1.getImage().getHeight():(int)p2.getImage().getHeight();
    }

    //will implement this later!
    public Date[] getRangeOfPhotos() {
        Date[] firstAndLast = new Date[2];
        //firstAndLast[0] = the earliest photo
        //firstAndLast[1] = the latest photo
        return firstAndLast;
    }


}
