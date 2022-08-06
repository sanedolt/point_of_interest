package com.poi.poi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "trip")
@JsonDeserialize(builder = Trip.Builder.class)
public class Trip implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long tripId;
    @ManyToOne
    @JoinColumn(name = "username")
    @JsonIgnore
    private User user;
    private String name;

//    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @JoinTable(name = "trip_records", joinColumns = { @JoinColumn(name = "tripId")}, inverseJoinColumns = {@JoinColumn(name = "poi_id")})
//    Set<Poi> poiSet = new HashSet<>();

    public Long getId() {
        return tripId;
    }

    public User getUser() {
        return user;
    }

    public Trip setUser(User user){
        this.user = user;
        return this;
    }

    public String getName() {
        return name;
    }

    public Trip() {}

    public Trip(Builder builder){
        this.name = builder.name;
        this.tripId = builder.tripId;
        this.user = builder.user;
    }

    public static class Builder {
        @JsonProperty("id")
        private Long tripId;

        @JsonProperty("name")
        private String name;

        @JsonProperty("user")
        private User user;

        public Builder setId(Long tripId) {
            this.tripId = tripId;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setUser(User user) {
            this.user = user;
            return this;
        }

        public Trip build() {
            return new Trip(this);
        }
    }
}
