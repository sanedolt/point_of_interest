package com.poi.poi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "poi_image")
public class PoiImage implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String url;

    @ManyToOne
    @JoinColumn(name = "stay_id")
    @JsonIgnore
    private Poi poi;

    public PoiImage() {}

    public PoiImage(String url, Poi poi) {
        this.url = url;
        this.poi = poi;
    }

    public String getUrl() {
        return url;
    }

    public PoiImage setUrl(String url) {
        this.url = url;
        return this;
    }

    public Poi getPoi() {
        return poi;
    }

    public PoiImage setPoi(Poi poi) {
        this.poi = poi;
        return this;
    }
}

