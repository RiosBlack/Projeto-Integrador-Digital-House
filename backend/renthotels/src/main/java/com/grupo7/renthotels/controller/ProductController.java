package com.grupo7.renthotels.controller;

import com.grupo7.renthotels.exception.NotFoundException;
import com.grupo7.renthotels.exception.UnprocessableEntityException;
import com.grupo7.renthotels.model.dto.ProductDTO;
import com.grupo7.renthotels.service.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody @Valid ProductDTO productDTO, @RequestHeader("Authorization") String token) throws NotFoundException {
        return productService.saveProduct(productDTO, token);
    }

    @GetMapping
    public List<ProductDTO> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody @Valid ProductDTO productDTO) throws UnprocessableEntityException, NotFoundException {
        return productService.updateProduct(id, productDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        return productService.deleteProduct(id);
    }

    @GetMapping("/categories/{kind}")
    public ResponseEntity<List<ProductDTO>> getByKind(@PathVariable String kind) {
        return productService.findByCategoryKind(kind);
    }

    @GetMapping("/cities/{cityDenomination}")
    public ResponseEntity<List<ProductDTO>> getByCityDenomination(@PathVariable String cityDenomination) {
        return productService.findByCityDenomination(cityDenomination);
    }

    @GetMapping(params = "productSku")
    public ResponseEntity<ProductDTO> getProductBySku(@RequestParam(value = "productSku") Long sku) {
        return productService.getProductBySku(sku);
    }

    @GetMapping("/dates/{bookingStartDate}/{bookingEndDate}")
    public ResponseEntity<?> getProductsAvailability(@PathVariable("bookingStartDate") LocalDate bookingStartDate, @PathVariable("bookingEndDate") LocalDate bookingEndDate) {
        if(bookingStartDate == null || bookingEndDate == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Data Inicial ou Data Final não informada!");
        }

        List<ProductDTO> productsAvailable = productService.findByAvailability(bookingStartDate, bookingEndDate);

        return ResponseEntity.ok(productsAvailable);
    }

    @GetMapping("/city/{cityDenomination}/{bookingStartDate}/{bookingEndDate}")
    public ResponseEntity<?> getProductsByCityDenominationAndAvailability(@PathVariable("cityDenomination") String cityDenomination, @PathVariable("bookingStartDate") LocalDate bookingStartDate, @PathVariable("bookingEndDate") LocalDate bookingEndDate) {
        return productService.findByCityDenominationAndAvailability(cityDenomination, bookingStartDate, bookingEndDate);
    }

    @PutMapping(params = "productSku")
    public ResponseEntity<ProductDTO> updateProductBySku(@RequestParam(value = "productSku") Long sku, @RequestBody @Valid ProductDTO productDTO){
        return productService.updateProductBySku(sku, productDTO);
    }

    @DeleteMapping(params = "productSku")
    public ResponseEntity<Void> deleteProductBySku(@RequestParam(value = "productSku") Long sku){
        return productService.deleteProductBySku(sku);
    }
}
