package com.mightymerce.core.web.rest.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;


/**
 * A DTO for the Product entity.
 */
public class ProductDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 64)
    private String productId;

    @NotNull
    @Size(max = 70)
    private String title;

    @NotNull
    @Size(max = 200)
    private String description;

    @NotNull
    private Double price;

    @NotNull
    @Size(max = 120)
    private String tax;

    @NotNull
    @Size(max = 20)
    private String currency;

    @NotNull
    @Size(min = 1, max = 20971520)
    @Lob
    private byte[] mainImage;

    @NotNull
    @Size(max = 200)
    private String mainImageAlt;

    private Long userId;

    private Long deliveryOptionId;

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDeliveryOptionId() {
        return deliveryOptionId;
    }

    public void setDeliveryOptionId(Long deliveryOptionId) {
        this.deliveryOptionId = deliveryOptionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProductDTO productDTO = (ProductDTO) o;

        if ( ! Objects.equals(id, productDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ProductDTO{" +
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
