package com.grupo7.renthotels.controller;

import com.grupo7.renthotels.model.Booking;
import com.grupo7.renthotels.model.Category;
import com.grupo7.renthotels.model.City;
import com.grupo7.renthotels.model.Function;
import com.grupo7.renthotels.model.Product;
import com.grupo7.renthotels.model.User;
import com.grupo7.renthotels.model.dto.BookingDTO;
import com.grupo7.renthotels.model.dto.CategoryDTO;
import com.grupo7.renthotels.model.dto.CityDTO;
import com.grupo7.renthotels.model.dto.FunctionDTO;
import com.grupo7.renthotels.model.dto.ProductDTO;
import com.grupo7.renthotels.model.dto.UserDTO;
import com.grupo7.renthotels.security.SpringSecurityWebAuxTestConfig;
import com.grupo7.renthotels.service.BookingService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(BookingController.class)
@Import(BookingController.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringSecurityWebAuxTestConfig.class)
public class BookingControllerTest {

    @MockBean
    private BookingService bookingService;

    @Autowired
    MockMvc mockMvc;

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

    String token = createJwtToken();

    @Test
    @WithUserDetails("user@test.com")
    public void shouldSaveABooking() throws Exception {
        FunctionDTO functionDTO = new FunctionDTO();
        functionDTO.setFunctionName("Admin");

        UserDTO userDTO = new UserDTO(3698521L, "User", "Test", "user@test.com", "usertestpass", "Admin");

        CategoryDTO categoryDTO = new CategoryDTO(7654321L, "Delux", 5, "Todos as acomodações Delux incluem piscinas privativas", "url", List.of());

        CityDTO cityDTO = new CityDTO(3698521L, "Sasacity", "Sasalandia");

    ProductDTO productDTO = new ProductDTO(7654321L, "Gran Hotel", "Gran Hotel is amazing!", 963.5F, "Chaos Street", "latitude", "longitude", List.of(), List.of(), categoryDTO, cityDTO);

        BookingDTO bookingDTO = new BookingDTO(1478523L, LocalDate.of(2023, 10, 15), LocalDate.of(2023,10,25), "14h", 1080.00F, "Some city", productDTO, userDTO);

        when(bookingService.saveBooking(any(BookingDTO.class), eq(token))).thenReturn(ResponseEntity.ok(bookingDTO));

        this.mockMvc
                .perform(
                        post("/api/bookings")
                                .header("Authorization", token)
                                .with(csrf())
                                .content("{\n" +
                                        "  \"bookingStartDate\": \"2023-10-15\",\n" +
                                        "  \"bookingEndDate\": \"2023-10-15\",\n" +
                                        "  \"bookingCheckInTime\": \"14h\",\n" +
                                        "  \"productDTO\": {\n" +
                                        "    \"productSku\": 7654321,\n" +
                                        "    \"productTitle\": \"Gran Hotel\",\n" +
                                        "    \"productDetails\": \"Gran Hotel is amazing!\",\n" +
                                        "    \"productPrice\": 963.5,\n" +
                                        "    \"productAddress\": \"Chaos Street\",\n" +
                                        "    \"images\": [],\n" +
                                        "    \"categoryDTO\": {\n" +
                                        "      \"sku\": 7654321,\n" +
                                        "      \"kind\": \"Delux\",\n" +
                                        "      \"qualification\": 5,\n" +
                                        "      \"details\": \"Todos as acomodações Delux incluem piscinas privativas\",\n" +
                                        "      \"imageURL\": \"url\",\n" +
                                        "      \"features\": []\n" +
                                        "    },\n" +
                                        "    \"cityDTO\": {\n" +
                                        "      \"citySku\": 3698521,\n" +
                                        "      \"cityDenomination\": \"Sasacity\",\n" +
                                        "      \"cityCountry\": \"Sasalandia\"\n" +
                                        "    }\n" +
                                        "  },\n" +
                                        "  \"userDTO\": {\n" +
                                        "    \"userSku\": 3698521,\n" +
                                        "    \"name\": \"User\",\n" +
                                        "    \"surname\": \"Test\",\n" +
                                        "    \"email\": \"user@test.com\",\n" +
                                        "    \"functionName\": \"Admin\"\n" +
                                        "  }\n" +
                                        "}")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookingCheckInTime").value("14h"));
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldGetAnEmptyListOfBookings() throws Exception {
        when(bookingService.getAllBookings()).thenReturn(Collections.emptyList());

        this.mockMvc
                .perform(
                        get("/api/bookings")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldGetAListOfAllRegisteredBookings() throws Exception {
        FunctionDTO functionDTO = new FunctionDTO();
        functionDTO.setFunctionName("Admin");

        UserDTO userDTO = new UserDTO();
        userDTO.setFunctionName("Admin");

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setFeatures(List.of());

        CityDTO cityDTO = new CityDTO();

        ProductDTO productDTO = new ProductDTO();
        productDTO.setCategory(categoryDTO);
        productDTO.setCity(cityDTO);
        productDTO.setImages(List.of());

        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setUserDTO(userDTO);
        bookingDTO.setProductDTO(productDTO);

        when(bookingService.getAllBookings()).thenReturn(List.of(bookingDTO));

        this.mockMvc
                .perform(
                        get("/api/bookings")
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldGetAnBookingById()  throws Exception {
        FunctionDTO functionDTO = new FunctionDTO();
        functionDTO.setFunctionName("Admin");

        UserDTO userDTO = new UserDTO();
        userDTO.setFunctionName("Admin");

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setFeatures(List.of());

        CityDTO cityDTO = new CityDTO();

        ProductDTO productDTO = new ProductDTO();
        productDTO.setCategory(categoryDTO);
        productDTO.setCity(cityDTO);
        productDTO.setImages(List.of());

        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setUserDTO(userDTO);
        bookingDTO.setProductDTO(productDTO);

        when(bookingService.getBookingById(1L)).thenReturn(ResponseEntity.ok(bookingDTO));

        this.mockMvc
                .perform(
                        get("/api/bookings/{id}", 1L)
                                .with(csrf())
                                .header("Authorization", token)

                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldNotGetABookingWithInvalidId() throws Exception {

        this.mockMvc
                .perform(
                        get("/api/bookings/{id}", "abc")
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldNotGetABookingWithInexistingId() throws Exception {

        when(bookingService.getBookingById(5L)).thenReturn(ResponseEntity.notFound().build());

        this.mockMvc
                .perform(
                        get("/api/bookings/{id}", 5)
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldDeleteAnBookingById() throws Exception {
        Function function = new Function();
        function.setName("Admin");

        User user = new User();
        user.setFunction(function);

        Category category = new Category();
        category.setFeatures(List.of());

        City city = new City();

        Product product = new Product();
        product.setCity(city);
        product.setCategory(category);
        product.setImages(List.of());

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setProduct(product);
        booking.setIdBooking(1L);

        when(bookingService.deleteBooking(eq(1L))).thenReturn(ResponseEntity.ok().build());

        this.mockMvc
                .perform(
                        get("/api/bookings/{id}", 1)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldNotDeleteABookingWithInexistingId() throws Exception {

        when(bookingService.deleteBooking(eq(1L))).thenReturn(ResponseEntity.notFound().build());

        this.mockMvc
                .perform(
                        delete("/api/bookings/{id}", 1)
                                .header("Authorization", token)
                                .with(csrf())

                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldNotDeleteABookingWithInvalidId() throws Exception {

        this.mockMvc
                .perform(
                        delete("/api/bookings/{id}", "abc")
                                .header("Authorization", token)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldGetAnBookingBySku()  throws Exception {
        FunctionDTO functionDTO = new FunctionDTO();
        functionDTO.setFunctionName("Admin");

        UserDTO userDTO = new UserDTO();
        userDTO.setFunctionName("Admin");

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setFeatures(List.of());

        CityDTO cityDTO = new CityDTO();

        ProductDTO productDTO = new ProductDTO();
        productDTO.setCategory(categoryDTO);
        productDTO.setCity(cityDTO);
        productDTO.setImages(List.of());

        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setUserDTO(userDTO);
        bookingDTO.setProductDTO(productDTO);

        when(bookingService.getBookingBySku(1234567L)).thenReturn(ResponseEntity.ok(bookingDTO));

        this.mockMvc
                .perform(
                        get("/api/bookings?bookingSku={bookingSku}", 1234567L)
                                .with(csrf())
                                .header("Authorization", token)

                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldNotGetABookingWithInvalidSku() throws Exception {

        this.mockMvc
                .perform(
                        get("/api/bookings?bookingSku={bookingSku}", "abc")
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldNotGetABookingWithInexistingSku() throws Exception {

        when(bookingService.getBookingBySku(1234567L)).thenReturn(ResponseEntity.notFound().build());

        this.mockMvc
                .perform(
                        get("/api/bookings?bookingSku={bookingSku}", 1234567)
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldDeleteAnBookingBySku() throws Exception {
        Function function = new Function();
        function.setName("Admin");

        User user = new User();
        user.setFunction(function);

        Category category = new Category();
        category.setFeatures(List.of());

        City city = new City();

        Product product = new Product();
        product.setCity(city);
        product.setCategory(category);
        product.setImages(List.of());

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setProduct(product);
        booking.setIdBooking(1L);

        when(bookingService.deleteBookingBySku(eq(1234567L))).thenReturn(ResponseEntity.ok().build());

        this.mockMvc
                .perform(
                        get("/api/bookings?bookingSku={bookingSku}", 1234567)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldNotDeleteABookingWithInexistingSku() throws Exception {

        when(bookingService.deleteBookingBySku(eq(1234567L))).thenReturn(ResponseEntity.notFound().build());

        this.mockMvc
                .perform(
                        delete("/api/bookings?bookingSku={bookingSku}", 1234567)
                                .header("Authorization", token)
                                .with(csrf())

                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldNotDeleteABookingWithInvalidSku() throws Exception {

        this.mockMvc
                .perform(
                        delete("/api/bookings?bookingSku={bookingSku}", "abc")
                                .header("Authorization", token)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldGetBookingsByUserSku() throws Exception {
        BookingDTO bookingDTO = new BookingDTO();
        List<BookingDTO> bookings = new ArrayList<>();
        bookings.add(bookingDTO);

        when(bookingService.getByUserSku(anyLong())).thenReturn(ResponseEntity.ok(bookings));

        this.mockMvc
                .perform(
                        get("/api/bookings?userSku={userSku}", 1234567)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldNotGetBookingsByUserWithInexistingUser() throws Exception {

        when(bookingService.getByUserSku(anyLong())).thenReturn(ResponseEntity.notFound().build());

        this.mockMvc
                .perform(
                        get("/api/bookings?userSku={userSku}", 1234567)
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldGetBookingsByProductSku() throws Exception {
        BookingDTO bookingDTO = new BookingDTO();

        List<BookingDTO> bookings = new ArrayList<>();
        bookings.add(bookingDTO);

        when(bookingService.getByProductSku(anyLong())).thenReturn(ResponseEntity.ok(bookings));

        this.mockMvc
                .perform(
                        get("/api/bookings?productSku={productSku}", 1234567)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldNotGetBookingsByProductSkuWithInexistingProduct() throws Exception {

        when(bookingService.getByProductSku(anyLong())).thenReturn(ResponseEntity.notFound().build());

        this.mockMvc
                .perform(
                        get("/api/bookings?productSku={productSku}", 1234567)
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
