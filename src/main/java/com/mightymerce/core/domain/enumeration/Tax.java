package com.mightymerce.core.domain.enumeration;

/**
 * Created by smhumayun on 10/3/2015.
 */
public enum Tax {

    GE_VAT_19PC ("Germany, 19 % VAT")
    ;

    private String key;
    private String value;

    Tax(String sameKeyValue) {
        this(sameKeyValue, sameKeyValue);
    }

    Tax(String key, String value) {
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
