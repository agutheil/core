package com.mightymerce.core.web.rest.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the DeliveryOption entity.
 */
public class DeliveryOptionDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String title;

    @NotNull
    @Size(max = 100)
    private String within;

    @NotNull
    @Size(max = 100)
    private String country;

    @NotNull
    @Size(max = 120)
    private String taxrow;

    @NotNull
    private Double cost;

    @NotNull
    @Size(max = 20)
    private String currency;

    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWithin() {
        return within;
    }

    public void setWithin(String within) {
        this.within = within;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTaxrow() {
        return taxrow;
    }

    public void setTaxrow(String taxrow) {
        this.taxrow = taxrow;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DeliveryOptionDTO deliveryOptionDTO = (DeliveryOptionDTO) o;

        if ( ! Objects.equals(id, deliveryOptionDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "DeliveryOptionDTO{" +
                "id=" + id +
                ", title='" + title + "'" +
                ", within='" + within + "'" +
                ", country='" + country + "'" +
                ", taxrow='" + taxrow + "'" +
                ", cost='" + cost + "'" +
                ", currency='" + currency + "'" +
                '}';
    }
}
