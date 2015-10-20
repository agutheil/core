package com.mightymerce.core.web.rest.dto;

import org.joda.time.DateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.mightymerce.core.domain.enumeration.OrderStatus;
import com.mightymerce.core.domain.enumeration.PaymentStatus;
import com.mightymerce.core.domain.enumeration.Tax;
import com.mightymerce.core.domain.enumeration.Currency;
import com.mightymerce.core.domain.enumeration.Currency;

/**
 * A DTO for the CustomerOrder entity.
 */
public class CustomerOrderDTO implements Serializable {

    private Long id;

    @NotNull
    private DateTime placedOn;

    @NotNull
    private OrderStatus orderStatus;

    @NotNull
    private PaymentStatus paymentStatus;

    @NotNull
    private Double totalAmount;

    @NotNull
    private String tax;

    @NotNull
    @Size(max = 100)
    private String paypalAccountId;

    @NotNull
    @Size(max = 100)
    private String paypalTransactionId;

    @NotNull
    private String currency;

    @NotNull
    private String taxCurrency;

    private Long productId;

    private Long customerId;

    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DateTime getPlacedOn() {
        return placedOn;
    }

    public void setPlacedOn(DateTime placedOn) {
        this.placedOn = placedOn;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaypalAccountId() {
        return paypalAccountId;
    }

    public void setPaypalAccountId(String paypalAccountId) {
        this.paypalAccountId = paypalAccountId;
    }

    public String getPaypalTransactionId() {
        return paypalTransactionId;
    }

    public void setPaypalTransactionId(String paypalTransactionId) {
        this.paypalTransactionId = paypalTransactionId;
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

    public String getTaxCurrency() {
        return taxCurrency;
    }

    public void setTaxCurrency(String taxCurrency) {
        this.taxCurrency = taxCurrency;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
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

        CustomerOrderDTO customerOrderDTO = (CustomerOrderDTO) o;

        if ( ! Objects.equals(id, customerOrderDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CustomerOrderDTO{" +
                "id=" + id +
                ", placedOn='" + placedOn + "'" +
                ", orderStatus='" + orderStatus + "'" +
                ", paymentStatus='" + paymentStatus + "'" +
                ", totalAmount='" + totalAmount + "'" +
                ", tax='" + tax + "'" +
                ", paypalAccountId='" + paypalAccountId + "'" +
                ", paypalTransactionId='" + paypalTransactionId + "'" +
                ", currency='" + currency + "'" +
                ", taxCurrency='" + taxCurrency + "'" +
                '}';
    }
}
