package com.schubber.model;

import java.math.BigDecimal;

/**
 * Created by agutheil on 16.12.14.
 */
public class Product {
    public Product(Long id, String title, BigDecimal price, Currency currency, Long stock) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.currency = currency;
        this.stock = stock;
    }

    private final Long id;
    private final String title;
    private final BigDecimal price;
    private final Currency currency;
    private final Long stock;

    public Currency getCurrency() {
        return currency;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long getStock() {
        return stock;
    }

}
