package com.grupo7.renthotels.service;

import com.grupo7.renthotels.model.Function;
import com.grupo7.renthotels.model.dto.FunctionDTO;
import com.grupo7.renthotels.repository.FunctionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FunctionService {

    @Autowired
    private FunctionRepository functionRepository;

    public ResponseEntity<FunctionDTO> saveFunction(FunctionDTO functionDTO) {
        Function function = functionDTO.toEntity();
        function.createFunctionSku();
        functionRepository.save(function);
        return ResponseEntity.ok(FunctionDTO.from(function));
    }

    public List<FunctionDTO> getAllFunctions() {
        return functionRepository.findAll()
                .stream()
                .map(FunctionDTO::from)
                .collect(Collectors.toList());
    }

    public ResponseEntity<FunctionDTO> getFunctionById(Long id) {
        return functionRepository.findById(id)
                .map(FunctionDTO::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<FunctionDTO> getFunctionByName(String name) {
        return functionRepository.findByName(name)
                .map(FunctionDTO::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<FunctionDTO> updateFunction(Long id, FunctionDTO functionDTO) {
        Optional<Function> existingFunction = functionRepository.findById(id);
        if (existingFunction.isPresent()) {
            Function function = functionDTO.toEntity();
            function.setIdFunction(existingFunction.get().getIdFunction());
            Function updateFunction = functionRepository.save(function);
            return ResponseEntity.ok(FunctionDTO.from(updateFunction));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Void> deleteFunction(Long id) {
        Optional<Function> function = functionRepository.findById(id);
        if (function.isPresent()) {
            functionRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<FunctionDTO> getFunctionBySku(Long functionSku) {
        return functionRepository.findByFunctionSku(functionSku)
                .map(FunctionDTO::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<FunctionDTO> updateFunctionBySku(Long sku, FunctionDTO functionDTO) {
        Optional<Function> existingFunction = functionRepository.findByFunctionSku(sku);
        if (existingFunction.isPresent()) {
            Function function = functionDTO.toEntity();
            function.setIdFunction(existingFunction.get().getIdFunction());
            function.setFunctionSku(existingFunction.get().getFunctionSku());
            Function updateFunction = functionRepository.save(function);
            return ResponseEntity.ok(FunctionDTO.from(updateFunction));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Void> deleteFunctionBySku(Long sku) {
        Optional<Function> function = functionRepository.findByFunctionSku(sku);
        if (function.isPresent()){
            functionRepository.deleteById(function.get().getIdFunction());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
