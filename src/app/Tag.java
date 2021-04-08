package app;

import java.io.Serializable;

public class Tag implements Serializable {

    public String key;
    public String value;
    public boolean x;

    public Tag(String key, String value, boolean x) {
        //if x = true -> it can have multiple values
        //if x = false -> it can NOT have multiple values
        this.key = key;
        this.value = value;
        this.x = x;
    }

    public String getTag() {
        return "( " + this.key + ", " + this.value + " )";
    }

    public String getKey() {
        return this.key;
    }

    public String getValue() {
        return this.value;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean getX() {return this.x;}

}
