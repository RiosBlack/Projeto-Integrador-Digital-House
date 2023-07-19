package com.grupo7.renthotels.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_product", nullable = false, unique = true)
    private Long idProduct;

    @Column(name = "product_sku", nullable = false, unique = true)
    private Long productSku;

    @Column(name = "product_title", length = 200, nullable = false)
    private String productTitle;

    @Column(name = "product_details", length = 500, nullable = false)
    private String productsDetails;

    @Column(name = "product_price", nullable = false)
    private Float productPrice;

    @Column(name = "product_address", length = 50)
    private String productAddress;

    @Column(name = "product_latitude", length = 15)
    private String productLatitude;

    @Column(name = "product_longitude", length = 15)
    private String productLongitude;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Image> images;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Policy> policies;

    @ManyToOne
    @JoinColumn(name = "id_category")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "id_city")
    private City city;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Booking> bookings;

    public void createSku() {
        long generated = (long) (Math.random() * 9999999L);
        this.productSku = (generated < 0 ? generated * -1 : generated);
    }

    @Builder
    public Product(String productTitle, String productsDetails, Float productPrice, String productAddress, List<Image> images, Category category, City city, List<Policy> policies, List<Booking> bookings) {
        this.productTitle = productTitle;
        this.productsDetails = productsDetails;
        this.productPrice = productPrice;
        this.productAddress = productAddress;
        this.images = images;
        this.category = category;
        this.city = city;
        this.policies = policies;
        this.bookings = bookings;
    }

}
