package com.poi.poi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.persistence.*;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.*;

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
    @JsonProperty("name")
    private String name;
    @JsonProperty("checkin")
    private LocalDate checkinDate;
    @JsonProperty("checkout")
    private LocalDate checkoutDate;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "trip_records", joinColumns = { @JoinColumn(name = "tripId")}, inverseJoinColumns = {@JoinColumn(name = "poi_id")})
    Set<Poi> poiSet = new HashSet<>();
    public Set<Poi> getPoiSet() {
        return poiSet;
    }

    public void setPoiSet(Set<Poi> poiSet) {
        this.poiSet = poiSet;
    }

//    public boolean addPoiToTrip(Poi poi){
//        return poiSet.add(poi);
//    }

    @JsonProperty("plan")
    String plan="";
    public String getPlan() {
        return plan;
    }
    public void setPlan(String plan) {
        this.plan = plan;
    }

    public Long getTripId() {
        return tripId;
    }

    public User getUser() {
        return user;
    }

//    public Trip setUser(User user){
//        this.user = user;
//        return this;
//    }
    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name=name;
    }

    public LocalDate getCheckinDate() {
        return checkinDate;
    }

    public void setCheckinDate(LocalDate date) {
        this.checkinDate = date;
    }

    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(LocalDate date) {
        this.checkoutDate = date;
    }
    public Trip() {}

    public Trip(Builder builder){
        this.name = builder.name;
        this.tripId = builder.tripId;
        this.user = builder.user;
        this.checkinDate = builder.checkinDate;
        this.checkoutDate = builder.checkoutDate;
    }

    public static class Builder {
        @JsonProperty("id")
        private Long tripId;

        @JsonProperty("name")
        private String name;

        @JsonProperty("user")
        private User user;

        @JsonProperty("checkin")
        private LocalDate checkinDate;

        @JsonProperty("checkout")
        private LocalDate checkoutDate;

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

        public Builder setCheckinDate(LocalDate date) {
            this.checkinDate = date;
            return this;
        }

        public Builder setCheckoutDate(LocalDate date) {
            this.checkoutDate = date;
            return this;
        }

        public Trip build() {
            return new Trip(this);
        }
    }
}
