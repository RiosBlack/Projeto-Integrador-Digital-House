package com.grupo7.renthotels.controller;

import com.grupo7.renthotels.model.dto.CityDTO;
import com.grupo7.renthotels.service.CityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/cities")
public class CityController {

    @Autowired
    private CityService cityService;

    @PostMapping
    public ResponseEntity<CityDTO> createCity(@RequestBody @Valid CityDTO cityDTO, @RequestHeader("Authorization") String token) {
        return cityService.saveCity(cityDTO, token);
    }

    @GetMapping
    public List<CityDTO> getAllCities() {
        return cityService.getAllCities();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CityDTO> getCityById(@PathVariable Long id) {
        return cityService.getCityById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CityDTO> updateCity(@PathVariable Long id,@RequestBody @Valid CityDTO cityDTO) {
        return cityService.updateCity(id, cityDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable Long id) {
        return cityService.deleteCity(id);
    }

    @GetMapping(params = "cityDenomination")
    public ResponseEntity<List<CityDTO>> getByCityDenomination(@RequestParam(value="cityDenomination") String cityDenomination) {
        return cityService.findByCityDenomination(cityDenomination);
    }

    @GetMapping(params = "citySku")
    public ResponseEntity<CityDTO> getCityBySku(@RequestParam(value = "citySku") Long sku) {
        return cityService.getCityBySku(sku);
    }

    @PutMapping(params = "citySku")
    ResponseEntity<CityDTO> updateCityBySku(@RequestParam(value = "citySku")  Long sku, @RequestBody @Valid CityDTO cityDTO) {
        return cityService.updateCityBySku(sku, cityDTO);
    }

    @DeleteMapping(params = "citySku")
    ResponseEntity<Void> deleteCityBySku(@RequestParam(value = "citySku")  Long sku) {
        return cityService.deleteCityBySku(sku);
    }
}
