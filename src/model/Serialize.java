package model;

import java.io.*;
import java.util.ArrayList;

/**
 * This class handles the serializing aspect of the application and
 * saves all of the users information when the app is closed.
 *
 * @author Jahnavi Bavuluri and Chiraag Rekhari
 */
public class Serialize implements Serializable {

    /**
     * The classes stream unique identifier
     */
    private static final long serialVersionUID = 1L;
    /**
     * This directory where the storage file is
     */
    public static final String storeDir = "data";
    /**
     * The actual file that is being written to and read from
     */
    public static final String storeFile = "users.dat";

    /**
     * This method writes to the storage file, which saves all of the
     * date across all of the users.
     *
     * @param users         This is the array list of users in the application
     * @throws IOException
     */
    public static void writeApp(ArrayList<User> users) throws IOException {

        ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(storeDir + File.separator + storeFile));
        oos.writeObject(users);
        oos.close();
    }

    /**
     * This method reads from the storage file, which allows the application to
     * run with the saves users information.
     *
     * @return                          an array list of users in the application
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static ArrayList<User> readApp() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(storeDir + File.separator + storeFile));
        ArrayList<User> users = (ArrayList<User>)ois.readObject();
        return users;
    }
}
