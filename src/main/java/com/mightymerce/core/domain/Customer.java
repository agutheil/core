package com.mightymerce.core.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mightymerce.core.domain.enumeration.Salutation;

/**
 * A Customer.
 */
@Entity
@Table(name = "CUSTOMER")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Customer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    
    @Enumerated(EnumType.STRING)
    @Column(name = "salutation")
    private Salutation salutation;

    @NotNull        
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "middle_name")
    private String middleName;

    @NotNull        
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotNull        
    @Column(name = "payer_id", nullable = false)
    private String payerId;

    @JsonIgnore
    @ManyToOne
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Salutation getSalutation() {
        return salutation;
    }

    public void setSalutation(Salutation salutation) {
        this.salutation = salutation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPayerId() {
        return payerId;
    }

    public void setPayerId(String payerId) {
        this.payerId = payerId;
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

        Customer customer = (Customer) o;

        if ( ! Objects.equals(id, customer.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", salutation='" + salutation + "'" +
                ", name='" + name + "'" +
                ", middleName='" + middleName + "'" +
                ", lastName='" + lastName + "'" +
                ", payerId='" + payerId + "'" +
                '}';
    }
}
