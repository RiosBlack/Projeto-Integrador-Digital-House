package com.grupo7.renthotels.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_category", nullable = false, unique = true)
    private Long idCategory;

    @Column(name = "category_sku", nullable = false, unique = true)
    private Long sku;

    @Column(name = "kind", length = 13, nullable = false)
    private String kind;
    @Column(name = "qualification", nullable = false)
    private Integer qualification;
    @Column(name = "details", length = 250, nullable = false)
    private String details;
    @Column(name = "image_url", length = 200)
    private String imageUrl;

    @ManyToMany
    @JoinTable(name = "categories_features",
            joinColumns = @JoinColumn(name = "id_category"),
            inverseJoinColumns = @JoinColumn(name = "id_feature"))
    private List<Feature> features;

    @OneToMany(mappedBy = "category")
    private List<Product> products = new ArrayList<>();


    public void createSku () {
        long generated = (long) (Math.random() * 9999999L);
        this.sku = (generated < 0 ? generated * -1 : generated);
    }


    @Builder
    public Category(String kind, Integer qualification, String details, String imageUrl, List<Product> products, List<Feature> features) {
        this.kind = kind;
        this.qualification = qualification;
        this.imageUrl = imageUrl;
        this.details = details;
        this.products = products;
        this.features = features;
    }
}
