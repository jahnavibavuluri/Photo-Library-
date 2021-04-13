package app;

import java.io.Serializable;

/**
 * The Tag class is used to store and perform the needed
 * operations for the tags for each picture.
 *
 * @author Jahnavi Bavuluri and Chiraag Rekhari
 */
public class Tag implements Serializable {

    /**
     * The key of the tag.
     */
    public String key;
    /**
     * The value of the tag.
     */
    public String value;
    /**
     * The boolean value that dictates if the tag
     * can have multiple values or not.
     */
    public boolean x;

    /**
     * The tag constructor that initializes key,
     * value, and boolean value.
     *
     * @param key       the key of the tag
     * @param value     the value of the tag
     * @param x         true if the tag can have multiple
     *                  values, false otherwise
     */
    public Tag(String key, String value, boolean x) {
        //if x = true -> it can have multiple values
        //if x = false -> it can NOT have multiple values
        this.key = key;
        this.value = value;
        this.x = x;
    }

    /**
     * Getter method that returns the String representation of the tag.
     *
     * @return  the String representation of the tag
     */
    public String getTag() {
        return "( " + this.key + ", " + this.value + " )";
    }

    /**
     * Getter method that returns the key of the tag.
     *
     * @return  the key of the tag
     */
    public String getKey() {
        return this.key;
    }

    /**
     * Getter method that returns the value of the tag
     *
     * @return  the value of the tag
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Getter method that returns the multiple values
     * boolean of the tag.
     *
     * @return  the multiple values boolean of the tag
     */
    public boolean getX() {return this.x;}

}
