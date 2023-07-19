package com.grupo7.renthotels.controller;

import com.grupo7.renthotels.exception.NotFoundException;
import com.grupo7.renthotels.model.dto.CategoryDTO;
import com.grupo7.renthotels.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody @Valid CategoryDTO categoryDTO) throws NotFoundException {
        return categoryService.saveCategory(categoryDTO);
    }

    @GetMapping
    public List<CategoryDTO> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @RequestBody @Valid CategoryDTO categoryDTO) {
        return categoryService.updateCategory(id, categoryDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        return categoryService.deleteCategory(id);
    }

    @GetMapping(params = "sku")
    public ResponseEntity<CategoryDTO> getCategoryBySku (@RequestParam(value = "sku")  Long sku) {
        return categoryService.getCategoryBySku(sku);
    }

    @PutMapping(params = "sku")
    public ResponseEntity<CategoryDTO> updateCategorySku(@RequestParam(value = "sku") Long sku, @RequestBody @Valid CategoryDTO categoryDTO) {
        return categoryService.updateCategoryBySku(sku, categoryDTO);
    }

    @DeleteMapping(params = "sku")
    public ResponseEntity<Void> deleteCategoryBySku(@RequestParam(value = "sku") Long sku) {
        return categoryService.deleteCategoryBySku(sku);
    }

}
