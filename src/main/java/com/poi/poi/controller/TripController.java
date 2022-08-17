package com.poi.poi.controller;

import com.poi.poi.exception.InvalidTripDateException;
import com.poi.poi.model.Poi;
import com.poi.poi.model.Trip;
import com.poi.poi.model.User;
import com.poi.poi.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
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
            @RequestParam("checkin") String start,
            @RequestParam("checkout") String end,
            Principal principal) {
        LocalDate checkinDate = LocalDate.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate checkoutDate = LocalDate.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate maxDate = checkinDate.plusDays(15);
        if (checkinDate.equals(checkoutDate) || checkinDate.isAfter(checkoutDate) || checkinDate.isBefore(LocalDate.now()) || checkoutDate.isAfter(maxDate)) {
            throw new InvalidTripDateException("Invalid date for trip");
        }

        Trip trip = new Trip.Builder().setName(name)
                        .setUser(new User.Builder().setUsername(principal.getName()).build())
                        .setCheckinDate(checkinDate)
                        .setCheckoutDate(checkoutDate)
                        .build();
        tripService.addTrip(trip);
    }

    @GetMapping("/trip/getUserNewest")
    public long getNewestTripIDByUser (Principal principal) {
        return tripService.getNewestTripIDByUser(principal.getName());
    }

    @GetMapping("/trip/getByUser")
    public List<Trip> getTripByUser(Principal principal) {
        return tripService.getTripByUser(principal.getName());
    }

    @GetMapping("/trip/getUpcoming")
    public List<Trip> getUpcomingTripByUser(Principal principal) {
        return tripService.getUpcomingTripByUser(principal.getName());
    }

    @GetMapping("/trip/getPast")
    public List<Trip> getPassedTripByUser(Principal principal) {
        return tripService.getPastTripByUser(principal.getName());
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

    @PostMapping("/trip/save")
    public void saveTrip(
            @RequestParam("trip_id") Long tripId,
            @RequestParam("plan") String plan,
            Principal principal){
        tripService.saveTrip(tripId, plan, principal.getName());
    }

    @PostMapping("/trip/addPoiToTrip")
    public void addPoiToTrip(
            @RequestParam("poi_id") Long poiId,
            @RequestParam("trip_id") Long tripId,
            Principal principal){
        tripService.addPoiToTrip(poiId, tripId, principal.getName());
    }

    @GetMapping("/trip/getPlanFromTrip")
    public List<List<Poi>> getPlanFromTrip(
            @RequestParam("trip_id") Long tripId,
            Principal principal){
        return tripService.getPlanFromTrip(tripId, principal.getName());
    }

    @GetMapping("/trip/getAllPoiFromTrip")
    public List<Poi> getAllPoiToTrip(
            @RequestParam("trip_id") Long tripId,
            Principal principal){
        return tripService.getAllPoiFromTrip(tripId, principal.getName());
    }

    @DeleteMapping("/trip/deletePoiFromTrip")
    public void deletePoiFromTrip(
            @RequestParam(name = "trip_id") Long tripId,
            @RequestParam("poi_id") Long poiId,
            Principal principal) {
        tripService.deletePoiFromTrip(poiId, tripId, principal.getName());
    }
}