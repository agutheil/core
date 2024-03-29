package com.mightymerce.core.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mightymerce.core.domain.enumeration.Currency;

/**
 * A Article.
 */
@Entity
@Table(name = "ARTICLE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Article implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @NotNull        
    @Column(name = "code", nullable = false)
    private String code;

    @NotNull        
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull        
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull        
    @Column(name = "price", precision=10, scale=2, nullable = false)
    private BigDecimal price;

    @NotNull        
    @Column(name = "delivery_costs", precision=10, scale=2, nullable = false)
    private BigDecimal deliveryCosts;

    @NotNull        
    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    private Currency currency;

    @NotNull
    @Size(min = 1000, max = 2500000)        
    @Lob
    @Column(name = "image1", nullable = false)
    private byte[] image1;

    @NotNull
    @Size(min = 1000, max = 2500000)        
    @Lob
    @Column(name = "image2", nullable = false)
    private byte[] image2;

    @JsonIgnore
    @ManyToOne
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getDeliveryCosts() {
        return deliveryCosts;
    }

    public void setDeliveryCosts(BigDecimal deliveryCosts) {
        this.deliveryCosts = deliveryCosts;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public byte[] getImage1() {
        return image1;
    }

    public void setImage1(byte[] image1) {
        this.image1 = image1;
    }

    public byte[] getImage2() {
        return image2;
    }

    public void setImage2(byte[] image2) {
        this.image2 = image2;
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

        Article article = (Article) o;

        if ( ! Objects.equals(id, article.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", code='" + code + "'" +
                ", name='" + name + "'" +
                ", description='" + description + "'" +
                ", price='" + price + "'" +
                ", deliveryCosts='" + deliveryCosts + "'" +
                ", currency='" + currency + "'" +
                ", image1='" + image1 + "'" +
                ", image2='" + image2 + "'" +
                '}';
    }
}
