package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class is responsible for creating a User Instance which is populated in the UsersList ArrayList
 *
 * @author Chiraag Rekhari
 * @author Jahnavi Bavuluri
 */
public class User implements Serializable {
    /**
     * Uername of the user to be created
     */
    public String username;
    /**
     * Album arrayList of the user to be created
     */
    public ArrayList<Album> albums;
    /**
     * preset Tags arrayList and every user starts with weather, person, and location preset Tags
     */
    public ArrayList<String> presetTags;

    /**
     * Contructor to instantiate a User object with a username, Albums arraylist, and preset Tags
     * @param username Uername of the user
     */
    public User(String username) {
        this.username = username;
        this.albums = new ArrayList<Album>();
        this.presetTags = new ArrayList<String>();
        presetTags.add("weather");
        presetTags.add("person");
        presetTags.add("location");
    }

    /**
     * Adds a preset Tag to the preset Preset Tags arraylist
     * @param tag Tag to be added to the Preset Tags arraylist
     */
    public void addPreset(Tag tag){
        for(int i=0; i<presetTags.size(); i++){

            if(presetTags.get(i).equals(tag.getKey())){
                return;
            }
        }
        presetTags.add(tag.getKey());
    }

    /**
     * Prints the preset Tags in the preset Tags arraylist
     * @return Returns the preset Tags as a String
     */
    public String printPreset(){
        StringBuilder allPresetTags = new StringBuilder();
        for(String t: presetTags){
            allPresetTags.append(t);
            allPresetTags.append(", ");
        }
        return allPresetTags.toString();
    }

    /**
     * Deletes the preset Tags from the preset Tags arraylist
     * @param tag The tag to be deleted in the preset Tags arraylist
     */
    public void deletePreset(Tag tag){
        if(tag.getKey().equals("weather") || tag.getKey().equals("location")){
            return;
        }
        for(int i=0; i<presetTags.size(); i++){
            if(presetTags.get(i).equals(tag.getKey())){
                presetTags.remove(i);
            }
        }
    }

    /**
     * Getter method to get the username of a user
     * @return Returns the name of a user as a String
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Getter method to get the albums ArrayList of a user
     * @return returns the albums ArrayList
     */
    public ArrayList<Album> getAlbums() {
        return albums;
    }

    /**
     * Returns the size of the albums ArrayList
     * @return Returns the size of the albums arraylist as an int
     */
    public int numberOfAlbums() {return albums.size();}

    /**
     * Adds an Albums to the albums arraylist and checks if the name is not already in use
     * @param album the album to be added to the albums arraylist
     * @throws IllegalArgumentException
     */
    public void addAlbum(Album album) throws IllegalArgumentException {
        if (album == null) {
            throw new IllegalArgumentException("Album cannot be null!");
        }

        if (album.getName().isEmpty()) {
            throw new IllegalArgumentException("Please enter a name.");
        }

        for (Album a: albums) {
            if (a.getName().equalsIgnoreCase(album.getName()))
                throw new IllegalArgumentException("You cannot add an album with the same name!");
        }
        albums.add(album);
    }

    /**
     * Getter method to get the Album with a particular name
     * @param name The name of the album found
     * @return The Album that has the particular name that is being searched for
     */
    public Album getAlbum(String name) {
        for (Album a: this.albums) {
            System.out.println("the name of the album is: " + a.getName());
            if (a.getName().equalsIgnoreCase(name)) {
                return a;
            }
        }
        return null;
    }

    /**
     * Edits the name of an album
     * @param oldName The old name of the album
     * @param newName The new name to be changed to of an albuym
     * @throws IllegalArgumentException
     */
    public void editAlbum(String oldName, String newName) throws IllegalArgumentException {
        for (Album a: albums) {
            if (a.getName().equalsIgnoreCase(newName) && !oldName.equalsIgnoreCase(newName)) //excludes the case where a user can decide to make no changes to the old album
                throw new IllegalArgumentException("You cannot add an album with the same name!");
        }
        boolean noAlbumExists = true;

        for (Album a: albums) {
            if (a.getName().equals(oldName)) {
                a.setName(newName);
                noAlbumExists = false;
            }
        }

        if (noAlbumExists) throw new IllegalArgumentException(oldName + " does not exist!");
    }

    /**
     * Deletes the album and removes it from the albums arraylist
     * @param name The name of the album to be deleted
     * @throws IllegalArgumentException
     */
    public void deleteAlbum(String name) throws IllegalArgumentException {
        if (name == null || name.isEmpty()) throw new IllegalArgumentException("Please enter an album to delete!");
        boolean noAlbumExists = true;
        Iterator<Album> iter = albums.iterator();
        while (iter.hasNext()) {
            Album a = iter.next();
            if (a.getName().equalsIgnoreCase(name)) {
                iter.remove();
                noAlbumExists = false;
            }

        }
        if (noAlbumExists) throw new IllegalArgumentException("Album with that name does not exist!");
    }

    /**
     * Finds the Album with the name passed in
     * @param name The name of the album being searched for
     * @return The album with the particular name
     */
    public Album getAlbumWithName(String name) {
        for (Album a: this.albums) {
            if (a.getName().equals(name))
                return a;
        }
        return null;
    }

    /**
     * Copies a photo from one album to another album
     * @param photo The photo to be copied
     * @param start The Album where the photo originally resides
     * @param end The album where the photo will be copied to
     * @throws IllegalArgumentException
     */
    public void copyPhoto(Photo photo, Album start, Album end) throws IllegalArgumentException {
        //makes sure either album is not null
        if (start == null || end == null)
            throw new IllegalArgumentException("Album does not exist!");
        boolean startAlbumDoesNotExist = true;
        boolean endAlbumDoesNotExist = true;
        Album startAlbum = null;
        Album endAlbum = null;

        //finds the given albums in the users albums arraylist
        for (Album a: this.albums) {
            if (a.getName().equals(start.getName())) {
                startAlbumDoesNotExist = false;
                startAlbum = a;
            }

            if (a.getName().equals((end.getName()))) {
                endAlbumDoesNotExist = false;
                endAlbum = a;
            }
        }
        //throws an exception if either album is not found
        if (startAlbumDoesNotExist || endAlbumDoesNotExist) throw new IllegalArgumentException("Album does not exist!");
        //finds the given photo in the start album
        boolean photoExistsinAlbum = false;
        Photo photoInAlbum = null;
        for (Photo p: startAlbum.getPhotos()) {
            if (p.sameImage(photo)) {
                photoExistsinAlbum = true;
                photoInAlbum = p;

            }
        }

        //if the given photo does not exist in the start album, throw exception
        if (!photoExistsinAlbum) throw new IllegalArgumentException("This photo does not exist in this album!");

        //finally adds the same photo to the end album
        end.addPhoto(photoInAlbum);

    }

    /**
     * Moves a photo from one album to another album
     * @param photo The photo to be moved
     * @param start The Album where the photo originally resides
     * @param end The album where the photo will be moved to
     * @throws IllegalArgumentException
     */
    public void movePhoto(Photo photo, Album start, Album end) throws IllegalArgumentException {
        //makes sure either album is not null
        if (start == null || end == null)
            throw new IllegalArgumentException("Album does not exist!");
        boolean startAlbumDoesNotExist = true;
        boolean endAlbumDoesNotExist = true;
        Album startAlbum = null;
        Album endAlbum = null;

        //finds the given albums in the users albums arraylist
        for (Album a: this.albums) {
            if (a.getName().equals(start.getName())) {
                startAlbumDoesNotExist = false;
                startAlbum = a;
            }

            if (a.getName().equals((end.getName()))) {
                endAlbumDoesNotExist = false;
                endAlbum = a;
            }
        }
        //throws an exception if either album is not found
        if (startAlbumDoesNotExist || endAlbumDoesNotExist) throw new IllegalArgumentException("Album does not exist!");
        //finds the given photo in the start album
        boolean photoExistsinAlbum = false;
        Photo photoInAlbum = null;
        for (Photo p: startAlbum.getPhotos()) {
            if (p.sameImage(photo)) {
                photoExistsinAlbum = true;
                photoInAlbum = p;

            }
        }

        //if the given photo does not exist in the start album, throw exception
        if (!photoExistsinAlbum) throw new IllegalArgumentException("This photo does not exist in this album!");

        //finally adds the same photo to the end album
        end.addPhoto(photoInAlbum);
        start.deletePhoto(photoInAlbum);

    }


}
