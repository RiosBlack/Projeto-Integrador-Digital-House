package com.grupo7.renthotels.repository;

import com.grupo7.renthotels.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    List<City> getByCityDenomination(@Param("cityDenomination") String cityDenomination);
    Optional<City> findByCitySku(Long citySku);
}
