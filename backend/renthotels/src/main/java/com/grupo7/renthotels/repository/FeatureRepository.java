package com.grupo7.renthotels.repository;

import com.grupo7.renthotels.model.Feature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FeatureRepository extends JpaRepository<Feature, Long> {
    Optional<Feature> findByFeatureSku(Long featureSku);
}
