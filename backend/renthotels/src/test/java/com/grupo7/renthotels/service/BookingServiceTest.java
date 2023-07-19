package com.grupo7.renthotels.service;

import com.grupo7.renthotels.model.Booking;
import com.grupo7.renthotels.model.Category;
import com.grupo7.renthotels.model.City;
import com.grupo7.renthotels.model.Feature;
import com.grupo7.renthotels.model.Function;
import com.grupo7.renthotels.model.Product;
import com.grupo7.renthotels.model.User;
import com.grupo7.renthotels.model.dto.BookingDTO;
import com.grupo7.renthotels.model.dto.CategoryDTO;
import com.grupo7.renthotels.model.dto.CityDTO;
import com.grupo7.renthotels.model.dto.FeatureDTO;
import com.grupo7.renthotels.model.dto.ProductDTO;
import com.grupo7.renthotels.model.dto.UserDTO;
import com.grupo7.renthotels.repository.BookingRepository;
import com.grupo7.renthotels.repository.ProductRepository;
import com.grupo7.renthotels.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@ExtendWith(MockitoExtension.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private TokenService tokenService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private BookingService bookingService;


    @Test
    public void shouldGetAListOfBookings(){
        City city = new City();
        city.setCitySku(1234567L);

        Feature feat = new Feature();
        List<Feature> features = new ArrayList<>();
        features.add(feat);

        Category category = new Category();
        category.setSku(7654321L);
        category.setFeatures(features);

        Function function = new Function();
        function.setName("Admin");

        User user = new User();
        user.setUserSku(9632587L);
        user.setFunction(function);

        Product product = Product
                .builder()
                .productTitle("Gran Hotel")
                .productsDetails("Gran Hotel is amazing!")
                .productPrice(678.6F)
                .productAddress("")
                .images(List.of())
                .policies(List.of())
                .city(city)
                .category(category)
                .build();

        Booking booking = Booking.builder()
                .bookingCheckInTime("12:00h")
                .bookingStartDate(LocalDate.of(2023,8,15))
                .bookingEndDate(LocalDate.of(2023,8,20))
                .build();
        booking.setProduct(product);
        booking.setUser(user);

        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);

        when(bookingRepository.findAll()).thenReturn(bookings);

        var result = bookingService.getAllBookings();
        assertThat(result)
                .hasSize(1);
    }

    @Test
    public void shouldGetAnEmptyListOfBookings(){
        when(bookingRepository.findAll()).thenReturn(List.of());
        var response = bookingService.getAllBookings();

        assertThat(response).isEmpty();
    }

    @Test
    public void shouldSaveABooking() throws Exception {
        Function function = new Function();
        function.setName("Admin");

        User user = new User();
        user.setUserSku(9632587L);
        user.setFunction(function);

        City city = new City();
        city.setCitySku(1234567L);

        Feature feat = new Feature();
        List<Feature> features = new ArrayList<>();
        features.add(feat);

        Category category = new Category();
        category.setSku(7654321L);
        category.setFeatures(features);

        Product product = Product
                .builder()
                .productTitle("Gran Hotel")
                .productsDetails("Gran Hotel is amazing!")
                .productPrice(678.6F)
                .productAddress("")
                .images(List.of())
                .policies(List.of())
                .city(city)
                .category(category)
                .build();

        Booking booking = Booking.builder()
                .bookingCheckInTime("12:00h")
                .bookingStartDate(LocalDate.of(2023,8,15))
                .bookingEndDate(LocalDate.of(2023,8,20))
                .build();
        booking.setProduct(product);
        booking.setUser(user);

        UserDTO userDTO = new UserDTO();
        userDTO.setUserSku(9632587L);
        userDTO.setFunctionName("Admin");

        CityDTO cityDTO = new CityDTO();
        cityDTO.setCitySku(1234567L);

        FeatureDTO featsDTO = new FeatureDTO();
        List<FeatureDTO> featuresDTO = new ArrayList<>();
        featuresDTO.add(featsDTO);

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setSku(7654321L);
        categoryDTO.setFeatures(featuresDTO);

        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductTitle("Gran Hotel");
        productDTO.setProductDetails("Gran Hotel is amazing!");
        productDTO.setProductPrice(678.6F);
        productDTO.setProductAddress("Chaos Street");
        productDTO.setProductLatitude("Latitude");
        productDTO.setProductLongitude("Longitude");
        productDTO.setImages(List.of());
        productDTO.setPolicies(List.of());
        productDTO.setCity(cityDTO);
        productDTO.setCategory(categoryDTO);
        productDTO.setProductSku(8523697L);

        BookingDTO bookingDTO = new BookingDTO(7412589L, LocalDate.of(2023,10,15), LocalDate.of(2023,10,25), "12:00h", 1080F, "Some city", productDTO, userDTO);

        when(tokenService.isValidToken(anyString())).thenReturn(true);
        when(productRepository.findByProductSku(8523697L)).thenReturn(Optional.of(product));
        when(userRepository.findByUserSku(9632587L)).thenReturn(Optional.of(user));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        when(productService.findByAvailability(bookingDTO.getBookingStartDate(), bookingDTO.getBookingEndDate())).thenReturn(List.of(productDTO));

        var response = bookingService.saveBooking(bookingDTO, "token");

        assertThat(response.getStatusCode()).isEqualTo(OK);
    }

    @Test
    public void shouldNotSaveABookingWhithoutAProduct(){
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductSku(8523697L);

        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setProductDTO(productDTO);

        when(productRepository.findByProductSku(8523697L)).thenReturn(Optional.empty());
        when(tokenService.isValidToken(anyString())).thenReturn(true);

        String token = "validToken";

        assertThrows(EntityNotFoundException.class, () -> bookingService.saveBooking(bookingDTO, token));
    }

    @Test
    public void shouldNotSaveABookingWhithoutAValidToken() throws Exception {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductSku(8523697L);

        Product product = new Product();

        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setProductDTO(productDTO);

        when(productRepository.findByProductSku(8523697L)).thenReturn(Optional.of(product));
        when(tokenService.isValidToken(anyString())).thenReturn(false);

        String token = "validToken";

        var response = bookingService.saveBooking(bookingDTO, token);

        assertThat(response.getStatusCode()).isEqualTo(UNAUTHORIZED);
        }

        @Test
        public void shouldGetABookingById(){
        Function function = new Function();
        function.setName("Admin");

        User user = new User();
        user.setUserSku(2589631L);
        user.setFunction(function);

        Category category = new Category();
        category.setSku(8523697L);
        category.setFeatures(List.of());

        City city = new City();
        city.setCitySku(7412583L);

        Product product = new Product();
        product.setProductSku(1234567L);
        product.setImages(List.of());
        product.setCategory(category);
        product.setCity(city);
        product.setPolicies(List.of());

            Booking booking = new Booking();
        booking.setIdBooking(1L);
        booking.setProduct(product);
        booking.setUser(user);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        var response = bookingService.getBookingById(1L);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        }

        @Test
        public void shouldNotGetABookingWhitInexistingId(){

        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        var response = bookingService.getBookingById(1L);

        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
        }

        @Test
        public void shouldDeleteABookingById(){
            Function function = new Function();
            function.setName("Admin");

            User user = new User();
            user.setUserSku(2589631L);
            user.setFunction(function);

            Category category = new Category();
            category.setSku(8523697L);
            category.setFeatures(List.of());

            City city = new City();
            city.setCitySku(7412583L);

            Product product = new Product();
            product.setProductSku(1234567L);
            product.setImages(List.of());
            product.setCategory(category);
            product.setCity(city);

            Booking booking = new Booking();
            booking.setIdBooking(1L);
            booking.setProduct(product);
            booking.setUser(user);

            when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

            var response = bookingService.deleteBooking(1L);

            assertThat(response.getStatusCode()).isEqualTo(NO_CONTENT);
        }

        @Test
        public void shouldNotDeleteABookingWithInexistingId(){

        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        var response = bookingService.deleteBooking(1L);

        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
        }

    @Test
    public void shouldGetABookingBySku(){
        Function function = new Function();
        function.setName("Admin");

        User user = new User();
        user.setUserSku(2589631L);
        user.setFunction(function);

        Category category = new Category();
        category.setSku(8523697L);
        category.setFeatures(List.of());

        City city = new City();
        city.setCitySku(7412583L);

        Product product = new Product();
        product.setProductSku(1234567L);
        product.setImages(List.of());
        product.setPolicies(List.of());
        product.setCategory(category);
        product.setCity(city);

        Booking booking = new Booking();
        booking.setBookingSku(3214569L);
        booking.setProduct(product);
        booking.setUser(user);

        when(bookingRepository.findByBookingSku(3214569L)).thenReturn(Optional.of(booking));

        var response = bookingService.getBookingBySku(3214569L);

        assertThat(response.getStatusCode()).isEqualTo(OK);
    }

    @Test
    public void shouldNotGetAnBookingWhitInexistingSku(){

        when(bookingRepository.findByBookingSku(3214569L)).thenReturn(Optional.empty());

        var response = bookingService.getBookingBySku(3214569L);

        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
    }

    @Test
    public void shouldDeleteABookingBySku(){
        Function function = new Function();
        function.setName("Admin");

        User user = new User();
        user.setUserSku(2589631L);
        user.setFunction(function);

        Category category = new Category();
        category.setSku(8523697L);
        category.setFeatures(List.of());

        City city = new City();
        city.setCitySku(7412583L);

        Product product = new Product();
        product.setProductSku(1234567L);
        product.setImages(List.of());
        product.setCategory(category);
        product.setCity(city);

        Booking booking = new Booking();
        booking.setBookingSku(3214569L);
        booking.setProduct(product);
        booking.setUser(user);

        when(bookingRepository.findByBookingSku(3214569L)).thenReturn(Optional.of(booking));

        var response = bookingService.deleteBookingBySku(3214569L);

        assertThat(response.getStatusCode()).isEqualTo(NO_CONTENT);
    }

    @Test
    public void shouldNotDeleteABookingWithInexistingSku(){

        when(bookingRepository.findByBookingSku(3214569L)).thenReturn(Optional.empty());

        var response = bookingService.deleteBookingBySku(3214569L);

        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
    }

    @Test
    public void shouldGetByUserSku() {
        Function function = new Function();
        function.setFunctionSku(9632587L);

        User user = new User();
        user.setUserSku(1234567L);
        user.setFunction(function);

        Category category = new Category();
        category.setFeatures(List.of());

        City city = new City();

        Product product = new Product();
        product.setProductSku(1234567L);
        product.setPolicies(List.of());
        product.setImages(List.of());
        product.setCity(city);
        product.setCategory(category);

        Booking booking = new Booking();
        booking.setProduct(product);
        booking.setUser(user);

        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        List<BookingDTO> bookingsByUser = bookings.stream().map(BookingDTO::from).collect(Collectors.toList());

        when(bookingRepository.findByUserUserSku(user.getUserSku())).thenReturn(bookings);

        ResponseEntity<List<BookingDTO>> response = bookingService.getByUserSku(user.getUserSku());

        assertEquals(bookingsByUser, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void shouldGetByProductSku() {
        Function function = new Function();
        function.setFunctionSku(9632587L);

        User user = new User();
        user.setUserSku(1234567L);
        user.setFunction(function);

        Category category = new Category();
        category.setFeatures(List.of());

        City city = new City();

        Product product = new Product();
        product.setProductSku(1234567L);
        product.setPolicies(List.of());
        product.setImages(List.of());
        product.setCity(city);
        product.setCategory(category);

        Booking booking = new Booking();
        booking.setProduct(product);
        booking.setUser(user);

        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        List<BookingDTO> bookingsByProduct = bookings.stream().map(BookingDTO::from).collect(Collectors.toList());

        when(bookingRepository.findByProductProductSku(product.getProductSku())).thenReturn(bookings);

        ResponseEntity<List<BookingDTO>> response = bookingService.getByProductSku(product.getProductSku());

        assertEquals(bookingsByProduct, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}
