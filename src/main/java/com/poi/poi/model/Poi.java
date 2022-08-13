package com.poi.poi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "poi")
@JsonDeserialize(builder = Poi.Builder.class)
public class Poi implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    private Long id;
    private String name;
    private String city;
    private String description;
    private String address;
    private Double timeTaken;
    @OneToMany(mappedBy = "poi", cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    private List<PoiImage> images;
    private Double latitude;
    private Double longitude;

    @JsonIgnore
    @ManyToMany(mappedBy = "poiSet")
    private Set<Trip> trips= new HashSet<>();

    public Set<Trip> getTrips() {
        return trips;
    }

    public void setTrips(Set<Trip> trips) {
        this.trips = trips;
    }

    @Override
    public String toString(){
        return String.format("%d", id);
    }


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }

    public Double getTimeTaken() {
        return timeTaken;
    }

    public List<PoiImage> getImages() {
        return images;
    }

    public Poi setImages(List<PoiImage> images){
        this.images = images;
        return this;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Poi() {}

    private Poi(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.city = builder.city;
        this.description = builder.description;
        this.address = builder.address;
        this.images = builder.images;
        this.timeTaken = builder.timeTaken;
        this.latitude = builder.latitude;
        this.longitude = builder.longitude;
    }

    public static class Builder {
        @JsonProperty("id")
        private Long id;
        @JsonProperty("name")
        private String name;
        @JsonProperty("city")
        private String city;
        @JsonProperty("description")
        private String description;
        @JsonProperty("address")
        private String address;
        @JsonProperty("images")
        private List<PoiImage> images;
        @JsonProperty("time_taken")
        private Double timeTaken;
        @JsonProperty("latitude")
        private Double latitude;
        @JsonProperty("longitude")
        private Double longitude;

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setCity(String city){
            this.city = city;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setAddress(String address) {
            this.address = address;
            return this;
        }

        public Builder setImages(List<PoiImage> images) {
            this.images = images;
            return this;
        }

        public Builder setTimeTaken(Double timeTaken){
            this.timeTaken = timeTaken;
            return this;
        }

        public Builder setLatitude(Double latitude){
            this.latitude = latitude;
            return this;
        }

        public Builder setLongitude(Double longitude){
            this.longitude = longitude;
            return this;
        }

        public Poi build() {
            return new Poi(this);
        }
    }

}

