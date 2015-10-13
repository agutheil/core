package com.mightymerce.core.domain.enumeration;

/**
 * The Currency enumeration.
 */
public enum Currency {

    EUR ("EUR")
    ;

    private String key;
    private String value;

    Currency(String sameKeyValue) {
        this(sameKeyValue, sameKeyValue);
    }

    Currency(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
