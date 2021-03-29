package app;

import java.util.ArrayList;
import app.Photo;

public class Album {

    String name;
    ArrayList<Photo> photos;

    public Album(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
