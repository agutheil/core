package com.mightymerce.core.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.mightymerce.core.domain.enumeration.Country;

/**
 * A Address.
 */
@Entity
@Table(name = "ADDRESS")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Address implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    

    @NotNull        
    @Column(name = "addressee", nullable = false)
    private String addressee;
    
    @Column(name = "detailed_description")
    private String detailedDescription;

    @NotNull        
    @Column(name = "streetname", nullable = false)
    private String streetname;

    @NotNull        
    @Column(name = "housenumber", nullable = false)
    private String housenumber;

    @NotNull        
    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    @NotNull        
    @Column(name = "town", nullable = false)
    private String town;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "country")
    private Country country;

    @ManyToOne
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddressee() {
        return addressee;
    }

    public void setAddressee(String addressee) {
        this.addressee = addressee;
    }

    public String getDetailedDescription() {
        return detailedDescription;
    }

    public void setDetailedDescription(String detailedDescription) {
        this.detailedDescription = detailedDescription;
    }

    public String getStreetname() {
        return streetname;
    }

    public void setStreetname(String streetname) {
        this.streetname = streetname;
    }

    public String getHousenumber() {
        return housenumber;
    }

    public void setHousenumber(String housenumber) {
        this.housenumber = housenumber;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
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

        Address address = (Address) o;

        if ( ! Objects.equals(id, address.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", addressee='" + addressee + "'" +
                ", detailedDescription='" + detailedDescription + "'" +
                ", streetname='" + streetname + "'" +
                ", housenumber='" + housenumber + "'" +
                ", postalCode='" + postalCode + "'" +
                ", town='" + town + "'" +
                ", country='" + country + "'" +
                '}';
    }
}
