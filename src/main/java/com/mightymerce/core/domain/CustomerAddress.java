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
 * A CustomerAddress.
 */
@Entity
@Table(name = "CUSTOMERADDRESS")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CustomerAddress implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    

    @NotNull
    @Size(max = 100)        
    @Column(name = "address_to", length = 100, nullable = false)
    private String addressTo;

    @NotNull
    @Size(max = 200)        
    @Column(name = "street_address", length = 200, nullable = false)
    private String streetAddress;

    @NotNull
    @Size(max = 20)        
    @Column(name = "zip", length = 20, nullable = false)
    private String zip;

    @NotNull
    @Size(max = 100)        
    @Column(name = "city", length = 100, nullable = false)
    private String city;

    @NotNull
    @Size(max = 100)        
    @Column(name = "state", length = 100, nullable = false)
    private String state;

    @NotNull
    @Size(max = 100)        
    @Column(name = "country", length = 100, nullable = false)
    private String country;
    
    @Column(name = "status")
    private String status;

    @ManyToOne
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddressTo() {
        return addressTo;
    }

    public void setAddressTo(String addressTo) {
        this.addressTo = addressTo;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

        CustomerAddress customerAddress = (CustomerAddress) o;

        if ( ! Objects.equals(id, customerAddress.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CustomerAddress{" +
                "id=" + id +
                ", addressTo='" + addressTo + "'" +
                ", streetAddress='" + streetAddress + "'" +
                ", zip='" + zip + "'" +
                ", city='" + city + "'" +
                ", state='" + state + "'" +
                ", country='" + country + "'" +
                ", status='" + status + "'" +
                '}';
    }
}
