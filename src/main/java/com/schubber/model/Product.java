package com.schubber.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.ManyToAny;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

/**
 * Created by agutheil on 16.12.14.
 */
@Entity
public class Product {

    @Id
    @GeneratedValue
    private Long id;

    @JsonIgnore
    @ManyToOne
    private Account account;
    private String title;
    private BigDecimal price;
    private Currency currency;
    private Long stock;

    Product() {
        // JPA Constructor
    }

    public Product(Account account, String title, BigDecimal price, Currency currency, Long stock) {
        this.account = account;
        this.title = title;
        this.price = price;
        this.currency = currency;
        this.stock = stock;
    }

    public Long getId() {
        return id;
    }

    public Account getAccount() {
        return account;
    }

    public String getTitle() {
        return title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Long getStock() {
        return stock;
    }
}
