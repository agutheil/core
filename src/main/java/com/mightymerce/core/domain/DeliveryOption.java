package com.mightymerce.core.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DeliveryOption.
 */
@Entity
@Table(name = "DELIVERYOPTION")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DeliveryOption implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    

    @NotNull
    @Size(max = 50)        
    @Column(name = "title", length = 50, nullable = false)
    private String title;

    @NotNull
    @Size(max = 100)        
    @Column(name = "within", length = 100, nullable = false)
    private String within;

    @NotNull
    @Size(max = 100)        
    @Column(name = "country", length = 100, nullable = false)
    private String country;

    @NotNull
    @Size(max = 120)        
    @Column(name = "taxrow", length = 120, nullable = false)
    private String taxrow;

    @NotNull        
    @Column(name = "cost", nullable = false)
    private Double cost;

    @NotNull
    @Size(max = 20)        
    @Column(name = "currency", length = 20, nullable = false)
    private String currency;

    @ManyToOne
    private User user;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DeliveryOption deliveryOption = (DeliveryOption) o;

        if ( ! Objects.equals(id, deliveryOption.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "DeliveryOption{" +
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
