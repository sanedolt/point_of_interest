package com.poi.poi.service;
import com.poi.poi.exception.PoiNotExistException;
import com.poi.poi.model.Poi;
import com.poi.poi.model.PoiImage;
import com.poi.poi.repository.PoiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PoiService {
    private PoiRepository poiRepository;
    private ImageStorageService imageStorageService;
    @Autowired
    public PoiService(PoiRepository poiRepository, ImageStorageService imageStorageService) {
        this.poiRepository = poiRepository;
        this.imageStorageService = imageStorageService;
    }

    public Optional<Poi> findById(Long id)  {
        return poiRepository.findById(id);
    }

    public void add(Poi poi, MultipartFile[] images) {
        List<String> mediaLinks = Arrays.stream(images).parallel().map(image -> imageStorageService.save(image)).collect(Collectors.toList());
        List<PoiImage> poiImages = new ArrayList<>();
        for (String mediaLink : mediaLinks) {
            poiImages.add(new PoiImage(mediaLink, poi));
        }
        poi.setImages(poiImages);

        poiRepository.save(poi);
    }

    public void addImage(Long id, MultipartFile[] images) {
        Optional<Poi> pois = poiRepository.findById(id);
        if (!pois.isPresent()) {
            throw new PoiNotExistException("Point of interest doesn't exist");
        }
        Poi poi = pois.get();
        List<String> mediaLinks = Arrays.stream(images).parallel().map(image -> imageStorageService.save(image)).collect(Collectors.toList());
        List<PoiImage> poiImages = new ArrayList<>();
        for (String mediaLink : mediaLinks) {
            poiImages.add(new PoiImage(mediaLink, poi));
        }
        poi.setImages(poiImages);

        poiRepository.save(poi);
    }

    public List<Poi> findByNameContaining(String name){
        return poiRepository.findByNameContaining(name);
    }

    public List<Poi> findAll(){
        return poiRepository.findAll();
    }

    public List<Poi> findTop6ByTimeTaken(){
        return poiRepository.findTop6ByOrderByTimeTaken();
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void delete(Long id) throws PoiNotExistException {
        Optional<Poi> poi = poiRepository.findById(id);
        if (!poi.isPresent()) {
            throw new PoiNotExistException("Point of interest doesn't exist");
        }
        poiRepository.deleteById(id);
    }
}







