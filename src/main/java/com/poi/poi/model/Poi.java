package com.poi.poi.model;

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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    private String address;

    @OneToMany(mappedBy = "poi", cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    private List<PoiImage> images;

    public Poi() {}

    private Poi(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.description = builder.description;
        this.address = builder.address;
        this.images = builder.images;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }
    public List<PoiImage> getImages() {
        return images;
    }

    public Poi setImages(List<PoiImage> images) {
        this.images = images;
        return this;
    }

    public static class Builder {

        @JsonProperty("id")
        private Long id;

        @JsonProperty("name")
        private String name;

        @JsonProperty("description")
        private String description;

        @JsonProperty("address")
        private String address;

        @JsonProperty("images")
        private List<PoiImage> images;

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
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

        public Poi build() {
            return new Poi(this);
        }
    }

}

