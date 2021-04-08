package model;

import app.User;

import java.io.*;
import java.util.ArrayList;

public class Serialize implements Serializable {
    public static final String storeDir = "data";
    public static final String storeFile = "users.dat";

    public static void writeApp(ArrayList<User> users) throws IOException {

        ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(storeDir + File.separator + storeFile));
        oos.writeObject(users);
        oos.close();
    }

    public static ArrayList<User> readApp() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(storeDir + File.separator + storeFile));
        ArrayList<User> users = (ArrayList<User>)ois.readObject();
        return users;
    }
}
