package com.grupo7.renthotels.controller;

import com.grupo7.renthotels.model.dto.FunctionDTO;
import com.grupo7.renthotels.service.FunctionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/functions")
public class FunctionController {

    @Autowired
    private FunctionService functionService;

    @PostMapping
    public ResponseEntity<FunctionDTO> createFunction(@RequestBody @Valid FunctionDTO functionDTO) {
        return functionService.saveFunction(functionDTO);
    }

    @GetMapping
    public List<FunctionDTO> getAllFunctions() {
        return functionService.getAllFunctions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FunctionDTO> getFunctionById(@PathVariable Long id) {
        return functionService.getFunctionById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FunctionDTO> updateFunction(@PathVariable Long id, @RequestBody @Valid FunctionDTO functionDTO) {
        return functionService.updateFunction(id, functionDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFunction(@PathVariable Long id) {
        return functionService.deleteFunction(id);
    }

    @GetMapping(params = "functionName")
    public ResponseEntity<FunctionDTO> getFunctionByName(@RequestParam(value = "functionName") String functionName) {
        return functionService.getFunctionByName(functionName);
    }

    @GetMapping(params = "functionSku")
    public ResponseEntity<FunctionDTO> getFunctionBySku(@RequestParam(value = "functionSku") Long functionsku){
        return functionService.getFunctionBySku(functionsku);
    }

    @PutMapping(params = "functionSku")
    public ResponseEntity<FunctionDTO> updateFunctionSku(@RequestParam(value = "functionSku") Long functionSku, @RequestBody @Valid FunctionDTO functionDTO) {
        return functionService.updateFunctionBySku(functionSku, functionDTO);
    }

    @DeleteMapping(params = "functionSku")
    public ResponseEntity<Void> deleteFunctionBySku(@RequestParam(value = "functionSku") Long functionSku) {
        return functionService.deleteFunctionBySku(functionSku);
    }

}
