package com.grupo7.renthotels.controller;

import com.grupo7.renthotels.model.City;
import com.grupo7.renthotels.model.dto.CityDTO;
import com.grupo7.renthotels.security.SpringSecurityWebAuxTestConfig;
import com.grupo7.renthotels.service.CityService;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(CityController.class)
@Import(CityController.class)
@ContextConfiguration(classes = SpringSecurityWebAuxTestConfig.class)
public class CityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CityService cityService;

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
    public void shouldGetAEmptyListOfCities() throws Exception {
        when(cityService.getAllCities()).thenReturn(Collections.emptyList());

        this.mockMvc
                .perform(
                        get("/api/cities")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[]"));
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldGetAListOfAllRegisteredCities() throws Exception {
        CityDTO cityDTO = new CityDTO();

        when(cityService.getAllCities()).thenReturn(List.of(cityDTO));

        this.mockMvc
                .perform(
                        get("/api/cities")
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldGetARegisteredCityById() throws Exception {
        CityDTO city = new CityDTO();

        when(cityService.getCityById(eq(5L))).thenReturn(ResponseEntity.ok(city));

        this.mockMvc
                .perform(
                        get("/api/cities/{id}", 1L)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldNotGetACityWithInexistingId() throws Exception {

        when(cityService.getCityById(anyLong())).thenReturn(ResponseEntity.notFound().build());

        this.mockMvc
                .perform(
                        get("/api/cities/{id}", 1)
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldNotGetACityWithAnInvalidId() throws Exception {
        this.mockMvc
                .perform(
                        get("/api/cities/{id}", "abc")
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldRegisterACity() throws Exception {

        CityDTO cityDTO = new CityDTO();

        when(cityService.saveCity(any(CityDTO.class), anyString()))
                .thenReturn(ResponseEntity.ok(cityDTO));

        this.mockMvc
                .perform(
                        post("/api/cities")
                                .header("Authorization", token)
                                .with(csrf())
                                .content("{" +
                                        "  \"cityDenomination\": \"Floripa\",\n" +
                                        "  \"cityCountry\": \"Brasil\" \n" +
                                        "}")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldNotEditACityWithAnInexistingId() throws Exception {

        when(cityService.updateCity(1L, new CityDTO())).thenReturn(ResponseEntity.notFound().build());

        this.mockMvc
                .perform(
                        put("/api/cities/{id}", 1L)
                                .with(csrf())
                                .content("{}")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldEditAnRegisteredCity() throws Exception {

        CityDTO cityDto = new CityDTO(1234567L,"Floripa", "Brasil");

        when(cityService.updateCity(2L, cityDto)).thenReturn(ResponseEntity.ok(cityDto));

        this.mockMvc
                .perform(
                        put("/api/cities/{id}", 2L)
                                .with(csrf())
                                .content("{" +
                                        "  \"citySku\": 1234567,\n" +
                                        "  \"cityDenomination\": \"Floripa\",\n" +
                                        "  \"cityCountry\": \"Brasil\" \n" +
                                        "}")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.citySku").value(1234567))
                .andExpect(jsonPath("$.cityDenomination").value("Floripa"))
                .andExpect(jsonPath("$.cityCountry").value("Brasil"));
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldGetARegisteredCityByDenomination() throws Exception {
        CityDTO city = new CityDTO();

        when(cityService.findByCityDenomination("Sasalandia")).thenReturn(ResponseEntity.ok(List.of(city)));

        this.mockMvc
                .perform(
                        get("/api/cities?cityDenomination={cityDenomination}", "Sasalandia")
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldNotGetCityWithNotRegisteredDenomination() throws Exception {

        when(cityService.findByCityDenomination("abc")).thenReturn((ResponseEntity.notFound().build()));

        this.mockMvc
                .perform(
                        get("/api/cities?cityDenomination={cityDenomination}", "abc")
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @WithUserDetails("user@test.com")
    @Test
    public void shouldNotDeleteACityWithAnInexistingId() throws Exception {

        when(cityService.deleteCity(eq(1L))).thenReturn(ResponseEntity.notFound().build());

        this.mockMvc
                .perform(
                        delete("/api/cities/{id}", 1)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @WithUserDetails("user@test.com")
    @Test
    public void shouldDeleteACityWithValidId() throws Exception {

        City city = new City();
        city.setIdCity(4L);

        when(cityService.deleteCity(eq(4L))).thenReturn(ResponseEntity.ok().build());

        this.mockMvc
                .perform(
                        delete("/api/cities/{id}", 4)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithUserDetails("user@test.com")
    @Test
    public void shouldNotDeleteACityWithAnInvalidId() throws Exception {

        this.mockMvc
                .perform(
                        delete("/api/cities/{id}", "abc")
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @WithUserDetails("user@test.com")
    @Test
    public void shouldGetARegisteredCityBySku() throws Exception {
        CityDTO city = new CityDTO();

        when(cityService.getCityBySku(eq(1234567L))).thenReturn(ResponseEntity.ok(city));

        this.mockMvc
                .perform(
                        get("/api/cities?citySku={sku}", 1234567L)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithUserDetails("user@test.com")
    @Test
    public void shouldNotGetACityWithInexistingSku() throws Exception {

        when(cityService.getCityBySku(anyLong())).thenReturn(ResponseEntity.notFound().build());

        this.mockMvc
                .perform(
                        get("/api/cities?citySku={sku}", 1234567)
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @WithUserDetails("user@test.com")
    @Test
    public void shouldNotGetACityWithAnInvalidSku() throws Exception {
        this.mockMvc
                .perform(
                        get("/api/cities?citySku={sku}", "abc")
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @WithUserDetails("user@test.com")
    @Test
    public void shouldNotEditACityWithAnInexistingSku() throws Exception {

        when(cityService.updateCityBySku(1234567L, new CityDTO())).thenReturn(ResponseEntity.notFound().build());

        this.mockMvc
                .perform(
                        put("/api/cities?citySku={sku}", 1234567L)
                                .with(csrf())
                                .content("{}")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @WithUserDetails("user@test.com")
    @Test
    public void shouldEditAnRegisteredCityBySku() throws Exception {

        CityDTO cityDto = new CityDTO(1234567L,"Floripa", "Brasil");

        when(cityService.updateCityBySku(1234567L, cityDto)).thenReturn(ResponseEntity.ok(cityDto));

        this.mockMvc
                .perform(
                        put("/api/cities?citySku={sku}", 1234567L)
                                .with(csrf())
                                .content("{" +
                                        "  \"citySku\": 1234567,\n" +
                                        "  \"cityDenomination\": \"Floripa\",\n" +
                                        "  \"cityCountry\": \"Brasil\" \n" +
                                        "}")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.citySku").value(1234567))
                .andExpect(jsonPath("$.cityDenomination").value("Floripa"))
                .andExpect(jsonPath("$.cityCountry").value("Brasil"));
    }

    @WithUserDetails("user@test.com")
    @Test
    public void shouldNotDeleteACityWithAnInexistingSku() throws Exception {

        when(cityService.deleteCityBySku(eq(1234567L))).thenReturn(ResponseEntity.notFound().build());

        this.mockMvc
                .perform(
                        delete("/api/cities?citySku={sku}", 1234567)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @WithUserDetails("user@test.com")
    @Test
    public void shouldDeleteACityBySku() throws Exception {

        City city = new City();
        city.setCitySku(1234567L);

        when(cityService.deleteCityBySku(eq(1234567L))).thenReturn(ResponseEntity.ok().build());

        this.mockMvc
                .perform(
                        delete("/api/cities?citySku={sku}", 1234567)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithUserDetails("user@test.com")
    @Test
    public void shouldNotDeleteACityWithAnInvalidSku() throws Exception {

        this.mockMvc
                .perform(
                        delete("/api/cities?citySku={sku}", "abc")
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
