package com.grupo7.renthotels.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.grupo7.renthotels.exception.NotFoundException;
import com.grupo7.renthotels.model.Feature;
import com.grupo7.renthotels.model.dto.CategoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.grupo7.renthotels.model.Category;
import com.grupo7.renthotels.repository.CategoryRepository;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    FeatureService featureService;

    public ResponseEntity<CategoryDTO> saveCategory(CategoryDTO categoryDTO) throws NotFoundException {
        List<Feature> features;
        Category category = categoryDTO.toEntity();

        switch (category.getKind()){
            case "Eurostar": features = featureService.eurostarFeatures(); category.setFeatures(features); break;
            case "Master": features = featureService.masterFeatures(); category.setFeatures(features); break;
            case "Premium": features = featureService.premiumFeatures(); category.setFeatures(features); break;
            case "Delux": features = featureService.deluxFeatures(); category.setFeatures(features); break;
            default: ResponseEntity.badRequest().build();
        }

        category.createSku();
        categoryRepository.save(category);
        return ResponseEntity.ok(CategoryDTO.from(category));
    }

    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryDTO::from)
                .collect(Collectors.toList());
    }

    public ResponseEntity<CategoryDTO> getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(CategoryDTO::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<CategoryDTO> updateCategory(Long id, CategoryDTO categoryDTO) {
        Optional<Category> existingCategory = categoryRepository.findById(id);
        if (existingCategory.isPresent()) {
            Category category = categoryDTO.toEntity();
            category.setIdCategory(existingCategory.get().getIdCategory());
            Category updatedCategory = categoryRepository.save(category);
            return ResponseEntity.ok(CategoryDTO.from(updatedCategory));
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    public ResponseEntity<Void> deleteCategory(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isPresent()) {
            categoryRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<CategoryDTO> getCategoryBySku(Long sku){
        return categoryRepository.findBySku(sku)
                .map(CategoryDTO::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<CategoryDTO> updateCategoryBySku(Long sku, CategoryDTO categoryDTO) {
        Optional<Category> existingCategory = categoryRepository.findBySku(sku);
        if (existingCategory.isPresent()) {
            Category category = categoryDTO.toEntity();
            category.setIdCategory(existingCategory.get().getIdCategory());
            category.setSku(existingCategory.get().getSku());
            category.setFeatures(existingCategory.get().getFeatures());
            Category updatedCategory = categoryRepository.save(category);
            return ResponseEntity.ok(CategoryDTO.from(updatedCategory));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Void> deleteCategoryBySku(Long sku) {
        Optional<Category> category = categoryRepository.findBySku(sku);
        if (category.isPresent()) {
            categoryRepository.deleteById(category.get().getIdCategory());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
