package app;

import java.util.ArrayList;
import java.util.Iterator;

import app.Album;

public class User {

    String username;
    ArrayList<Album> albums;

    public User(String username) {
        this.username = username;
        albums = new ArrayList<Album>();
    }

    public void addAlbum(String album) throws IllegalArgumentException {
        for (Album a: albums) {
            if (a.getName().equals(album))
                throw new IllegalArgumentException("You cannot add an album with the same name!");
        }
        albums.add(new Album(album));
    }

    public void editAlbum(String oldName, String newName) throws IllegalArgumentException {
        for (Album a: albums) {
            if (a.getName().equals(newName) && !oldName.equals(newName)) //excludes the case where a user can decide to make no changes to the old album
                throw new IllegalArgumentException("You cannot add an album with the same name!");
        }

        for (Album a: albums) {
            if (a.getName().equals(oldName)) {
                a.setName(newName);
            }
        }
    }



}
