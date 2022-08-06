package com.poi.poi.service;

import com.poi.poi.exception.PoiNotExistException;
import com.poi.poi.exception.TripNotExistException;
import com.poi.poi.model.Poi;
import com.poi.poi.model.Trip;
import com.poi.poi.model.User;
import com.poi.poi.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TripService {
    private TripRepository tripRepository;

    @Autowired
    public TripService(TripRepository tripRepository){
        this.tripRepository = tripRepository;
    }

    public void addTrip(Trip trip){
        tripRepository.save(trip);
    }

    public List<Trip> getTripByUser(String username){
        return tripRepository.findByUser(new User.Builder().setUsername(username).build());
    }

    public Optional<Trip> getTripById(Long id){
        return tripRepository.findById(id);
    }

    public void deleteTrip(Long tripId, String username) throws PoiNotExistException {
        Trip trip = tripRepository.findByTripIdAndUser(tripId, new User.Builder().setUsername(username).build());
        if (trip == null) {
            throw new TripNotExistException("Trip doesn't exist");
        }
        tripRepository.deleteById(tripId);
    }
}
