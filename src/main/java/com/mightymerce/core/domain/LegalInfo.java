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
 * A LegalInfo.
 */
@Entity
@Table(name = "LEGALINFO")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class LegalInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    

    @NotNull
    @Size(max = 50)        
    @Column(name = "title", length = 50, nullable = false)
    private String title;

    @NotNull
    @Size(max = 100)        
    @Column(name = "purpose", length = 100, nullable = false)
    private String purpose;

    @NotNull
    @Size(max = 50)        
    @Column(name = "page_title", length = 50, nullable = false)
    private String pageTitle;

    @NotNull        
    @Column(name = "page_text", nullable = false)
    private String pageText;

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

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public String getPageText() {
        return pageText;
    }

    public void setPageText(String pageText) {
        this.pageText = pageText;
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

        LegalInfo legalInfo = (LegalInfo) o;

        if ( ! Objects.equals(id, legalInfo.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "LegalInfo{" +
                "id=" + id +
                ", title='" + title + "'" +
                ", purpose='" + purpose + "'" +
                ", pageTitle='" + pageTitle + "'" +
                ", pageText='" + pageText + "'" +
                '}';
    }
}
