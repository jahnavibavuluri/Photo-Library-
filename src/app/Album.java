package app;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;

import javafx.scene.image.Image;
import app.Photo;

public class Album {

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

    public String getPhotos() {
        return photos.toString();
    }

    public void addPhoto(Photo photo) throws IllegalArgumentException {
        //checking that the same photo is not being added
        for (Photo p: photos) {
            boolean samePhoto = true;
            for (int i = 0; i < p.image.getHeight(); i++) {
                for (int j = 0; j < p.image.getWidth(); j++) {
                    if (p.image.getPixelReader().getArgb(i,j) != photo.image.getPixelReader().getArgb(i, j)) {
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
            for (int i = 0; i < p.image.getHeight(); i++) {
                for (int j = 0; j < p.image.getWidth(); j++) {
                    if (p.image.getPixelReader().getArgb(i,j) != photo.image.getPixelReader().getArgb(i, j)) {
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

    //will implement this later!
    public Image[] getRangeOfPhotos() {
        Image[] firstAndLast = new Image[2];
        //firstAndLast[0] = the earliest photo
        //firstAndLast[1] = the latest photo
        return firstAndLast;
    }


}
