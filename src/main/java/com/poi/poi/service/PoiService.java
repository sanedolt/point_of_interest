package com.poi.poi.service;
import com.poi.poi.exception.PoiNotExistException;
import com.poi.poi.model.Poi;
import com.poi.poi.repository.PoiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PoiService {
    private PoiRepository poiRepository;

    @Autowired
    public PoiService(PoiRepository poiRepository) {
        this.poiRepository = poiRepository;
    }

    public Optional<Poi> findById(Long id)  {
        return poiRepository.findById(id);
    }

    public void add(Poi poi) {
        poiRepository.save(poi);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void delete(Long id) throws PoiNotExistException {
        Optional<Poi> poi = poiRepository.findById(id);
        if (poi == null) {
            throw new PoiNotExistException("Point of intererst doesn't exist");
        }
        poiRepository.deleteById(id);
    }
}







