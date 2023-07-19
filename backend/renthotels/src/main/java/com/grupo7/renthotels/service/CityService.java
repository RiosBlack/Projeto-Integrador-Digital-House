package com.grupo7.renthotels.service;

import com.grupo7.renthotels.model.City;
import com.grupo7.renthotels.model.dto.CityDTO;
import com.grupo7.renthotels.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CityService {

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private TokenService tokenService;

    public ResponseEntity<CityDTO> saveCity(CityDTO cityDTO, String token) {
        if (!tokenService.isValidToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        City city = cityDTO.toEntity();
        city.createCitySku();
        cityRepository.save(city);
        return ResponseEntity.ok(CityDTO.from(city));
    }

    public List<CityDTO> getAllCities(){
        return cityRepository.findAll()
                .stream()
                .map(CityDTO::from)
                .collect(Collectors.toList());
    }

    public ResponseEntity<CityDTO> getCityById(Long id) {
        return cityRepository.findById(id)
                .map(CityDTO::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    public ResponseEntity<CityDTO> updateCity(Long id, CityDTO cityDTO) {
        Optional<City> existingCity = cityRepository.findById(id);
        if (existingCity.isPresent()) {
            City city = cityDTO.toEntity();
            city.setIdCity(existingCity.get().getIdCity());
            City updateCity = cityRepository.save(city);
            return ResponseEntity.ok(CityDTO.from(updateCity));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Void> deleteCity(Long id) {
        Optional<City> city = cityRepository.findById(id);
        if ( city.isPresent()) {
            cityRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<List<CityDTO>> findByCityDenomination(String cityDenomination) {
    List<City> cities = cityRepository.getByCityDenomination(cityDenomination);
    List<CityDTO> cityDTOs = cities.stream()
            .map(CityDTO::from)
            .collect(Collectors.toList());
    return ResponseEntity.ok(cityDTOs);
    }

    public ResponseEntity<CityDTO> getCityBySku(Long sku) {
        return cityRepository.findByCitySku(sku)
                .map(CityDTO::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<CityDTO> updateCityBySku(Long sku, CityDTO cityDTO){
        Optional<City> existingCity = cityRepository.findByCitySku(sku);
        if(existingCity.isPresent()){
            City city = cityDTO.toEntity();
            city.setIdCity(existingCity.get().getIdCity());
            city.setCitySku(existingCity.get().getCitySku());
            City updatedCity = cityRepository.save(city);
            return ResponseEntity.ok(CityDTO.from(updatedCity));
        }  else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Void> deleteCityBySku(Long sku){
        Optional<City> city = cityRepository.findByCitySku(sku);
        if (city.isPresent()){
            cityRepository.deleteById(city.get().getIdCity());
            return ResponseEntity.noContent().build();
        }else {
            return ResponseEntity.notFound().build();
        }
    }
}
