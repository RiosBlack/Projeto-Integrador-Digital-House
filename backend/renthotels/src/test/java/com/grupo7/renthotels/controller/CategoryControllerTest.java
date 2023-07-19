package com.grupo7.renthotels.controller;

import com.grupo7.renthotels.model.Category;
import com.grupo7.renthotels.model.dto.CategoryDTO;
import com.grupo7.renthotels.security.SpringSecurityWebAuxTestConfig;
import com.grupo7.renthotels.service.CategoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(CategoryController.class)
@Import(CategoryController.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringSecurityWebAuxTestConfig.class)
public class CategoryControllerTest {


    @MockBean
    private CategoryService categoryService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails("user@test.com")
    public void shouldGetAEmptyListOfCategories() throws Exception {
        when(categoryService.getAllCategories()).thenReturn(Collections.emptyList());

        this.mockMvc
                .perform(
                        get("/api/categories")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldGetAListOfAllRegisteredCategories() throws Exception {
        CategoryDTO categoryDTO = new CategoryDTO();

        when(categoryService.getAllCategories()).thenReturn(List.of(categoryDTO));

        this.mockMvc
                .perform(
                        get("/api/categories")
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldMakeAGetCategoryWithValidId() throws Exception {
        CategoryDTO category = new CategoryDTO();

        when(categoryService.getCategoryById(eq(5L))).thenReturn(ResponseEntity.ok(category));

        this.mockMvc
                .perform(
                        get("/api/categories/{id}", 5L)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldNotMakeAGetCategoryWithInvalidId() throws Exception {

        this.mockMvc
                .perform(
                        get("/api/categories/{id}", "abc")
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldNotMakeAGetCategoryWithInexistingId() throws Exception {

        when(categoryService.getCategoryById(anyLong())).thenReturn(ResponseEntity.notFound().build());

        this.mockMvc
                .perform(
                        get("/api/categories/{id}", 1)
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldSaveACategory() throws Exception {

        CategoryDTO categoryDTO =new CategoryDTO();

        when(categoryService.saveCategory(any(CategoryDTO.class)))
                .thenReturn(ResponseEntity.ok(categoryDTO));

        this.mockMvc
                .perform(
                        post("/api/categories")
                                .with(csrf())
                                .content("{" +
                                        "  \"kind\": \"Apartamento\",\n" +
                                        "  \"details\": \"Apartamento com varanda\",\n" +
                                        "  \"qualification\": 2,\n" +
                                        "  \"imageURL\": \"url\"\n" +
                                        "}")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldNotDoAPutWhenItDoesntFindTheId() throws Exception {

        when(categoryService.updateCategory(1L, new CategoryDTO())).thenReturn(ResponseEntity.notFound().build());

        this.mockMvc
                .perform(
                        put("/api/categories/{id}", 1L)
                                .with(csrf())
                                .content("{}")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldEditAnExistingCategoryWithValidId() throws Exception {

        CategoryDTO categoryDto = new CategoryDTO(1234567L, "Apartamento", 2, "Apartamento com varanda", "url", List.of());

        when(categoryService.updateCategory(2L, categoryDto)).thenReturn(ResponseEntity.ok(categoryDto));

        this.mockMvc
                .perform(
                        put("/api/categories/{id}", 2)
                                .with(csrf())
                                .content("{" +
                                        "  \"sku\": 1234567,\n" +
                                        "  \"kind\": \"Apartamento\",\n" +
                                        "  \"qualification\": 2,\n" +
                                        "  \"details\": \"Apartamento com varanda\",\n" +
                                        "  \"imageUrl\": \"url\",\n" +
                                        "  \"features\": []\n" +
                                        "}")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sku").value(1234567))
                .andExpect(jsonPath("$.kind").value("Apartamento"))
                .andExpect(jsonPath("$.qualification").value(2))
                .andExpect(jsonPath("$.details").value("Apartamento com varanda"))
                .andExpect(jsonPath("$.imageUrl").value("url"));
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldNotDeleteAnInexistingId() throws Exception {

        when(categoryService.deleteCategory(eq(1L))).thenReturn(ResponseEntity.notFound().build());

        this.mockMvc
                .perform(
                        delete("/api/categories/{id}", 1)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldDeleteAnExistingId() throws Exception {

        Category category = new Category();
        category.setIdCategory(4L);

        when(categoryService.deleteCategory(eq(4L))).thenReturn(ResponseEntity.ok().build());

        this.mockMvc
                .perform(
                        delete("/api/categories/{id}", 4)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldNotDeleteAnInvalidId() throws Exception {

        this.mockMvc
                .perform(
                        delete("/api/categories/{id}", "abc")
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldGetACategoryBySku() throws Exception {
        CategoryDTO category = new CategoryDTO();

        when(categoryService.getCategoryBySku(eq(1234567L))).thenReturn(ResponseEntity.ok(category));

        this.mockMvc
                .perform(
                        get("/api/categories?sku={sku}", 1234567L)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldNotGetACategoryWithInexistingSku() throws Exception {

        when(categoryService.getCategoryBySku(anyLong())).thenReturn(ResponseEntity.notFound().build());

        this.mockMvc
                .perform(
                        get("/api/categories?sku={sku}", 1)
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldNotGetACategoryWithInvalidSku() throws Exception {

        this.mockMvc
                .perform(
                        get("/api/categories?sku={sku}", "abc")
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
    @Test
    @WithUserDetails("user@test.com")
    public void shouldNotEditACategoryWhenDoesNotFindTheSku() throws Exception {

        when(categoryService.updateCategoryBySku(1234567L, new CategoryDTO())).thenReturn(ResponseEntity.notFound().build());

        this.mockMvc
                .perform(
                        put("/api/categories?sku={sku}", 1234567L)
                                .with(csrf())
                                .content("{}")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldEditACategoryBySku() throws Exception {

        CategoryDTO categoryDto = new CategoryDTO(1234567L, "Apartamento", 2, "Apartamento com varanda", "url", List.of());

        when(categoryService.updateCategoryBySku(1234567L, categoryDto)).thenReturn(ResponseEntity.ok(categoryDto));

        this.mockMvc
                .perform(
                        put("/api/categories?sku={sku}", 1234567)
                                .with(csrf())
                                .content("{" +
                                        "  \"sku\": 1234567,\n" +
                                        "  \"kind\": \"Apartamento\",\n" +
                                        "  \"qualification\": 2,\n" +
                                        "  \"details\": \"Apartamento com varanda\",\n" +
                                        "  \"imageUrl\": \"url\",\n" +
                                        "  \"features\": []\n" +
                                        "}")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sku").value(1234567))
                .andExpect(jsonPath("$.kind").value("Apartamento"))
                .andExpect(jsonPath("$.qualification").value(2))
                .andExpect(jsonPath("$.details").value("Apartamento com varanda"))
                .andExpect(jsonPath("$.imageUrl").value("url"));
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldNotDeleteAnInexistingSku() throws Exception {

        when(categoryService.deleteCategoryBySku(eq(1234567L))).thenReturn(ResponseEntity.notFound().build());

        this.mockMvc
                .perform(
                        delete("/api/categories?sku={sku}", 1234567)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldDeleteACategoryBySku() throws Exception {

        Category category = new Category();
        category.setSku(1234567L);

        when(categoryService.deleteCategoryBySku(eq(1234567L))).thenReturn(ResponseEntity.ok().build());

        this.mockMvc
                .perform(
                        delete("/api/categories?sku={sku}", 1234567)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldNotDeleteACategoryWithInvalidSku() throws Exception {

        this.mockMvc
                .perform(
                        delete("/api/categories?sku={sku}", "abc")
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}