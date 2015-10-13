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
 * A Product.
 */
@Entity
@Table(name = "PRODUCT")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    

    @NotNull
    @Size(max = 64)        
    @Column(name = "product_id", length = 64, nullable = false)
    private String productId;

    @NotNull
    @Size(max = 70)        
    @Column(name = "title", length = 70, nullable = false)
    private String title;

    @NotNull
    @Size(max = 200)        
    @Column(name = "description", length = 200, nullable = false)
    private String description;

    @NotNull        
    @Column(name = "price", nullable = false)
    private Double price;

    @NotNull
    @Size(max = 120)        
    @Column(name = "tax", length = 120, nullable = false)
    private String tax;

    @NotNull
    @Size(max = 20)        
    @Column(name = "currency", length = 20, nullable = false)
    private String currency;

    @NotNull
    @Size(min = 1, max = 20971520)        
    @Lob
    @Column(name = "main_image", nullable = false)
    private byte[] mainImage;

    @NotNull
    @Size(max = 200)        
    @Column(name = "main_image_alt", length = 200, nullable = false)
    private String mainImageAlt;

    @ManyToOne
    private User user;

    @ManyToOne
    private DeliveryOption deliveryOption;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public byte[] getMainImage() {
        return mainImage;
    }

    public void setMainImage(byte[] mainImage) {
        this.mainImage = mainImage;
    }

    public String getMainImageAlt() {
        return mainImageAlt;
    }

    public void setMainImageAlt(String mainImageAlt) {
        this.mainImageAlt = mainImageAlt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public DeliveryOption getDeliveryOption() {
        return deliveryOption;
    }

    public void setDeliveryOption(DeliveryOption deliveryOption) {
        this.deliveryOption = deliveryOption;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Product product = (Product) o;

        if ( ! Objects.equals(id, product.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", productId='" + productId + "'" +
                ", title='" + title + "'" +
                ", description='" + description + "'" +
                ", price='" + price + "'" +
                ", tax='" + tax + "'" +
                ", currency='" + currency + "'" +
                ", mainImage='" + mainImage + "'" +
                ", mainImageAlt='" + mainImageAlt + "'" +
                '}';
    }
}
