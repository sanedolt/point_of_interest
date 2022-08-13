package com.poi.poi.service;

import com.poi.poi.exception.PoiAlreadyExistInTripException;
import com.poi.poi.exception.PoiNotExistException;
import com.poi.poi.exception.TripNotExistException;
import com.poi.poi.model.Poi;
import com.poi.poi.model.Trip;
import com.poi.poi.model.User;
import com.poi.poi.repository.PoiRepository;
import com.poi.poi.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TripService {
    private TripRepository tripRepository;
    private PoiRepository poiRepository;

    @Autowired
    public TripService(TripRepository tripRepository, PoiRepository poiRepository){
        this.tripRepository = tripRepository;
        this.poiRepository = poiRepository;
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

    public void deleteTrip(Long tripId, String username) throws TripNotExistException {
        Trip trip = tripRepository.findByTripIdAndUser(tripId, new User.Builder().setUsername(username).build());
        if (trip == null) {
            throw new TripNotExistException("Trip doesn't exist");
        }
        tripRepository.deleteById(tripId);
    }

    public void addPoiToTrip(Long poiId, Long tripId, String username) throws TripNotExistException, PoiNotExistException, PoiAlreadyExistInTripException {
        Trip trip = tripRepository.findByTripIdAndUser(tripId, new User.Builder().setUsername(username).build());
        if(trip == null){
            throw new TripNotExistException("Trip doesn't exist");
        }
        Optional<Poi> poi = poiRepository.findById(poiId);
        if(!poi.isPresent()){
            throw new PoiNotExistException("Point of interest doesn't exist");
        }
//        boolean test = trip.addPoiToTrip(poi.get());
        Boolean test = trip.getPoiSet().add(poi.get());
        if(test == false){
            throw new PoiAlreadyExistInTripException("The selected point of interest is already in current trip.");
        }
        tripRepository.save(trip);
    }

    public List<Poi> getAllPoiFromTrip(Long tripId, String username) throws TripNotExistException{
        Trip trip = tripRepository.findByTripIdAndUser(tripId, new User.Builder().setUsername(username).build());
        if(trip == null){
            throw new TripNotExistException("Trip doesn't exist");
        }
        List<Poi> l = new ArrayList<>(trip.getPoiSet());
        return l;
    }

    public void deletePoiFromTrip(Long poiId, Long tripId, String username) throws TripNotExistException, PoiNotExistException{
        Trip trip = tripRepository.findByTripIdAndUser(tripId, new User.Builder().setUsername(username).build());
        if(trip == null){
            throw new TripNotExistException("Trip doesn't exist");
        }
        Optional<Poi> poi = poiRepository.findById(poiId);
        if(!poi.isPresent()){
            throw new PoiNotExistException("Point of interest doesn't exist in our system");
        }
        Set<Poi> set = trip.getPoiSet();
        if(!set.contains(poi.get())){
            throw new PoiNotExistException("Current trip does not contain this Point of Interest");
        }
        set.remove(poi.get());
        tripRepository.save(trip);
    }
}