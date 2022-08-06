package com.poi.poi.repository;

import com.poi.poi.model.Poi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PoiRepository extends JpaRepository<Poi, Long> {
    List<Poi> findByNameContaining(String name);

    List<Poi> findTop6ByOrderByTimeTaken();
}
