package com.schubber.web;

import com.schubber.model.Currency;
import com.schubber.model.Product;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

/**
 * Created by agutheil on 31.01.15.
 */
public class ProductResource extends ResourceSupport{
    private Product product;
    @JsonProperty
    Long id;
    @JsonProperty
    Currency currency;
    @JsonProperty
    BigDecimal price;
    @JsonProperty
    Long stock;
    @JsonProperty
    String title;

    @JsonCreator
    public ProductResource(Product product) {
        id = product.getId();
        currency = product.getCurrency();
        price = product.getPrice();
        stock = product.getStock();
        title = product.getTitle();
        String username = product.getAccount().getUsername();
        this.product = product;
        this.add(linkTo(ProductRestController.class, username).withRel("products"));
        this.add(linkTo(methodOn(ProductRestController.class).readProduct(username, product.getId())).withSelfRel());
    }
}
