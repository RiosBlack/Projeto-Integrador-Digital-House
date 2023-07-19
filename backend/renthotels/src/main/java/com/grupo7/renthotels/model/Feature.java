package com.grupo7.renthotels.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "features")
@Getter
@Setter
@NoArgsConstructor
public class Feature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_feature", nullable = false, unique = true)
    private Long idFeature;

    @Column(name = "feature_sku", nullable = false, unique = true)
    private Long featureSku;

    @Column(name = "feature_title", length = 50,nullable = false)
    private String featureTitle;

    @Column(name = "feature_icon_url", length = 200)
    private String featureIconUrl;

    @ManyToMany(mappedBy = "features")
    private List<Category> categories;

    @Builder
    public Feature(String featureTitle, String featureIconUrl) {
        this.featureTitle = featureTitle;
        this.featureIconUrl = featureIconUrl;
    }
}
