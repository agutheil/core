package com.mightymerce.checkout;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by agutheil on 12.05.15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Order {
    public void setId(Long id) {
        this.id = id;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public void setTest2(String test2) {
        this.test2 = test2;
    }

    private Long id;

    public Long getId() {
        return id;
    }

    public String getTest() {
        return test;
    }

    public String getTest2() {
        return test2;
    }

    private String test;

        private String test2;
}
