package app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import app.Album;

public class User implements Serializable {

    public String username;
    public ArrayList<Album> albums;

    public User(String username) {
        this.username = username;
        this.albums = new ArrayList<Album>();
    }

    public String getUsername() {
        return this.username;
    }

    public ArrayList<Album> getAlbums() {
        return albums;
    }

    public int numberOfAlbums() {return albums.size();}

    public void addAlbum(Album album) throws IllegalArgumentException {
        if (album == null) {
            throw new IllegalArgumentException("Album cannot be null!");
        }

        if (album.getName().isEmpty()) {
            throw new IllegalArgumentException("Please enter a name.");
        }

        for (Album a: albums) {
            if (a.getName().equals(album.getName()))
                throw new IllegalArgumentException("You cannot add an album with the same name!");
        }
        albums.add(album);
    }

    public Album getAlbum(String name) {
        for (Album a: this.albums) {
            if (a.getName().equals(name))
                return a;
        }
        return null;
    }

    public void editAlbum(String oldName, String newName) throws IllegalArgumentException {
        for (Album a: albums) {
            if (a.getName().equals(newName) && !oldName.equals(newName)) //excludes the case where a user can decide to make no changes to the old album
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

    public void deleteAlbum(String name) throws IllegalArgumentException {
        if (name == null || name.isEmpty()) throw new IllegalArgumentException("Please enter an album to delete!");
        boolean noAlbumExists = true;
        Iterator<Album> iter = albums.iterator();
        while (iter.hasNext()) {
            Album a = iter.next();
            if (a.getName().equals(name)) {
                iter.remove();
                noAlbumExists = false;
            }

        }
        if (noAlbumExists) throw new IllegalArgumentException("Album with that name does not exist!");
    }

    public Album getAlbumWithName(String name) {
        for (Album a: this.albums) {
            if (a.getName().equals(name))
                return a;
        }
        return null;
    }

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
            if (p.sameImage(photo.getImage())) {
                photoExistsinAlbum = true;
                photoInAlbum = p;

            }
        }

        //if the given photo does not exist in the start album, throw exception
        if (!photoExistsinAlbum) throw new IllegalArgumentException("This photo does not exist in this album!");

        //finally adds the same photo to the end album
        end.addPhoto(photoInAlbum);

    }

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
            if (p.sameImage(photo.getImage())) {
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
