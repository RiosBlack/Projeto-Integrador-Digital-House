package com.grupo7.renthotels.service;

import com.grupo7.renthotels.model.City;
import com.grupo7.renthotels.model.dto.CityDTO;
import com.grupo7.renthotels.repository.CityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
public class CityServiceTest {
    @Mock
    private CityRepository cityRepository;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private CityService cityService;

    @Test
    public void shouldSaveACity(){
        CityDTO cityDTO = new CityDTO(1234567L, "Manaus", "Brasil");
        String token = "token";

        when(tokenService.isValidToken(token)).thenReturn(true);
        when(cityRepository.save(any(City.class))).then(returnsFirstArg());

        var result = cityService.saveCity(cityDTO, token);
        assertThat(result.getStatusCode()).isEqualTo(OK);
    }

    @Test
    public void shouldGetAnEmptyListOfAllCities(){

        when(cityRepository.findAll()).thenReturn(List.of());

        var response = cityService.getAllCities();
        assertThat(response).isEmpty();
    }

    @Test
    public void shouldGetAListOfAllRegisteredCities(){
        City city1 = new City();
        city1.setIdCity(1L);
        city1.setCityDenomination("Dougcity");
        City city2 = new City();
        city2.setIdCity(2L);

        List<City> cities = new ArrayList<>();
        cities.add(city1);
        cities.add(city2);

        when(cityRepository.findAll()).thenReturn(cities);

        var response = cityService.getAllCities();
        assertThat(response)
                .hasSize(2)
                .first()
                .hasFieldOrPropertyWithValue("cityDenomination", "Dougcity");
    }

    @Test
    public void shouldGetACityWithValidId(){
        City city = City
                .builder()
                .cityDenomination("Cotia")
                .cityCountry("Brasil")
                .build();
        city.setIdCity(2L);

        when(cityRepository.findById(2L)).thenReturn(Optional.of(city));

        var result = cityService.getCityById(2L);
        assertThat(result.getStatusCode()).isEqualTo(OK);
    }

    @Test
    public void shouldNotGetACityWithAnInexistingId(){
        when(cityRepository.findById(2L)).thenReturn(Optional.empty());

        var result = cityService.getCityById(2L);
        assertThat(result.getStatusCode()).isEqualTo(NOT_FOUND);
    }

    @Test
    public void shouldGetACityByDenomination(){
        City city = City
                .builder()
                .cityDenomination("Belo Horizonte")
                .cityCountry("Brasil")
                .build();
        city.setIdCity(3L);

        when(cityRepository.getByCityDenomination("Belo Horizonte")).thenReturn(List.of(city));

        var result = cityService.findByCityDenomination("Belo Horizonte");
        assertThat(result.getStatusCode()).isEqualTo(OK);
    }

    @Test
    public void shouldNotGetACityWhenDoesntFindARegisteredDenomination(){
        when(cityRepository.getByCityDenomination("Salvador")).thenReturn(Collections.emptyList());

        var result = cityService.findByCityDenomination("Salvador");
        assertThat(result.getStatusCode()).isEqualTo(OK);
    }

    @Test
    public void shoulEditARegisteredCity(){
        City city = City
                .builder()
                .cityDenomination("São Paulo")
                .cityCountry("Brasil")
                .build();
        city.setIdCity(4L);

        when(cityRepository.findById(eq(4L))).thenReturn(
                Optional.of(city)
        );

        when(cityRepository.save(argThat(cityToSave -> cityToSave.getIdCity().equals(4L))))
                .then(returnsFirstArg());

        CityDTO newParams = new CityDTO(1234567L, "Palmas", "Brasil");

        var updatedCity = cityService.updateCity(4L, newParams);
        assertThat(updatedCity.getStatusCode()).isEqualTo(OK);
        assertThat(updatedCity.getBody())
                .hasFieldOrPropertyWithValue("cityDenomination", "Palmas");
    }

    @Test
    public void shouldNotEditACityWithAnInexistingId(){
        when(cityRepository.findById(eq(4L))).thenReturn(Optional.empty());

        var result = cityService.updateCity(4L, null);
        assertThat(result.getStatusCode()).isEqualTo(NOT_FOUND);
    }

    @Test
    public void shouldDeleteARegisteredCity(){
        City city = City
                .builder()
                .cityDenomination("Curitiba")
                .cityCountry("Brasil")
                .build();
        city.setIdCity(5L);

        when(cityRepository.findById(eq(5L))).thenReturn(Optional.of(city));

        var result = cityService.deleteCity(5L);
        assertThat(result.getStatusCode()).isEqualTo(NO_CONTENT);
    }

    @Test
    public void shouldNotDeleteACityWithInexistingId(){
        when(cityRepository.findById(eq(5L))).thenReturn(Optional.empty());

        var result = cityService.deleteCity(5L);
        assertThat(result.getStatusCode()).isEqualTo(NOT_FOUND);
    }

    @Test
    public void shouldGetACityBySku(){
        City city = City
                .builder()
                .cityDenomination("Cotia")
                .cityCountry("Brasil")
                .build();
        city.setCitySku(1234567L);

        when(cityRepository.findByCitySku(1234567L)).thenReturn(Optional.of(city));

        var result = cityService.getCityBySku(1234567L);
        assertThat(result.getStatusCode()).isEqualTo(OK);
    }

    @Test
    public void shouldNotGetACityWithAnInexistingSku(){
        when(cityRepository.findByCitySku(1234567L)).thenReturn(Optional.empty());

        var result = cityService.getCityBySku(1234567L);
        assertThat(result.getStatusCode()).isEqualTo(NOT_FOUND);
    }

    @Test
    public void shoulEditARegisteredCityBySku(){
        City city = City
                .builder()
                .cityDenomination("São Paulo")
                .cityCountry("Brasil")
                .build();
        city.setCitySku(1234567L);

        when(cityRepository.findByCitySku(eq(1234567L))).thenReturn(
                Optional.of(city)
        );

        when(cityRepository.save(argThat(cityToSave -> cityToSave.getCitySku().equals(1234567L))))
                .then(returnsFirstArg());

        CityDTO newParams = new CityDTO(1234567L, "Palmas", "Brasil");

        var updatedCity = cityService.updateCityBySku(1234567L, newParams);
        assertThat(updatedCity.getStatusCode()).isEqualTo(OK);
        assertThat(updatedCity.getBody())
                .hasFieldOrPropertyWithValue("cityDenomination", "Palmas");
    }

    @Test
    public void shouldNotEditACityWithAnInexistingSku(){
        when(cityRepository.findByCitySku(eq(1234567L))).thenReturn(Optional.empty());

        var result = cityService.updateCityBySku(1234567L, null);
        assertThat(result.getStatusCode()).isEqualTo(NOT_FOUND);
    }

    @Test
    public void shouldDeleteARegisteredCityBySku(){
        City city = City
                .builder()
                .cityDenomination("Curitiba")
                .cityCountry("Brasil")
                .build();
        city.setCitySku(1234567L);

        when(cityRepository.findByCitySku(1234567L)).thenReturn(Optional.of(city));

        var result = cityService.deleteCityBySku(1234567L);
        assertThat(result.getStatusCode()).isEqualTo(NO_CONTENT);
    }

    @Test
    public void shouldNotDeleteACityWithInexistingSku(){
        when(cityRepository.findByCitySku(1234567L)).thenReturn(Optional.empty());

        var result = cityService.deleteCityBySku(1234567L);
        assertThat(result.getStatusCode()).isEqualTo(NOT_FOUND);
    }
}