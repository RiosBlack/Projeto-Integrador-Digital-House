package com.grupo7.renthotels.model.dto;

import com.grupo7.renthotels.model.Category;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    private Long sku;
    private String kind;
    private Integer qualification;
    private String details;
    private String imageUrl;
    private List<FeatureDTO> features;


    public static CategoryDTO from(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setSku(category.getSku());
        categoryDTO.setKind(category.getKind());
        categoryDTO.setQualification(category.getQualification());
        categoryDTO.setDetails(category.getDetails());
        categoryDTO.setImageUrl(category.getImageUrl());
        categoryDTO.setFeatures(category.getFeatures().stream().map(FeatureDTO::from).collect(Collectors.toList()));
        return categoryDTO;
    }

    public Category toEntity() {
        Category category = new Category();
        category.setSku(this.sku);
        category.setKind(this.kind);
        category.setQualification(this.qualification);
        category.setDetails(this.details);
        category.setImageUrl(this.imageUrl);
        category.setFeatures(this.features.stream().map(FeatureDTO::toEntity).collect(Collectors.toList()));
        return category;
    }

}
