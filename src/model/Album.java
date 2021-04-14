package model;

import java.io.Serializable;
import java.util.*;

/**
 * This class is responsible for creating an Album Instance which is populated in the albums ArrayList
 *
 * @author Chiraag Rekhari
 * @author Jahnavi Bavuluri
 */
public class Album implements Serializable {

    /**
     * Name of the album to be created
     */
    public String name;
    /**
     * The photos arrayList that the album will have
     */
    public ArrayList<Photo> photos;

    /**
     * Contructor to instantiate a Album object with a name and photos arraylist
     * @param name
     */
    public Album(String name) {
        this.name = name;
        photos = new ArrayList<Photo>();
    }

    /**
     * Getter method to get the name of a particular album
     * @return Returns the name of an album
     */
    public String getName() {
        return this.name;
    }

    /**
     * Setter method to set the name of a particular album
     * @param name The new name of the album
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter method to get the photos ArrayList of an album
     * @return Returns the photos ArrayList
     */
    public ArrayList<Photo> getPhotos() {
        return this.photos;
    }

    /**
     * Finds the size of the photos Arraylist
     * @return Returns the size of the photos ArrayList as an int
     */
    public int numPhotos() {
        return photos.size();
    }

    /**
     * Checks if the photos has already been added to albums ArrayList without throwing an exception
     * @param p photo arrayList of an album
     * @param photo photo to be added
     * @return Returns true if it is already in the album and false if it not already in the album
     */
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

    /**
     * Checks if the photos has already been added to albums ArrayList and throws an exception if it has
     * @param photo photo to be added
     * @throws IllegalArgumentException
     */
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

    /**
     * Deletes a photo from the photos ArrayList
     * @param photo Photo to be deleted
     * @throws IllegalArgumentException
     */
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


    //will implement this later!

    /**
     * Gets the date range of the photos in the photos arraylist
     * @return Returns the date range in a Date ArrayList with the first element being the oldest date and the second being the most recent date
     */
    public ArrayList<Date> getRangeOfPhotos() {
        ArrayList<Date> rangeOfDates = new ArrayList<Date>();
        ArrayList<Photo> copy = this.photos;
        if (copy.size() == 0) return null;
        if (copy.size() == 1) {
            rangeOfDates.add(copy.get(0).getActualDate());
            return rangeOfDates;
        }
        Collections.sort(copy, new Comparator<Photo>() {
            /**
             * Sorts the copy array list by date
             * @param p1 First photo to be compared
             * @param p2 Second photo to be compared
             * @return
             */
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
