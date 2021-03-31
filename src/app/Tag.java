package app;

import java.io.Serializable;

public class Tag implements Serializable {

    public String key;
    public String value;

    public Tag(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getTag() {
        return "( " + this.key + " , " + this.value + " )";
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

}
