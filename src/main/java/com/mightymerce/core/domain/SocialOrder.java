package com.mightymerce.core.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.mightymerce.core.domain.enumeration.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mightymerce.core.domain.enumeration.DeliveryStatus;
import com.mightymerce.core.domain.enumeration.OrderStatus;

/**
 * A SocialOrder.
 */
@Entity
@Table(name = "SOCIALORDER")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SocialOrder implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    
    @Column(name = "payer_id")
    private String payerId;
    
    @Column(name = "transaction_id")
    private String transactionId;
    
    @Column(name = "total_amount", precision=10, scale=2)
    private BigDecimal totalAmount;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status")
    private DeliveryStatus deliveryStatus;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;

    @ManyToOne
    private Article article;

    @ManyToOne
    private Address delivery;

    @ManyToOne
    private Address billing;

    @JsonIgnore
    @ManyToOne
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPayerId() {
        return payerId;
    }

    public void setPayerId(String payerId) {
        this.payerId = payerId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Address getDelivery() {
        return delivery;
    }

    public void setDelivery(Address address) {
        this.delivery = address;
    }

    public Address getBilling() {
        return billing;
    }

    public void setBilling(Address address) {
        this.billing = address;
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

        SocialOrder socialOrder = (SocialOrder) o;

        if ( ! Objects.equals(id, socialOrder.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SocialOrder{" +
                "id=" + id +
                ", payerId='" + payerId + "'" +
                ", transactionId='" + transactionId + "'" +
                ", totalAmount='" + totalAmount + "'" +
                ", paymentStatus='" + paymentStatus + "'" +
                ", deliveryStatus='" + deliveryStatus + "'" +
                ", orderStatus='" + orderStatus + "'" +
                '}';
    }
}
