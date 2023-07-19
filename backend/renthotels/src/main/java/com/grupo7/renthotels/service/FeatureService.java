package com.grupo7.renthotels.service;

import com.grupo7.renthotels.exception.NotFoundException;
import com.grupo7.renthotels.model.Feature;
import com.grupo7.renthotels.repository.FeatureRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FeatureService {
    @Autowired
    private FeatureRepository repository;

    public Feature getAFeature(Long sku) throws NotFoundException {
        return repository.findByFeatureSku(sku).orElseThrow(() -> new NotFoundException("Nao foi encontrada categoria com o sku "+ sku + " ."));
    }


    public List<Feature> premiumFeatures() throws NotFoundException {
        List<Feature> features = new ArrayList<>();
        features.add(getAFeature(7694639L));
        features.add(getAFeature(3578694L));
        features.add(getAFeature(1597239L));
        features.add(getAFeature(8796841L));
        features.add(getAFeature(8745741L));
        return features;
    }

    public List<Feature> masterFeatures() throws NotFoundException {
        List<Feature> features = new ArrayList<>();
        features.add(getAFeature(7694639L));
        features.add(getAFeature(3578694L));
        features.add(getAFeature(1597239L));
        features.add(getAFeature(8796841L));
        features.add(getAFeature(8745741L));
        features.add(getAFeature(9856327L));
        return features;
    }

    public List<Feature> deluxFeatures() throws NotFoundException {
       List<Feature> features = new ArrayList<>();
        features.add(getAFeature(7694639L));
        features.add(getAFeature(3578694L));
        features.add(getAFeature(1597239L));
        features.add(getAFeature(8796841L));
        features.add(getAFeature(8745741L));
        features.add(getAFeature(9856327L));
        features.add(getAFeature(1678894L));
       return features;
    }

    public List<Feature> eurostarFeatures(){
        List<Feature> features = new ArrayList<>();
        features.addAll(repository.findAll());
        return features;
    }
}
