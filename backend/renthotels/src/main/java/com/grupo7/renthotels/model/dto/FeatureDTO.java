package com.grupo7.renthotels.model.dto;

import com.grupo7.renthotels.model.Feature;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class FeatureDTO {
    private Long featureSku;
    private String featureTitle;
    private String featureIconUrl;


    public static FeatureDTO from(Feature feature) {
        return new FeatureDTO(
                feature.getFeatureSku(),
                feature.getFeatureTitle(),
                feature.getFeatureIconUrl()
        );
    }

    public Feature toEntity() {
        Feature feature = new Feature();
        feature.setFeatureSku(this.featureSku);
        feature.setFeatureTitle(this.featureTitle);
        feature.setFeatureIconUrl(this.featureIconUrl);
        return feature;
    }
}