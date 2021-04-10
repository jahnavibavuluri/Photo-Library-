package app;

import java.io.Serializable;
import java.nio.file.Files;
import java.util.*;

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

    public boolean addPhotoBoolean(ArrayList<Photo> p, Photo photo){
        for (Photo k: p){
            if (k.sameImage(photo)){
                System.out.println("returning false");
                return false;
            }
        }
        System.out.println("returning true");
        return true;
    }

    public void addPhoto(Photo photo) throws IllegalArgumentException {
        //checking that the same photo is not being added
        for (Photo p: photos) {
            boolean samePhoto = false;
            if (p.sameImage(photo)) {
                samePhoto = true;
            }
            /*int width = getSmallerWidth(p,photo);
            int height = getSmallerHeight(p,photo);
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (p.getImage().getPixelReader().getArgb(j,i) != photo.getImage().getPixelReader().getArgb(j,i)) {
                        samePhoto = false;
                        break;
                    }
                }
            }*/
            if (samePhoto) throw new IllegalArgumentException("The same photo cannot be added to the same album!");
        }

        photos.add(photo);
    }

    public void deletePhoto(Photo photo) throws IllegalArgumentException {
        Iterator<Photo> iter = photos.iterator();
        while (iter.hasNext()) {
            Photo p = iter.next();
            boolean samePhoto = false;
            if (p.sameImage(photo)) {
                samePhoto = true;
            }
            /*
            int width = getSmallerWidth(p,photo);
            int height = getSmallerHeight(p,photo);
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (p.getImage().getPixelReader().getArgb(j,i) != photo.getImage().getPixelReader().getArgb(j,i)) {
                        samePhoto = false;
                        break;
                    }
                }
            }*/
            if (samePhoto) {
                photos.remove(p);
                return;
            }
        }

        throw new IllegalArgumentException("Cannot find this photo in the album! (This exception should not be thrown since we know for a fact this photo exists in the album!)");
    }

    /*public int getSmallerWidth(Photo p1, Photo p2) {
        return p1.getImage().getWidth() < p2.getImage().getWidth() ? (int)p1.getImage().getWidth():(int)p2.getImage().getWidth();
    }

    public int getSmallerHeight(Photo p1, Photo p2) {
        return p1.getImage().getHeight() < p2.getImage().getHeight() ? (int)p1.getImage().getHeight():(int)p2.getImage().getHeight();
    }*/

    //will implement this later!
    public ArrayList<Date> getRangeOfPhotos() {
        ArrayList<Date> rangeOfDates = new ArrayList<Date>();
        ArrayList<Photo> copy = this.photos;
        if (copy.size() == 0) return null;
        if (copy.size() == 1) {
            rangeOfDates.add(copy.get(0).getActualDate());
            return rangeOfDates;
        }

        Collections.sort(copy, new Comparator<Photo>() {
            @Override
            public int compare(Photo p1, Photo p2) {
                return p1.getActualDate().compareTo(p2.getActualDate());
            }
        });

        rangeOfDates.add(copy.get(0).getActualDate());
        rangeOfDates.add(copy.get(copy.size()-1).getActualDate());
        return rangeOfDates;
    }


}
