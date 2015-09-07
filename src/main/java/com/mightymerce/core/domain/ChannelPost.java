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
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.mightymerce.core.domain.enumeration.PublicationStatus;

/**
 * A ChannelPost.
 */
@Entity
@Table(name = "CHANNELPOST")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ChannelPost implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PublicationStatus status;
    
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "publication_date")
    private DateTime publicationDate;
    
    @Column(name = "external_post_key")
    private String externalPostKey;

    @ManyToOne
    private Article article;

    @ManyToOne
    private MerchantChannel merchantChannel;

    @ManyToOne
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PublicationStatus getStatus() {
        return status;
    }

    public void setStatus(PublicationStatus status) {
        this.status = status;
    }

    public DateTime getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(DateTime publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getExternalPostKey() {
        return externalPostKey;
    }

    public void setExternalPostKey(String externalPostKey) {
        this.externalPostKey = externalPostKey;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public MerchantChannel getMerchantChannel() {
        return merchantChannel;
    }

    public void setMerchantChannel(MerchantChannel merchantChannel) {
        this.merchantChannel = merchantChannel;
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

        ChannelPost channelPost = (ChannelPost) o;

        if ( ! Objects.equals(id, channelPost.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ChannelPost{" +
                "id=" + id +
                ", status='" + status + "'" +
                ", publicationDate='" + publicationDate + "'" +
                ", externalPostKey='" + externalPostKey + "'" +
                '}';
    }
}
