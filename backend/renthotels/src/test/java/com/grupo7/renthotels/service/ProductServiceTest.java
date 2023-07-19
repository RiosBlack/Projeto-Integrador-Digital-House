package com.grupo7.renthotels.service;

import com.grupo7.renthotels.exception.NotFoundException;
import com.grupo7.renthotels.exception.UnprocessableEntityException;
import com.grupo7.renthotels.model.Booking;
import com.grupo7.renthotels.model.Category;
import com.grupo7.renthotels.model.City;
import com.grupo7.renthotels.model.Function;
import com.grupo7.renthotels.model.Product;
import com.grupo7.renthotels.model.User;
import com.grupo7.renthotels.model.dto.CategoryDTO;
import com.grupo7.renthotels.model.dto.CityDTO;
import com.grupo7.renthotels.model.dto.ProductDTO;
import com.grupo7.renthotels.repository.BookingRepository;
import com.grupo7.renthotels.repository.CategoryRepository;
import com.grupo7.renthotels.repository.CityRepository;
import com.grupo7.renthotels.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CityRepository cityRepository;

    @Mock
    BookingRepository bookingRepository;

    @Mock
    TokenService tokenService;

    @InjectMocks
    private ProductService productService;

    @Test
    public void shouldSaveAProduct() throws NotFoundException {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setFeatures(List.of());
        CityDTO cityDTO = new CityDTO();

        ProductDTO productDTO = new ProductDTO(1234567L,"Hotel ABC", "Quarto com varanda", 467.90F, "Rua dos Alfeneiros", "Latitude", "Longitude", List.of(), List.of(), categoryDTO, cityDTO);

        Category category = new Category();
        category.setFeatures(List.of());
        when(categoryRepository.findBySku(productDTO.getCategory().getSku())).thenReturn(Optional.of(category));

        City city = new City();
        when(cityRepository.findByCitySku(productDTO.getCity().getCitySku())).thenReturn(Optional.of(city));

        Product product = new Product();
        product.setProductTitle("Hotel ABC");

        when(tokenService.isValidToken(any())).thenReturn(true);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        var result = productService.saveProduct(productDTO, "token");

        assertNotNull(result.getBody());
        assertEquals(product.getProductTitle(), result.getBody().getProductTitle());
    }

    @Test
    public void shouldNotSaveAProductWhenDoesNotFindTheCategorySku(){
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setSku(9632587L);
        CityDTO cityDTO = new CityDTO();
        cityDTO.setCitySku(7654321L);
        ProductDTO productDTO = new ProductDTO();
        productDTO.setCity(cityDTO);
        productDTO.setCategory(categoryDTO);

        when(tokenService.isValidToken(any())).thenReturn(true);
        when(categoryRepository.findBySku(productDTO.getCategory().getSku())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> productService.saveProduct(productDTO, "token"));
    }

    @Test
    public void shouldNotSaveAProductWhenDoesNotFindTheCitySku(){
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setSku(9632587L);
        CityDTO cityDTO = new CityDTO();
        cityDTO.setCitySku(7654321L);
        ProductDTO productDTO = new ProductDTO();
        productDTO.setCity(cityDTO);
        productDTO.setCategory(categoryDTO);

        Category category = new Category();

        when(categoryRepository.findBySku(productDTO.getCategory().getSku())).thenReturn(Optional.of(category));

        when(cityRepository.findByCitySku(productDTO.getCity().getCitySku())).thenReturn(Optional.empty());

        when(tokenService.isValidToken(any())).thenReturn(true);

        assertThrows(EntityNotFoundException.class, () -> productService.saveProduct(productDTO, "token"));
    }

    @Test
    public void shouldGetAEmptyListOfProducts(){
        when(productRepository.findAll()).thenReturn(new ArrayList<>());

        List<ProductDTO> products = productService.getAllProducts();

        assertNotNull(products);
        assertTrue(products.isEmpty());

    }

    @Test
    public void shouldGetAListOfAllProducts(){
        Category category = new Category();
        category.setFeatures(List.of());
        City city = new City();

        Product product = new Product();
        product.setCategory(category);
        product.setCity(city);
        product.setImages(List.of());
        product.setPolicies(List.of());

        when(productRepository.findAll()).thenReturn(List.of(product));

        var result = productService.getAllProducts();
        assertEquals(result.size(), 1);
    }

    @Test
    public void shouldGetAProductById(){
        Category category = new Category();
        category.setFeatures(List.of());
        City city = new City();

        Product product = Product
                .builder()
                .productTitle("Studio")
                .productsDetails("Tudo que você precisa em modo compacto")
                .productPrice(465.75F)
                .productAddress("Rua dos Canários")
                .images(List.of())
                .policies(List.of())
                .category(category)
                .city(city)
                .build();
        product.setIdProduct(2L);

        when(productRepository.findById(2L)).thenReturn(Optional.of(product));

        var result = productService.getProductById(2L);

        assertEquals(result.getStatusCode(), HttpStatusCode.valueOf(200));
        assertEquals(result.getBody().getProductTitle(), "Studio");
    }

    @Test
    public void shouldNotGetAProductWithInexistingId(){
        when(productRepository.findById(3L)).thenReturn(Optional.empty());

        var result = productService.getProductById(3L);

        assertEquals(result.getStatusCode(), HttpStatusCode.valueOf(404));
    }

    @Test
    public void shouldEditAProduct() throws UnprocessableEntityException, NotFoundException {
        Category category = new Category();
        category.setSku(8521479L);
        category.setFeatures(List.of());

        City city = new City();
        city.setCitySku(9631478L);

        Product product = new Product();
        product.setProductTitle("Hotel");
        product.setIdProduct(4L);
        product.setImages(List.of());
        product.setPolicies(List.of());
        product.setCategory(category);
        product.setCity(city);

        when(productRepository.findById(eq(4L))).thenReturn(
                Optional.of(product)
        );

        when(productRepository.save(argThat(productToSave -> productToSave.getIdProduct().equals(4L))))
                .then(returnsFirstArg());

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setFeatures(List.of());
        CityDTO cityDTO = new CityDTO();
        ProductDTO newParams = new ProductDTO();
        newParams.setProductTitle("Pousada");
        newParams.setImages(List.of());
        newParams.setCategory(categoryDTO);
        newParams.setCity(cityDTO);

        var updatedProduct = productService.updateProduct(4L, newParams);
        assertEquals(updatedProduct.getStatusCode(), HttpStatusCode.valueOf(200));
        assertEquals(updatedProduct.getBody().getProductTitle(), "Pousada");
    }

    @Test
    public void shouldNotEditAProduct() throws UnprocessableEntityException, NotFoundException {
        when(productRepository.findById(eq(4L))).thenReturn(Optional.empty());

        var result = productService.updateProduct(4L, any(ProductDTO.class));

        assertEquals(result.getStatusCode(), HttpStatusCode.valueOf(404));
    }

    @Test
    public void shouldDeleteAProduct(){
        Product product = new Product();
        product.setIdProduct(5L);

        when(productRepository.findById(eq(5L))).thenReturn(Optional.of(product));

        var result = productService.deleteProduct(5L);
        assertEquals(result.getStatusCode(), HttpStatusCode.valueOf(204));
    }

    @Test
    public void shouldNotDeleteAProduct(){
        when(productRepository.findById(eq(5L))).thenReturn(Optional.empty());

        var result = productService.deleteProduct(5L);
        assertEquals(result.getStatusCode(), HttpStatusCode.valueOf(404));
    }

    @Test
    public void shouldFindAProductByCategoryKind(){
        Category category = new Category();
        category.setKind("Eurostar");
        category.setFeatures(List.of());

        City city = new City();

        Product product1 = new Product();
        product1.setCategory(category);
        product1.setCity(city);
        product1.setImages(List.of());
        product1.setPolicies(List.of());

        Product product2 = new Product();
        product2.setCategory(category);
        product2.setCity(city);
        product2.setImages(List.of());
        product2.setPolicies(List.of());

        List<Product> products = List.of(product1, product2);

        when(productRepository.getByCategoryKind("Eurostar")).thenReturn(products);

        var result = productService.findByCategoryKind("Eurostar");

        assertEquals(result.getStatusCode(), HttpStatusCode.valueOf(200));
        assertNotNull(result.getBody());
        assertEquals(result.getBody().size(), products.size());
    }

    @Test
    public void shouldFindAProductByCityDenomination(){
        Category category = new Category();
        category.setFeatures(List.of());

        City city = new City();
        city.setCityDenomination("Gabcity");

        Product product1 = new Product();
        product1.setCategory(category);
        product1.setCity(city);
        product1.setImages(List.of());
        product1.setPolicies(List.of());

        Product product2 = new Product();
        product2.setCategory(category);
        product2.setCity(city);
        product2.setImages(List.of());
        product2.setPolicies(List.of());

        List<Product> products = List.of(product1, product2);

        when(productRepository.getByCityCityDenomination("Gabcity")).thenReturn(products);

        var result = productService.findByCityDenomination("Gabcity");

        assertEquals(result.getStatusCode(), HttpStatusCode.valueOf(200));
        assertNotNull(result.getBody());
        assertEquals(result.getBody().size(), products.size());
    }

    @Test
    public void shouldGetAProductBySku(){
        Category category = new Category();
        category.setFeatures(List.of());
        City city = new City();

        Product product = Product
                .builder()
                .productTitle("Studio")
                .productsDetails("Tudo que você precisa em modo compacto")
                .productPrice(465.75F)
                .productAddress("Rua dos Canários")
                .images(List.of())
                .policies(List.of())
                .category(category)
                .city(city)
                .build();
        product.setProductSku(1234567L);

        when(productRepository.findByProductSku(1234567L)).thenReturn(Optional.of(product));

        var result = productService.getProductBySku(1234567L);

        assertEquals(result.getStatusCode(), HttpStatusCode.valueOf(200));
        assertEquals(result.getBody().getProductTitle(), "Studio");
    }

    @Test
    public void shouldNotGetAProductWithInexistingSku(){
        when(productRepository.findByProductSku(1234567L)).thenReturn(Optional.empty());

        var result = productService.getProductBySku(1234567L);

        assertEquals(result.getStatusCode(), HttpStatusCode.valueOf(404));
    }

    @Test
    public void shouldEditAProductBySku(){
        Category category = new Category();
        category.setFeatures(List.of());
        City city = new City();

        Product product = Product
                .builder()
                .productTitle("Pousada")
                .productsDetails("Permitido pets")
                .productPrice(300.5F)
                .productAddress("Rua Icaro")
                .images(List.of())
                .policies(List.of())
                .city(city)
                .category(category)
                .build();
        product.setProductSku(1234567L);

        when(productRepository.findByProductSku(eq(1234567L))).thenReturn(
                Optional.of(product)
        );

        when(productRepository.save(argThat(productToSave -> productToSave.getProductSku().equals(1234567L))))
                .then(returnsFirstArg());

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setFeatures(List.of());
        CityDTO cityDTO = new CityDTO();

        ProductDTO newParams = new ProductDTO(1234567L, "Hotel Fazenda", "Conforto junto a natureza", 450.9F, "Rua Italo Calvino", "Latitude", "Longitude", List.of(), List.of(), categoryDTO, cityDTO);

        var updatedProduct = productService.updateProductBySku(1234567L, newParams);
        assertEquals(updatedProduct.getStatusCode(), HttpStatusCode.valueOf(200));
        assertEquals(updatedProduct.getBody().getProductTitle(), "Hotel Fazenda");
    }

    @Test
    public void shouldNotEditAProductWhenDoesNotFindTheSku(){
        when(productRepository.findByProductSku(eq(1234567L))).thenReturn(Optional.empty());

        var result = productService.updateProductBySku(1234567L, any(ProductDTO.class));

        assertEquals(result.getStatusCode(), HttpStatusCode.valueOf(404));
    }

    @Test
    public void shouldDeleteAProductBySku(){
        Product product = new Product();
        product.setProductSku(1234567L);

        when(productRepository.findByProductSku(eq(1234567L))).thenReturn(Optional.of(product));

        var result = productService.deleteProductBySku(1234567L);
        assertEquals(result.getStatusCode(), HttpStatusCode.valueOf(204));
    }

    @Test
    public void shouldNotDeleteAProductWhenDoesNotFindTheSku(){
        when(productRepository.findByProductSku(eq(1234567L))).thenReturn(Optional.empty());

        var result = productService.deleteProductBySku(1234567L);
        assertEquals(result.getStatusCode(), HttpStatusCode.valueOf(404));
    }

    @Test
    public void shouldReturnAListOfProductsAvailableInDateResearch(){
        LocalDate bookingStartDate = LocalDate.now();
        LocalDate bookingEndDate = LocalDate.now().plusDays(7);

        Category category = new Category();
        category.setFeatures(List.of());
        City city = new City();

        Product product1 = new Product();
        product1.setImages(List.of());
        product1.setCategory(category);
        product1.setCity(city);
        product1.setProductSku(1234567L);
        product1.setPolicies(List.of());

        Product product2 = new Product();
        product2.setImages(List.of());
        product2.setCategory(category);
        product2.setCity(city);
        product2.setProductSku(7654321L);
        product2.setPolicies(List.of());

        List<Product> productsAvailable = new ArrayList<>();
        productsAvailable.add(product1);
        productsAvailable.add(product2);

        when(productRepository.findAll()).thenReturn(productsAvailable);
        when(bookingRepository.findAllByBookingStartDateBetween(bookingStartDate, bookingEndDate)).thenReturn(Collections.emptyList());

        List<ProductDTO> result = productService.findByAvailability(bookingStartDate, bookingEndDate);

        assertEquals(productsAvailable.size(), result.size());
        assertThat(result.get(0).getProductSku()).isEqualTo(1234567L);
        assertThat(result.get(1).getProductSku()).isEqualTo(7654321L);
    }

    @Test
    public void shouldReturnAListOfProductsAvailableInDateResearchIgnorinBookedProducts() {
        LocalDate bookingStartDate = LocalDate.now();
        LocalDate bookingEndDate = LocalDate.now().plusDays(7);

        Category category = new Category();
        category.setFeatures(List.of());
        City city = new City();

        Product product1 = new Product();
        product1.setImages(List.of());
        product1.setCategory(category);
        product1.setCity(city);
        product1.setProductSku(1234567L);
        product1.setPolicies(List.of());

        Product product2 = new Product();
        product2.setImages(List.of());
        product2.setCategory(category);
        product2.setCity(city);
        product2.setProductSku(7654321L);
        product2.setPolicies(List.of());

        List<Product> products = List.of(product1, product2);

        Function function = new Function();
        function.setName("Admin");

        User user = new User();
        user.setFunction(function);

        Booking booking = new Booking();
        booking.setProduct(product1);
        booking.setUser(user);

        List<Booking> bookedProducts = List.of(booking);

        when(productRepository.findAll()).thenReturn(products);
        when(bookingRepository.findAllByBookingStartDateBetween(bookingStartDate, bookingEndDate)).thenReturn(bookedProducts);

        List<ProductDTO> result = productService.findByAvailability(bookingStartDate, bookingEndDate);

        assertEquals(1, result.size());
        assertThat(result.get(0).getProductSku()).isEqualTo(7654321L);
    }

    @Test
    public void shouldReturnAvailableProductsByDateAndCity() {

        LocalDate bookingStartDate = LocalDate.now();
        LocalDate bookingEndDate = LocalDate.now().plusDays(7);

        Category category = new Category();
        category.setFeatures(List.of());
        City city = new City();
        city.setCityDenomination("Some Cool Place");

        Product product1 = new Product();
        product1.setImages(List.of());
        product1.setCategory(category);
        product1.setCity(city);
        product1.setProductSku(1234567L);
        product1.setPolicies(List.of());

        Product product2 = new Product();
        product2.setImages(List.of());
        product2.setCategory(category);
        product2.setCity(city);
        product2.setProductSku(7654321L);
        product2.setPolicies(List.of());

        List<Product> products = List.of(product1, product2);

        when(productRepository.findAll()).thenReturn(products);
        when(bookingRepository.findAllByBookingStartDateBetween(bookingStartDate, bookingEndDate)).thenReturn(List.of());

         var result = productService.findByCityDenominationAndAvailability(city.getCityDenomination(), bookingStartDate, bookingEndDate);

        assertEquals(2, result.getBody().size());
    }

    @Test
    public void shouldNotReturnAvailableProducts() {

        LocalDate bookingStartDate = LocalDate.now();
        LocalDate bookingEndDate = LocalDate.now().plusDays(7);

        Category category = new Category();
        category.setFeatures(List.of());
        City city = new City();
        city.setCityDenomination("Some Cool Place");

        Product product1 = new Product();
        product1.setImages(List.of());
        product1.setCategory(category);
        product1.setCity(city);
        product1.setProductSku(1234567L);
        product1.setPolicies(List.of());

        Product product2 = new Product();
        product2.setImages(List.of());
        product2.setCategory(category);
        product2.setCity(city);
        product2.setProductSku(7654321L);
        product2.setPolicies(List.of());

        List<Product> products = List.of(product1, product2);

        Function function = new Function();
        function.setName("Admin");

        User user = new User();
        user.setFunction(function);

        Booking booking1 = new Booking();
        booking1.setProduct(product2);
        booking1.setUser(user);

        Booking booking2 = new Booking();
        booking2.setProduct(product1);
        booking2.setUser(user);

        List<Booking> bookedProducts = List.of(booking1, booking2);

        when(productRepository.findAll()).thenReturn(products);
        when(bookingRepository.findAllByBookingStartDateBetween(bookingStartDate, bookingEndDate)).thenReturn(bookedProducts);

        var result = productService.findByCityDenominationAndAvailability(city.getCityDenomination(), bookingStartDate, bookingEndDate);

        assertThat(result.getBody()).isEmpty();
    }
}
