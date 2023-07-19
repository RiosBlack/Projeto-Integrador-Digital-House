package com.grupo7.renthotels.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.grupo7.renthotels.model.City;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties
public class CityDTO {
    private  Long citySku;
    private String cityDenomination;
    private String cityCountry;

    public static CityDTO from (City city) {
        return new CityDTO(
                city.getCitySku(),
                city.getCityDenomination(),
                city.getCityCountry()
        );
    }

    public City toEntity(){
        City city = new City();
        city.setCitySku(this.citySku);
        city.setCityDenomination(this.cityDenomination);
        city.setCityCountry(this.cityCountry);
        return city;
    }
}
