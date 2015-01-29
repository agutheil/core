package com.schubber.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by agutheil on 29.01.15.
 */
@Entity
public class Account {
    @Id
    @GeneratedValue
    private Long id;
    @OneToMany(mappedBy = "account")
    private Set<Product> products = new HashSet<>();
    private String username;
    @JsonIgnore
    private String password;

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

    Account() {
        // JPA Constructor
    }

    public Long getId() {
        return id;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
