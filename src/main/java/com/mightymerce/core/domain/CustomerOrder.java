package com.mightymerce.core.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mightymerce.core.domain.util.CustomDateTimeDeserializer;
import com.mightymerce.core.domain.util.CustomDateTimeSerializer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.mightymerce.core.domain.enumeration.OrderStatus;
import com.mightymerce.core.domain.enumeration.PaymentStatus;
import com.mightymerce.core.domain.enumeration.Tax;
import com.mightymerce.core.domain.enumeration.Currency;

/**
 * A CustomerOrder.
 */
@Entity
@Table(name = "CUSTOMERORDER")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CustomerOrder implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @NotNull
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "placed_on", nullable = false)
    private DateTime placedOn;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;

    @NotNull
    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;

/*
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tax", nullable = false)
    private Tax tax;
*/
    @NotNull
    @Size(max = 120)
    @Column(name = "tax", length = 120, nullable = false)
    private String tax;

    @NotNull
    @Size(max = 100)
    @Column(name = "paypal_account_id", length = 100, nullable = false)
    private String paypalAccountId;

    @NotNull
    @Size(max = 100)
    @Column(name = "paypal_transaction_id", length = 100, nullable = false)
    private String paypalTransactionId;

/*
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    private Currency currency;
*/
    @NotNull
    @Size(max = 20)
    @Column(name = "currency", length = 20, nullable = false)
    private String currency;

/*
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tax_currency", nullable = false)
    private Currency taxCurrency;
*/
    @NotNull
    @Size(max = 20)
    @Column(name = "tax_currency", length = 20, nullable = false)
    private String taxCurrency;

    @ManyToOne
    private Product product;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private User user;

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

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setTaxCurrency(String taxCurrency) {
        this.taxCurrency = taxCurrency;
    }

    public String getCurrency() {
        return currency;
    }

    public String getTaxCurrency() {
        return taxCurrency;
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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

        CustomerOrder customerOrder = (CustomerOrder) o;

        if ( ! Objects.equals(id, customerOrder.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CustomerOrder{" +
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
