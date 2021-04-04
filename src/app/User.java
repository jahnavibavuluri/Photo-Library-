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

        if (album.getName().isBlank()) {
            throw new IllegalArgumentException("Please enter a name.");
        }

        for (Album a: albums) {
            if (a.getName().equals(album.getName()))
                throw new IllegalArgumentException("You cannot add an album with the same name!");
        }
        albums.add(album);
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
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Please enter an album to delete!");
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


}
