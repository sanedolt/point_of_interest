package com.poi.poi.controller;

import com.poi.poi.model.Poi;
import com.poi.poi.service.PoiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
public class PoiController {
    private PoiService poiService;

    @Autowired
    public PoiController(PoiService poiService) {
        this.poiService = poiService;
    }

    @PostMapping("/poi")
    public void addStay(
            @RequestParam("name") String name,
            @RequestParam("address") String address,
            @RequestParam("description") String description,
            @RequestParam("images") MultipartFile[] images) {

        Poi poi = new Poi.Builder().setName(name)
                .setAddress(address)
                .setDescription(description)
                .build();
        poiService.add(poi, images);
    }

    @GetMapping(value = "/poi/id")
    public Optional<Poi> getPoi(@RequestParam(name = "poi_id") Long stayId) {
        return poiService.findById(stayId);
    }

    @DeleteMapping("/poi")
    public void deletePoi(@RequestParam(name = "poi_id") Long stayId) {
        poiService.delete(stayId);
    }
}






