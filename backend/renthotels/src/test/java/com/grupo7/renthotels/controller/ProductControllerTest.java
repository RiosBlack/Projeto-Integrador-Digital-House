package com.grupo7.renthotels.controller;

import com.grupo7.renthotels.model.Product;
import com.grupo7.renthotels.model.dto.CategoryDTO;
import com.grupo7.renthotels.model.dto.CityDTO;
import com.grupo7.renthotels.model.dto.ProductDTO;
import com.grupo7.renthotels.security.SpringSecurityWebAuxTestConfig;
import com.grupo7.renthotels.service.ProductService;
import com.grupo7.renthotels.service.TokenService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
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
@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(ProductController.class)
@ContextConfiguration(classes = SpringSecurityWebAuxTestConfig.class)
@Import(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private TokenService tokenService;

    public String createJwtToken() {
        String secretKey = "testuserpass";
        String username = "user@test.com.br";
        String function = "Admin";

        String token = Jwts.builder()
                .setSubject(username)
                .claim("authorities", function)
                .signWith(SignatureAlgorithm.HS512, secretKey.getBytes())
                .compact();

        return "Bearer " + token;
    }
    @Test
    @WithUserDetails("user@test.com")
    public void shouldRegisterAProduct() throws Exception {

        ProductDTO productDTO = new ProductDTO();

        String jwtToken = createJwtToken();

        when(tokenService.isValidToken(anyString())).thenReturn(true);
        when(productService.saveProduct(any(ProductDTO.class), eq("token"))).thenReturn(ResponseEntity.ok(productDTO));

        this.mockMvc
                .perform(
                        post("/api/products")
                                .header("Authorization", jwtToken)
                                .with(csrf())
                                .content("{" +
                                        "  \"productTitle\": \"Hotel XYZ\",\n" +
                                        "  \"productDetails\": \"Dois quartos e uma suíte\", \n" +
                                        "  \"productPrice\": 653.28,\n" +
                                        "  \"productAddress\": \"Ruas das Desilusões\",\n" +
                                        "  \"productLatitude\": \"Latitude\",\n" +
                                        "  \"productLongitude\": \"Longitude\",\n" +
                                        "  \"images\": [],\n" +
                                        "  \"policies\": [],\n" +
                                        " \"category\": {" +
                                        "  \"sku\": 7654321,\n" +
                                        "  \"kind\": \"Delux\",\n" +
                                        "  \"qualification\": 5,\n" +
                                        "  \"details\": \"Todos as acomodações Delux incluem piscinas privativas\",\n" +
                                        "  \"imageUrl\": \"url\",\n" +
                                        "  \"features\": []\n" +
                                        "}," +
                                        " \"city\": {" +
                                        "  \"citySku\": 3698521,\n" +
                                        "  \"cityDenomination\": \"Sasacity\",\n" +
                                        "  \"cityCountry\": \"Sasalandia\"\n" +
                                        "}" +
                                        "}")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldGetAEmptyListOfProducts() throws Exception {
        when(productService.getAllProducts()).thenReturn(Collections.emptyList());

        this.mockMvc
                .perform(
                        get("/api/products")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldGetAListOfAllRegisteredProducts() throws Exception {
        ProductDTO productDTO = new ProductDTO();

        when(productService.getAllProducts()).thenReturn(List.of(productDTO));

        this.mockMvc
                .perform(
                        get("/api/products")
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldGetARegisteredProductById() throws Exception {
        ProductDTO productDTO = new ProductDTO();

        when(productService.getProductById(eq(1L))).thenReturn(ResponseEntity.ok(productDTO));

        this.mockMvc
                .perform(
                        get("/api/products/{id}", 1L)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldNotGetAProductWithAnInvalidId() throws Exception {
        this.mockMvc
                .perform(
                        get("/api/products/{id}", "abc")
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldNotGetAProductWithInexistingId() throws Exception {

        when(productService.getProductById(anyLong())).thenReturn(ResponseEntity.notFound().build());

        this.mockMvc
                .perform(
                        get("/api/cities/{id}", 1L)
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldNotEditAProductWithAnInexistingId() throws Exception {

        when(productService.updateProduct(eq(9L), any(ProductDTO.class))).thenReturn(ResponseEntity.notFound().build());

        this.mockMvc
                .perform(
                        put("/api/products/{id}", 9L)
                                .with(csrf())
                                .content("{}")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldEditAnRegisteredProductById() throws Exception {

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setSku(7654321L);
        categoryDTO.setKind("Delux");
        categoryDTO.setQualification(5);
        categoryDTO.setDetails("Todos as acomodações Delux incluem piscinas privativas");
        categoryDTO.setImageUrl("url");
        categoryDTO.setFeatures(List.of());

        CityDTO cityDTO = new CityDTO();
        cityDTO.setCitySku(3698521L);
        cityDTO.setCityDenomination("Sasacity");
        cityDTO.setCityCountry("Sasalandia");

        ProductDTO productDTO = new ProductDTO(1234567L,"Hotel ABC", "Quarto com varanda", 467.90F, "Rua dos Alfeneiros", "Latitude" , "Longitude", List.of(), List.of(), categoryDTO, cityDTO);

        when(productService.updateProduct(1L, productDTO)).thenReturn(ResponseEntity.ok(productDTO));

        this.mockMvc
                .perform(
                        put("/api/products/{id}", 1L)
                                .with(csrf())
                                .content("{" +
                                        " \"productSku\": 1234567,\n" +
                                        "  \"productTitle\": \"Hotel ABC\",\n" +
                                        "  \"productDetails\": \"Quarto com varanda\", \n" +
                                        "  \"productPrice\": 467.90,\n" +
                                        "  \"productAddress\": \"Rua dos Alfeneiros\",\n" +
                                        "  \"productLatitude\": \"Latitude\",\n" +
                                        "  \"productLongitude\": \"Longitude\",\n" +
                                        "  \"images\": [],\n" +
                                        "  \"policies\": [],\n" +
                                        " \"category\": {" +
                                        "  \"sku\": 7654321,\n" +
                                        "  \"kind\": \"Delux\",\n" +
                                        "  \"qualification\": 5,\n" +
                                        "  \"details\": \"Todos as acomodações Delux incluem piscinas privativas\",\n" +
                                        "  \"imageUrl\": \"url\",\n" +
                                        "  \"features\": []\n" +
                                        "}," +
                                        " \"city\": {" +
                                        "  \"citySku\": 3698521,\n" +
                                        "  \"cityDenomination\": \"Sasacity\",\n" +
                                        "  \"cityCountry\": \"Sasalandia\"\n" +
                                        "}" +
                                        "}")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productTitle").value("Hotel ABC"))
                .andExpect(jsonPath("$.productPrice").value(467.90F));
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldNotDeleteAProductWithAnInexistingId() throws Exception {

        when(productService.deleteProduct(eq(1L))).thenReturn(ResponseEntity.notFound().build());

        this.mockMvc
                .perform(
                        delete("/api/products/{id}", 1)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldDeleteAProductWithValidId() throws Exception {

        Product product = new Product();
        product.setIdProduct(4L);

        when(productService.deleteProduct(eq(4L))).thenReturn(ResponseEntity.ok().build());

        this.mockMvc
                .perform(
                        delete("/api/products/{id}", 4)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldNotDeleteAProductWithAnInvalidId() throws Exception {

        this.mockMvc
                .perform(
                        delete("/api/products/{id}", "abc")
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldGetProductsByCategories() throws Exception {
        ProductDTO product = new ProductDTO();

        when(productService.findByCategoryKind("Standard")).thenReturn(ResponseEntity.ok(List.of(product)));

        this.mockMvc
                .perform(
                        get("/api/products/categories/{kind}", "Standard")
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldNotGetProductsWithInexistingCategories() throws Exception {

        when(productService.findByCategoryKind("Kitnet")).thenReturn(ResponseEntity.notFound().build());

        this.mockMvc
                .perform(
                        get("/api/products/categories/{kind}", "Kitnet")
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldGetProductsByCities() throws Exception {
        ProductDTO product = new ProductDTO();

        when(productService.findByCityDenomination("Sasacity")).thenReturn(ResponseEntity.ok(List.of(product)));

        this.mockMvc
                .perform(
                        get("/api/products/cities/{cityDenomination}", "Sasacity")
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldNotGetProductsWithInexistingCities() throws Exception {

        when(productService.findByCityDenomination("Evercity")).thenReturn(ResponseEntity.notFound().build());

        this.mockMvc
                .perform(
                        get("/api/products/cities/{cityDenomination}", "Evercity")
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldGetARegisteredProductBySku() throws Exception {
        ProductDTO productDTO = new ProductDTO();

        when(productService.getProductBySku(eq(1234567L))).thenReturn(ResponseEntity.ok(productDTO));

        this.mockMvc
                .perform(
                        get("/api/products?productSku={sku}", 1234567L)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldNotGetAProductWithAnInvalidSku() throws Exception {
        this.mockMvc
                .perform(
                        get("/api/products?productSku={sku}", "abc")
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldNotGetAProductWithInexistingSku() throws Exception {

        when(productService.getProductBySku(anyLong())).thenReturn(ResponseEntity.notFound().build());

        this.mockMvc
                .perform(
                        get("/api/products?productSku={sku}", 1234567L)
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldNotEditAProductWithAnInexistingSku() throws Exception {

        when(productService.updateProductBySku(eq(1234567L), any(ProductDTO.class))).thenReturn(ResponseEntity.notFound().build());

        this.mockMvc
                .perform(
                        put("/api/products?productSku={sku}", 1234567L)
                                .with(csrf())
                                .content("{}")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldEditAnRegisteredProductBySku() throws Exception {

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setSku(7654321L);
        categoryDTO.setKind("Delux");
        categoryDTO.setQualification(5);
        categoryDTO.setDetails("Todos as acomodações Delux incluem piscinas privativas");
        categoryDTO.setImageUrl("url");
        categoryDTO.setFeatures(List.of());

        CityDTO cityDTO = new CityDTO();
        cityDTO.setCitySku(3698521L);
        cityDTO.setCityDenomination("Sasacity");
        cityDTO.setCityCountry("Sasalandia");

        ProductDTO productDTO = new ProductDTO(1234567L,"Hotel ABC", "Quarto com varanda", 467.90F, "Rua dos Alfeneiros", "Latitude" , "Longitude", List.of(), List.of(), categoryDTO, cityDTO);

        when(productService.updateProductBySku(1234567L, productDTO)).thenReturn(ResponseEntity.ok(productDTO));

        this.mockMvc
                .perform(
                        put("/api/products?productSku={sku}", 1234567L)
                                .with(csrf())
                                .content("{" +
                                        " \"productSku\": 1234567,\n" +
                                        "  \"productTitle\": \"Hotel ABC\",\n" +
                                        "  \"productDetails\": \"Quarto com varanda\", \n" +
                                        "  \"productPrice\": 467.90,\n" +
                                        "  \"productAddress\": \"Rua dos Alfeneiros\",\n" +
                                        "  \"productLatitude\": \"Latitude\",\n" +
                                        "  \"productLongitude\": \"Longitude\",\n" +
                                        "  \"images\": [],\n" +
                                        "  \"policies\": [],\n" +
                                        " \"category\": {" +
                                        "  \"sku\": 7654321,\n" +
                                        "  \"kind\": \"Delux\",\n" +
                                        "  \"qualification\": 5,\n" +
                                        "  \"details\": \"Todos as acomodações Delux incluem piscinas privativas\",\n" +
                                        "  \"imageUrl\": \"url\",\n" +
                                        "  \"features\": []\n" +
                                        "}," +
                                        " \"city\": {" +
                                        "  \"citySku\": 3698521,\n" +
                                        "  \"cityDenomination\": \"Sasacity\",\n" +
                                        "  \"cityCountry\": \"Sasalandia\"\n" +
                                        "}" +
                                        "}")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productTitle").value("Hotel ABC"))
                .andExpect(jsonPath("$.productPrice").value(467.90F));
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldNotDeleteAProductWithAnInexistingSku() throws Exception {

        when(productService.deleteProductBySku(eq(1234567L))).thenReturn(ResponseEntity.notFound().build());

        this.mockMvc
                .perform(
                        delete("/api/products?productSku={sku}", 1234567)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldDeleteAProductBySku() throws Exception {

        Product product = new Product();
        product.setProductSku(1234567L);

        when(productService.deleteProductBySku(eq(1234567L))).thenReturn(ResponseEntity.ok().build());

        this.mockMvc
                .perform(
                        delete("/api/products?productSku={sku}", 1234567)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldNotDeleteAProductWithAnInvalidSku() throws Exception {

        this.mockMvc
                .perform(
                        delete("/api/products?productSku={sku}", "abc")
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldReturnAvailableProductsByDate() throws Exception {
        LocalDate bookingStartDate = LocalDate.now();
        LocalDate bookingEndDate = LocalDate.now().plusDays(7);

        List<ProductDTO> productsAvailable = new ArrayList<>();
        productsAvailable.add(new ProductDTO());
        productsAvailable.add(new ProductDTO());

        when(productService.findByAvailability(bookingStartDate, bookingEndDate)).thenReturn(productsAvailable);

        mockMvc.perform(get("/api/products/dates/{bookingStartDate}/{bookingEndDate}", bookingStartDate, bookingEndDate))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldNotReturnAvailableProductsWithoutAllRequiredParams() throws Exception {
        mockMvc.perform(get("/dates/{bookingStartDate}/{bookingEndDate}", null, null))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldReturnAvailableProductsByDateAndCityDenomination() throws Exception {
        String cityDenomination = "Some Cool Place";
        LocalDate bookingStartDate = LocalDate.now();
        LocalDate bookingEndDate = LocalDate.now().plusDays(7);

        List<ProductDTO> productsAvailable = new ArrayList<>();
        productsAvailable.add(new ProductDTO());
        productsAvailable.add(new ProductDTO());

        when(productService.findByCityDenominationAndAvailability(cityDenomination, bookingStartDate, bookingEndDate))
                .thenReturn(ResponseEntity.ok(productsAvailable));

        mockMvc.perform(get("/api/products/city/{cityDenomination}/{bookingStartDate}/{bookingEndDate}", cityDenomination, bookingStartDate, bookingEndDate))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldNotReturnAvailableProductsByDateAndCityDenominationWithoutAllRequiredParams() throws Exception {
        LocalDate bookingStartDate = LocalDate.now();
        LocalDate bookingEndDate = LocalDate.now().plusDays(7);

        List<ProductDTO> productsAvailable = new ArrayList<>();
        productsAvailable.add(new ProductDTO());
        productsAvailable.add(new ProductDTO());

        when(productService.findByCityDenominationAndAvailability(null, bookingStartDate, bookingEndDate))
                .thenReturn(ResponseEntity.ok(productsAvailable));

        mockMvc.perform(get("/api/products/city/{cityDenomination}/{bookingStartDate}/{bookingEndDate}", null, bookingStartDate, bookingEndDate))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldNotReturnAvailableProductsByDateAndCityDenominationWithInexistingCity() throws Exception {
        LocalDate bookingStartDate = LocalDate.now();
        LocalDate bookingEndDate = LocalDate.now().plusDays(7);

        List<ProductDTO> productsAvailable = new ArrayList<>();
        productsAvailable.add(new ProductDTO());
        productsAvailable.add(new ProductDTO());

        when(productService.findByCityDenominationAndAvailability("somewhere", bookingStartDate, bookingEndDate))
                .thenReturn(ResponseEntity.notFound().build());

        mockMvc.perform(get("/api/products/city/{cityDenomination}/{bookingStartDate}/{bookingEndDate}", "somewhere", bookingStartDate, bookingEndDate))
                .andExpect(status().isNotFound());
    }
}