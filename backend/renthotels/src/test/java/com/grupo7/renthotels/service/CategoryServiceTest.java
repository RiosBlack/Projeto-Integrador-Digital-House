package com.grupo7.renthotels.service;

import com.grupo7.renthotels.exception.NotFoundException;
import com.grupo7.renthotels.model.Category;
import com.grupo7.renthotels.model.Feature;
import com.grupo7.renthotels.model.dto.CategoryDTO;
import com.grupo7.renthotels.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
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
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private FeatureService featureService;

    @Test
    public void shouldSaveACategory() throws NotFoundException {
        CategoryDTO categoryDTO = new CategoryDTO(1234567L, "Premium", 4, "suite master", "imageurl", List.of());

        when(featureService.premiumFeatures()).thenReturn(List.of(new Feature("Piscina", "url")));
        when(categoryRepository.save(any(Category.class))).then(returnsFirstArg());

        var result = categoryService.saveCategory(categoryDTO);

        assertThat(result.getBody().getFeatures().get(0).getFeatureTitle().equals("Piscina"));
        assertThat(result.getStatusCode()).isEqualTo(OK);
    }

    @Test
    public void shouldGetAnEmptyListOfAllCategories(){
        when(categoryRepository.findAll()).thenReturn(List.of());
        var response = categoryService.getAllCategories();

        assertThat(response).isEmpty();
    }

    @Test
    public void shouldGetAListOfAllRegisteredCategories(){
        Category category1 = new Category();
        category1.setIdCategory(1L);
        category1.setKind("Master");
        category1.setFeatures(List.of());
        Category category2 = new Category();
        category2.setIdCategory(2L);
        category2.setFeatures(List.of());

        List<Category> categories = new ArrayList<>();
        categories.add(category1);
        categories.add(category2);

        when(categoryRepository.findAll()).thenReturn(categories);

        var result = categoryService.getAllCategories();
        assertThat(result)
                .hasSize(2)
                .first()
                .hasFieldOrPropertyWithValue("kind", "Master");
    }

    @Test
    public void shouldGetACategoryWithValidId(){
        Category category = Category
                .builder()
                .kind("Master")
                .qualification(3)
                .details("Quarto com cama king size")
                .imageUrl("image")
                .features(List.of())
                .build();
        category.setIdCategory(1L);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        var searchCategory = categoryService.getCategoryById(1L);
        assertThat(searchCategory.getStatusCode()).isEqualTo(OK);
    }

    @Test
    public void shouldNotGetACategoryWithInexistingId(){
        when(categoryRepository.findById(3L)).thenReturn(Optional.empty());

        var response = categoryService.getCategoryById(3L);
        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
    }

    @Test
    public void shouldNotEditACategoryWhenItDoesntFindTheId() {
        when(categoryRepository.findById(2L)).thenReturn(Optional.empty());

        var result = categoryService.updateCategory(2L, null);
        assertThat(result.getStatusCode()).isEqualTo(NOT_FOUND);
    }

    @Test
    public void shouldEditAnRegisteredCategory() {
        Category category = Category
                .builder()
                .kind("Casa")
                .qualification(5)
                .imageUrl("google")
                .build();
        category.setIdCategory(2L);
        category.setSku(1234567L);


        when(categoryRepository.findById(eq(2L))).thenReturn(
                Optional.of(category)
        );

        when(categoryRepository.save(argThat(categoryToSave -> categoryToSave.getIdCategory().equals(2L))))
                .then(returnsFirstArg());

        CategoryDTO newParams = new CategoryDTO(1234567L ,"Apartamento", 2, "Apartamento com varanda", "url", List.of());

        var updatedCategory = categoryService.updateCategory(2L, newParams);
        assertThat(updatedCategory.getStatusCode()).isEqualTo(OK);
        assertThat(updatedCategory.getBody())
                .hasFieldOrPropertyWithValue("sku", 1234567L)
                .hasFieldOrPropertyWithValue("kind", "Apartamento")
                .hasFieldOrPropertyWithValue("qualification", 2)
                .hasFieldOrPropertyWithValue("details", "Apartamento com varanda")
                .hasFieldOrPropertyWithValue("imageUrl", "url");
    }

    @Test
    public void shouldDeleteARegisteredCategory(){
        Category category = Category
                .builder()
                .kind("Standard")
                .qualification(1)
                .details("Quarto com cama dupla")
                .imageUrl("imageurl")
                .build();
        category.setIdCategory(9L);
        category.setFeatures(List.of());

        when(categoryRepository.findById(eq(9L))).thenReturn(
                Optional.of(category)
        );

        var result = categoryService.deleteCategory(9L);
        assertThat(result.getStatusCode()).isEqualTo(NO_CONTENT);
    }

    @Test
    public void shouldNotDeleteACategoryWithInexistingId(){
        when(categoryRepository.findById(9L)).thenReturn(Optional.empty());

        var result = categoryService.deleteCategory(9L);
        assertThat(result.getStatusCode()).isEqualTo(NOT_FOUND);
    }

    @Test
    public void shouldGetACategoryBySku(){
        Category category = new Category();
        category.setSku(1234567L);
        category.setFeatures(List.of());

        when(categoryRepository.findBySku(1234567L)).thenReturn(Optional.of(category));

        var searchCategory = categoryService.getCategoryBySku(1234567L);
        assertThat(searchCategory.getStatusCode()).isEqualTo(OK);
    }

    @Test
    public void shouldNotGetACategoryWithInexistingSku(){
        when(categoryRepository.findBySku(1234567L)).thenReturn(Optional.empty());

        var response = categoryService.getCategoryBySku(1234567L);
        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
    }

    @Test
    public void shouldNotEditACategoryWhenItDoesntFindTheSku() {
        when(categoryRepository.findBySku(1234567L)).thenReturn(Optional.empty());

        var result = categoryService.updateCategoryBySku(1234567L, null);
        assertThat(result.getStatusCode()).isEqualTo(NOT_FOUND);
    }

    @Test
    public void shouldEditAnRegisteredCategoryBySku() {
        Category category = Category
                .builder()
                .kind("Casa")
                .qualification(5)
                .imageUrl("google")
                .features(List.of())
                .build();
        category.setSku(1234567L);

        when(categoryRepository.findBySku(eq(1234567L))).thenReturn(
                Optional.of(category)
        );

        when(categoryRepository.save(argThat(categoryToSave -> categoryToSave.getSku().equals(1234567L))))
                .then(returnsFirstArg());

        CategoryDTO newParams = new CategoryDTO(category.getSku() ,"Apartamento", 2, "Apartamento com varanda", "url", List.of());

        var updatedCategory = categoryService.updateCategoryBySku(1234567L, newParams);
        assertThat(updatedCategory.getStatusCode()).isEqualTo(OK);
        assertThat(updatedCategory.getStatusCode()).isEqualTo(OK);
        assertThat(updatedCategory.getBody())
                .hasFieldOrPropertyWithValue("sku", 1234567L)
                .hasFieldOrPropertyWithValue("kind", "Apartamento")
                .hasFieldOrPropertyWithValue("qualification", 2)
                .hasFieldOrPropertyWithValue("details", "Apartamento com varanda")
                .hasFieldOrPropertyWithValue("imageUrl", "url");
    }

    @Test
    public void shouldDeleteARegisteredCategoryBySku(){
        Category category = Category
                .builder()
                .kind("Standard")
                .qualification(1)
                .details("Quarto com cama dupla")
                .imageUrl("imageurl")
                .build();
        category.setSku(1234567L);

        when(categoryRepository.findBySku(eq(1234567L))).thenReturn(
                Optional.of(category)
        );

        var result = categoryService.deleteCategoryBySku(1234567L);
        assertThat(result.getStatusCode()).isEqualTo(NO_CONTENT);
    }

    @Test
    public void shouldNotDeleteACategoryWithInexistingSku(){
        when(categoryRepository.findBySku(1234567L)).thenReturn(Optional.empty());

        var result = categoryService.deleteCategoryBySku(1234567L);
        assertThat(result.getStatusCode()).isEqualTo(NOT_FOUND);
    }
}
