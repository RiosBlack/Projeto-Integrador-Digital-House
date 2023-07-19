package com.grupo7.renthotels.service;

import com.grupo7.renthotels.model.Function;
import com.grupo7.renthotels.model.dto.FunctionDTO;
import com.grupo7.renthotels.repository.FunctionRepository;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class FunctionServiceTest {

    @Mock
    private FunctionRepository functionRepository;

    @InjectMocks
    private FunctionService functionService;

    @Test
    public void shouldSaveAFunction(){
        FunctionDTO functionDTO = new FunctionDTO();
        functionDTO.setFunctionName("Admin");

        when(functionRepository.save(any(Function.class))).then(returnsFirstArg());

        var response = functionService.saveFunction(functionDTO);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody().getFunctionName()).isEqualTo("Admin");
    }

    @Test
    public void shouldGetAnEmptyListOfFunctions(){

        when(functionRepository.findAll()).thenReturn(List.of());

        var response = functionService.getAllFunctions();

        assertThat(response.isEmpty());
    }

    @Test
    public void shouldGetAnListOfAllRegisteredFunctions(){
        Function function1 = new Function();
        Function function2 = new Function();

        List<Function> functions = new ArrayList<>();
        functions.add(function1);
        functions.add(function2);

        when(functionRepository.findAll()).thenReturn(functions);

        var response = functionService.getAllFunctions();

        assertThat(response.size()).isEqualTo(2);
    }

    @Test
    public void shouldGetAFunctionById(){
        Function function = new Function();
        function.setIdFunction(1L);

        when(functionRepository.findById(1L)).thenReturn(Optional.of(function));

        var response = functionService.getFunctionById(1L);

        assertThat(response.getStatusCode()).isEqualTo(OK);
    }

    @Test
    public void shouldNotGetAFunctionWithInexistingId(){
        when(functionRepository.findById(1L)).thenReturn(Optional.empty());

        var response = functionService.getFunctionById(1L);

        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
    }

    @Test
    public void shouldGetAFunctionByName(){
        Function function = new Function();
        function.setName("Admin");

        when(functionRepository.findByName("Admin")).thenReturn(Optional.of(function));

        var response = functionService.getFunctionByName("Admin");

        assertThat(response.getStatusCode()).isEqualTo(OK);
    }

    @Test
    public void shouldNotGetAFunctionWithInexistingName(){
        when(functionRepository.findByName("Admin")).thenReturn(Optional.empty());

        var response = functionService.getFunctionByName("Admin");

        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
    }

    @Test
    public void shouldEditAFunctionById(){
        Function function = new Function(1L, 1234567L, "Admin");

        when(functionRepository.findById(1L)).thenReturn(Optional.of(function));

        when(functionRepository.save(argThat(functionToSave -> functionToSave.getIdFunction().equals(1L)))).then(returnsFirstArg());

        FunctionDTO newParams = new FunctionDTO(1234567L, "REGULAR_USER");

        var updatedFunction = functionService.updateFunction(1L, newParams);
        assertThat(updatedFunction.getStatusCode()).isEqualTo(OK);
        assertThat(updatedFunction.getBody()).hasFieldOrPropertyWithValue("functionName", "REGULAR_USER");
    }

    @Test
    public void shouldNotEditAFunctionWithInexistingId(){
        when(functionRepository.findById(1L)).thenReturn(Optional.empty());

        var response = functionService.updateFunction(1L, null);

        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
    }

    @Test
    public void shouldDeleteAFunctionById(){
        Function function = new Function();
        function.setIdFunction(1L);

        when(functionRepository.findById(1L)).thenReturn(Optional.of(function));

        var response = functionService.deleteFunction(1L);

        assertThat(response.getStatusCode()).isEqualTo(NO_CONTENT);
    }

    @Test
    public void shouldNotDeleteAFunctionWithInexistingId(){
        when(functionRepository.findById(1L)).thenReturn(Optional.empty());

        var response = functionService.deleteFunction(1L);

        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
    }

    @Test
    public void shouldGetAFunctionBySku(){
        Function function = new Function();
        function.setFunctionSku(1234567L);

        when(functionRepository.findByFunctionSku(1234567L)).thenReturn(Optional.of(function));

        var response = functionService.getFunctionBySku(1234567L);

        assertThat(response.getStatusCode()).isEqualTo(OK);
    }

    @Test
    public void shouldNotGetAFunctionWithInexistingSku(){
        when(functionRepository.findByFunctionSku(1234567L)).thenReturn(Optional.empty());

        var response = functionService.getFunctionBySku(1234567L);

        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
    }

    @Test
    public void shouldEditAFunctionBySku(){
        Function function = new Function(1L, 1234567L, "Admin");

        when(functionRepository.findByFunctionSku(1234567L)).thenReturn(Optional.of(function));

        when(functionRepository.save(argThat(functionToSave -> functionToSave.getFunctionSku().equals(1234567L)))).then(returnsFirstArg());

        FunctionDTO newParams = new FunctionDTO(1234567L, "REGULAR_USER");

        var updatedFunction = functionService.updateFunctionBySku(1234567L, newParams);
        assertThat(updatedFunction.getStatusCode()).isEqualTo(OK);
        assertThat(updatedFunction.getBody()).hasFieldOrPropertyWithValue("functionName", "REGULAR_USER");
    }

    @Test
    public void shouldNotEditAFunctionWithInexistingISku(){
        when(functionRepository.findByFunctionSku(1234567L)).thenReturn(Optional.empty());

        var response = functionService.updateFunctionBySku(1234567L, null);

        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
    }

    @Test
    public void shouldDeleteAFunctionBySku(){
        Function function = new Function();
        function.setFunctionSku(1234567L);

        when(functionRepository.findByFunctionSku(1234567L)).thenReturn(Optional.of(function));

        var response = functionService.deleteFunctionBySku(1234567L);

        assertThat(response.getStatusCode()).isEqualTo(NO_CONTENT);
    }

    @Test
    public void shouldNotDeleteAFunctionWithInexistingSku(){
        when(functionRepository.findByFunctionSku(1234567L)).thenReturn(Optional.empty());

        var response = functionService.deleteFunctionBySku(1234567L);

        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
    }
}
