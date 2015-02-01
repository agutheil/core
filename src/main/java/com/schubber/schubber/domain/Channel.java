package com.schubber.schubber.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

/**
 * A Channel.
 */
@Document(collection = "T_CHANNEL")
public class Channel implements Serializable {

    @Id
    private String id;

    @Field("type")
    private String type;

    @Field("name")
    private String name;

    @Field("token")
    private String token;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Channel channel = (Channel) o;

        if (id != null ? !id.equals(channel.id) : channel.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "id=" + id +
                ", type='" + type + "'" +
                ", name='" + name + "'" +
                ", token='" + token + "'" +
                '}';
    }
}
