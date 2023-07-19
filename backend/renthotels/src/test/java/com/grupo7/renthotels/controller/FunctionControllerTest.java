package com.grupo7.renthotels.controller;

import com.grupo7.renthotels.model.Function;
import com.grupo7.renthotels.model.dto.FunctionDTO;
import com.grupo7.renthotels.security.SpringSecurityWebAuxTestConfig;
import com.grupo7.renthotels.service.FunctionService;
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
@WebMvcTest(FunctionController.class)
@Import(FunctionController.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringSecurityWebAuxTestConfig.class)
public class FunctionControllerTest {

    @MockBean
    private FunctionService functionService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails("user@test.com")
    public void shouldGetAListOfFunctions() throws Exception {
        when(functionService.getAllFunctions()).thenReturn(Collections.emptyList());

        this.mockMvc
                .perform(
                        get("/api/functions")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldGetAnEmptyListOfFunctions() throws Exception {
        FunctionDTO functionDTO = new FunctionDTO();

        when(functionService.getAllFunctions()).thenReturn(List.of(functionDTO));

        this.mockMvc
                .perform(
                        get("/api/functions")
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldSaveAFunction() throws Exception {
        FunctionDTO functionDTO = new FunctionDTO();

        when(functionService.saveFunction(any(FunctionDTO.class))).thenReturn(ResponseEntity.ok(functionDTO));

        this.mockMvc
                .perform(
                        post("/api/functions")
                                .with(csrf())
                                .content("{" +
                                        "  \"functionName\": \"Admin\"\n" +
                                        "}")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldGetAFunctionById() throws Exception {
        FunctionDTO functionDTO = new FunctionDTO();

        when(functionService.getFunctionById(eq(1L))).thenReturn(ResponseEntity.ok(functionDTO));

        this.mockMvc
                .perform(
                        get("/api/functions/{id}", 1)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldNotGetAFunctionWithInexistingId() throws Exception {

        when(functionService.getFunctionById(eq(1L))).thenReturn(ResponseEntity.notFound().build());

        this.mockMvc
                .perform(
                        get("/api/functions/{id}", 1)
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldNotGetAFunctionWithInvalidId() throws Exception {

        this.mockMvc
                .perform(
                        get("/api/functions/{id}", "abc")
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldGetAFunctionByName() throws Exception {

        FunctionDTO functionDTO = new FunctionDTO();

        when(functionService.getFunctionByName("Admin")).thenReturn(ResponseEntity.ok(functionDTO));

        this.mockMvc
                .perform(
                        get("/api/functions?functionName={functionName}", "Admin")
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldNotGetAFunctionWithInexistingName() throws Exception {

        when(functionService.getFunctionByName("Admin")).thenReturn(ResponseEntity.notFound().build());

        this.mockMvc
                .perform(
                        get("/api/functions?functionName={functionName}", "Admin")
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldEditAFunctionById() throws Exception {

        FunctionDTO functionDTO = new FunctionDTO(1234567L, "Admin");

        when(functionService.updateFunction(eq(1L), any(FunctionDTO.class))).thenReturn(ResponseEntity.ok(functionDTO));

        this.mockMvc
                .perform(
                        put("/api/functions/{id}", 1)
                                .with(csrf())
                                .content("{" +
                                        "  \"functionName\": \"Admin\"\n" +
                                        "}")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.functionName").value("Admin"));
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldNotEditAFunctionWithInexistingId() throws Exception {

        when(functionService.updateFunction(eq(1L), any(FunctionDTO.class))).thenReturn(ResponseEntity.notFound().build());

        this.mockMvc
                .perform(
                        put("/api/functions/{id}", 1)
                                .with(csrf())
                                .content("{}")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldDeleteAFunctionById() throws Exception {
        Function function = new Function();
        function.setIdFunction(1L);

        when(functionService.deleteFunction(1L)).thenReturn(ResponseEntity.ok().build());

        this.mockMvc
                .perform(
                delete("/api/functions/{id}", 1)
                        .with(csrf())
        )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldNotDeleteAFuntionWithInexistingId() throws Exception {

        when(functionService.deleteFunction(1L)).thenReturn(ResponseEntity.notFound().build());

        this.mockMvc
                .perform(
                        delete("/api/functions/{id}", 1)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldNotDeleteAFuntionWithInvalidId() throws Exception {

        this.mockMvc
                .perform(
                        delete("/api/functions/{id}", "abc")
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldGetAFunctionBySku() throws Exception {
        FunctionDTO functionDTO = new FunctionDTO();

        when(functionService.getFunctionBySku(eq(1234567L))).thenReturn(ResponseEntity.ok(functionDTO));

        this.mockMvc
                .perform(
                        get("/api/functions?functionSku={functionSku}", 1234567)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldNotGetAFunctionWithInexistingSku() throws Exception {

        when(functionService.getFunctionBySku(1234567L)).thenReturn(ResponseEntity.notFound().build());

        this.mockMvc
                .perform(
                        get("/api/functions?functionSku={functionSku}", 1234567)
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldNotGetAFunctionWithInvalidSku() throws Exception {
        this.mockMvc
                .perform(
                        get("/api/functions?functionSku={functionSku}", "ABC")
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldEditAFunctionBySku() throws Exception {
        FunctionDTO functionDTO = new FunctionDTO(1234567L, "Admin");

        when(functionService.updateFunctionBySku(eq(1234567L), any(FunctionDTO.class))).thenReturn(ResponseEntity.ok(functionDTO));

        this.mockMvc
                .perform(
                        put("/api/functions?functionSku={functionSku}", 1234567)
                                .with(csrf())
                                .content("{" +
                                        "  \"functionName\": \"Admin\"\n" +
                                        "}")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.functionName").value("Admin"));
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldNotEditAFunctionWithInexistingSku() throws Exception {

        when(functionService.updateFunctionBySku(eq(1234567L), any(FunctionDTO.class))).thenReturn(ResponseEntity.notFound().build());

        this.mockMvc
                .perform(
                        put("/api/functions?functionSku={functionSku}", 1234567)
                                .with(csrf())
                                .content("{}")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldDeleteAFunctionBySku() throws Exception {
        Function function = new Function();

        when(functionService.deleteFunctionBySku(1234567L)).thenReturn(ResponseEntity.ok().build());

        this.mockMvc
                .perform(
                        delete("/api/functions?functionSku={functionSku}", 1234567)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldNotDeleteAFuntionWithInexistingSku() throws Exception {

        when(functionService.deleteFunctionBySku(1234567L)).thenReturn(ResponseEntity.notFound().build());

        this.mockMvc
                .perform(
                        delete("/api/functions?functionSku={functionSku}", 1234567)
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    @WithUserDetails("user@test.com")
    public void shouldNotDeleteAFuntionWithInvalidSku() throws Exception {
        this.mockMvc
                .perform(
                        delete("/api/functions?functionSku={functionSku}", "ABC")
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
