package com.mightymerce.core.web.rest.dto;

import java.io.Serializable;

/**
 * Created by smhumayun on 10/3/2015.
 */
public class LookupDTO implements Serializable {

    private String key;
    private String value;

    public LookupDTO() {
    }

    public LookupDTO(String key, String value) {
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("LookupDTO{");
        sb.append("key='").append(key).append('\'');
        sb.append(", value='").append(value).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
