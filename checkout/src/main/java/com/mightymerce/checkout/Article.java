package com.mightymerce.checkout;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

/**
 * Created by agutheil on 11.05.15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Article {
    private String articleId;

    private String name;

    private String description;

    private BigDecimal price;

    private String currency;

    private String paypal;

    public String getArticleId() {
        return articleId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }

    public String getPaypal() {
        return paypal;
    }
}
