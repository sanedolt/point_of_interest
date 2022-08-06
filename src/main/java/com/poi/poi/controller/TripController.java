package com.poi.poi.controller;

import com.poi.poi.model.Trip;
import com.poi.poi.model.User;
import com.poi.poi.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
public class TripController {
    private TripService tripService;

    @Autowired
    public TripController(TripService tripService){
        this.tripService = tripService;
    }

    @PostMapping("/trip/add")
    public void addTrip(
            @RequestParam("name") String name,
            Principal principal) {
        Trip trip = new Trip.Builder().setName(name)
                        .setUser(new User.Builder().setUsername(principal.getName()).build())
                        .build();
        tripService.addTrip(trip);
    }

    @GetMapping("/trip/getByUser")
    public List<Trip> getTripByUser(Principal principal) {
        return tripService.getTripByUser(principal.getName());
    }

    @GetMapping("/trip/getById")
    public Optional<Trip> getTripById(@RequestParam(name = "trip_id") Long id) {
        return tripService.getTripById(id);
    }

    @DeleteMapping("/trip/delete")
    public void deleteTrip(
            @RequestParam(name = "trip_id") Long tripId,
            Principal principal) {
        tripService.deleteTrip(tripId, principal.getName());
    }
}
