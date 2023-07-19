package com.grupo7.renthotels.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name="cities")
@Getter
@Setter
@NoArgsConstructor
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_city", nullable = false, unique = true)
    private Long idCity;

    @Column(name = "city_sku", nullable = false, unique = true)
    private Long citySku;

    @Column(name = "city_denomination", length = 100, nullable = false)
    private String cityDenomination;

    @Column(name="city_country", length = 90, nullable = false)
    private String cityCountry;

    @OneToMany(mappedBy = "city")
    private List<Product> products;

    public void createCitySku(){
        long generated = (long) (Math.random() * 9999999L);
        this.citySku = (generated < 0 ? generated * -1 : generated);
    }

    @Builder
    public City(String cityDenomination, String cityCountry, List<Product> products) {
        this.cityDenomination = cityDenomination;
        this.cityCountry = cityCountry;
        this.products = products;
    }
}
