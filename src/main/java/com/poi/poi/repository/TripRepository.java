package com.poi.poi.repository;

import com.poi.poi.model.Trip;
import com.poi.poi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findByUser(User user);

    Trip findByTripIdAndUser(Long id, User user);
}
