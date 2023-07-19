package com.grupo7.renthotels.service;

import com.grupo7.renthotels.exception.NotFoundException;
import com.grupo7.renthotels.exception.UnprocessableEntityException;
import com.grupo7.renthotels.model.Booking;
import com.grupo7.renthotels.model.Category;
import com.grupo7.renthotels.model.City;
import com.grupo7.renthotels.model.Image;
import com.grupo7.renthotels.model.Policy;
import com.grupo7.renthotels.model.Product;
import com.grupo7.renthotels.model.dto.CategoryDTO;
import com.grupo7.renthotels.model.dto.CityDTO;
import com.grupo7.renthotels.model.dto.ImageDTO;
import com.grupo7.renthotels.model.dto.PolicyDTO;
import com.grupo7.renthotels.model.dto.ProductDTO;
import com.grupo7.renthotels.repository.BookingRepository;
import com.grupo7.renthotels.repository.CategoryRepository;
import com.grupo7.renthotels.repository.CityRepository;
import com.grupo7.renthotels.repository.ImageRepository;
import com.grupo7.renthotels.repository.PolicyRepository;
import com.grupo7.renthotels.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    private PolicyRepository policyRepository;

    @Autowired
    TokenService tokenService;

    public ResponseEntity<ProductDTO> saveProduct(ProductDTO productDTO, String token) throws NotFoundException {
        if (!tokenService.isValidToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Category category = categoryRepository.findBySku(productDTO.getCategory().getSku())
                .orElseThrow(() -> new NotFoundException("Categoria com o Sku: " + productDTO.getCategory().getSku() + " não existe"));

        City city = cityRepository.findByCitySku(productDTO.getCity().getCitySku())
                .orElseThrow(() -> new NotFoundException("Cidade com o Sku: " + productDTO.getCity().getCitySku() + " não existe"));

        Product product = productDTO.toEntity();

        List<Image> images = product.getImages();
        if(images == null)
            return ResponseEntity.badRequest().build();
        else {
            for(Image image : images){
                image.createImageSku();
                image.setProduct(product);
                image.setImageTitle("Imagem de produto " + productDTO.getProductTitle());
            }
        }

        List<Policy> policies = product.getPolicies();
        if(policies == null)
            return ResponseEntity.badRequest().build();
        else {
            for(Policy policy: policies){
                policy.createPolicySku();
                policy.setProduct(product);
            }
        }

        product.createSku();
        product.setCategory(category);
        product.setCity(city);
        productRepository.save(product);
        return ResponseEntity.ok(ProductDTO.from(product));
    }

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream().map(ProductDTO::from).collect(Collectors.toList());
    }

    public ResponseEntity<ProductDTO> getProductById(Long id) {
        return productRepository.findById(id)
                .map(ProductDTO::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<ProductDTO> updateProduct(Long id, ProductDTO productDTO) throws NotFoundException, UnprocessableEntityException {
        Optional<Product> existingProduct = productRepository.findById(id);
        if (existingProduct.isPresent()) {
            Product product = existingProduct.get();

            if (productDTO.getProductSku() != null) {
                product.setProductSku(productDTO.getProductSku());
            }
            if (productDTO.getProductTitle() != null) {
                product.setProductTitle(productDTO.getProductTitle());
            }
            if (productDTO.getProductDetails() != null) {
                product.setProductsDetails(productDTO.getProductDetails());
            }
            if (productDTO.getProductPrice() != null) {
                product.setProductPrice(productDTO.getProductPrice());
            }
            if (productDTO.getProductAddress() != null) {
                product.setProductAddress(productDTO.getProductAddress());
            }
            if (productDTO.getProductLatitude() != null) {
                product.setProductLatitude(productDTO.getProductLatitude());
            }
            if (productDTO.getProductLongitude() != null) {
                product.setProductLongitude(productDTO.getProductLongitude());
            }

            CategoryDTO categoryDTO = productDTO.getCategory();
            if (categoryDTO != null) {
                Long categorySku = categoryDTO.getSku();
                if (categorySku != null) {
                    Category category = categoryRepository.findBySku(categorySku)
                            .orElseThrow(() -> new NotFoundException("Categoria com o Sku: " + categorySku + " não existe"));
                    product.setCategory(category);
                }
            }

            CityDTO cityDTO = productDTO.getCity();
            if (cityDTO != null) {
                Long citySku = cityDTO.getCitySku();
                if (citySku != null) {
                    City city = cityRepository.findByCitySku(citySku)
                            .orElseThrow(() -> new NotFoundException("Cidade com o Sku: " + citySku + " não existe"));
                    product.setCity(city);
                }
            }

            List<Image> existingImages = product.getImages();
            List<ImageDTO> newImagesDTO = productDTO.getImages();
            if (newImagesDTO != null) {
                for (ImageDTO imageDTO : newImagesDTO) {
                    Long imageSku = imageDTO.getImageSku();
                    if (imageSku != null) {
                        Optional<Image> existingImage = existingImages.stream()
                                .filter(image -> image.getImageSku().equals(imageSku))
                                .findFirst();
                        if (existingImage.isPresent()) {
                            Image image = existingImage.get();
                            if (imageDTO.getImageTitle() != null) {
                                image.setImageTitle(imageDTO.getImageTitle());
                            }
                            if (imageDTO.getImageUrl() != null) {
                                image.setImageUrl(imageDTO.getImageUrl());
                            }
                            imageRepository.save(image);
                        } else {
                            throw new NotFoundException("Imagem com o Sku fornecido não foi encontrada: " + imageDTO.getImageSku());
                        }
                    } else {
                        throw new UnprocessableEntityException("O Sku da imagem não pode ser nulo");
                    }
                }
            }
            List<Policy> existingPolicies = product.getPolicies();
            List<PolicyDTO> newPoliciesDTO = productDTO.getPolicies();
            if (newPoliciesDTO != null) {
                for (PolicyDTO policyDTO : newPoliciesDTO) {
                    Long policySku = policyDTO.getPolicySku();
                    if (policySku != null) {
                        Optional<Policy> existingPolicy = existingPolicies.stream()
                                .filter(policy -> policy.getPolicySku().equals(policySku))
                                .findFirst();
                        if (existingPolicy.isPresent()) {
                            Policy policy = existingPolicy.get();
                            if (policyDTO.getPolicyHotel1() != null) {
                                policy.setPolicyHotel1(policyDTO.getPolicyHotel1());
                            }

                            if (policyDTO.getPolicyHotel2() != null) {
                                policy.setPolicyHotel2(policyDTO.getPolicyHotel2());
                            }

                            if (policyDTO.getPolicyHotel3() != null) {
                                policy.setPolicyHotel3(policyDTO.getPolicyHotel3());
                            }

                            policyRepository.save(policy);
                        } else {
                            throw new NotFoundException("Política com o SKU fornecido não foi encontrada: " + policyDTO.getPolicySku());
                        }
                    } else {
                        throw new UnprocessableEntityException("O Sku da politica não pode ser nulo");
                    }
                }
            }
            Product updatedProduct = productRepository.save(product);
            return ResponseEntity.ok(ProductDTO.from(updatedProduct));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Void> deleteProduct(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            productRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public List<ProductDTO> findByAvailability(LocalDate bookingStartDate, LocalDate bookingEndDate) {
        List<Booking> bookings = bookingRepository.findAllByBookingStartDateBetween(bookingStartDate, bookingEndDate);
        List<ProductDTO> products = getAllProducts();
        List<ProductDTO> productsAvailable = new ArrayList<>();
        for (ProductDTO productDTO: products) {
            if (!(productsAvailable.stream().anyMatch(productAvailable -> productAvailable.getProductSku() == productDTO.getProductSku()))) {
                productsAvailable.add(productDTO);
            }
        }

        if (!bookings.isEmpty()) {
            products.forEach(product -> {
                bookings.forEach(booking -> {
                     if (booking.getProduct().getProductSku() == product.getProductSku()) {
                         productsAvailable.remove(product);
                    }
                });
            });
        }
        return productsAvailable;
    }

    public ResponseEntity<List<ProductDTO>> findByCityDenominationAndAvailability (String cityDenomination, LocalDate bookingStartDate, LocalDate bookingEndDate) {

        List<ProductDTO> availableProducts = findByAvailability(bookingStartDate, bookingEndDate);
        List<ProductDTO> availableProductsByCity = new ArrayList<>();

            availableProducts.forEach(product -> {
                if(product.getCity().getCityDenomination().compareTo(cityDenomination) == 0) {
                    availableProductsByCity.add(product);
                }
            });
            return ResponseEntity.ok(availableProductsByCity);
    }

    public ResponseEntity<List<ProductDTO>> findByCategoryKind(String kind) {
        List<Product> products = productRepository.getByCategoryKind(kind);
        List<ProductDTO> productDTOs = products.stream()
                .map(ProductDTO::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDTOs);
    }

    public ResponseEntity<List<ProductDTO>> findByCityDenomination(String cityDenomination) {
        List<Product> products = productRepository.getByCityCityDenomination(cityDenomination);
        List<ProductDTO> productDTOs =products.stream()
                .map(ProductDTO::from)
                .collect(Collectors.toList());
        return  ResponseEntity.ok(productDTOs);
    }

    public ResponseEntity<ProductDTO> getProductBySku(Long sku) {
        return productRepository.findByProductSku(sku)
                .map(ProductDTO::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<ProductDTO> updateProductBySku(Long sku, ProductDTO productDTO) {
        Optional<Product> existingProduct = productRepository.findByProductSku(sku);
        if(existingProduct.isPresent()){
            Product product = productDTO.toEntity();
            product.setIdProduct(existingProduct.get().getIdProduct());
            product.setProductSku(existingProduct.get().getProductSku());
            product.setCategory(existingProduct.get().getCategory());
            product.setCity(existingProduct.get().getCity());
            product.setImages(existingProduct.get().getImages());
            product.setPolicies(existingProduct.get().getPolicies());
            Product updatedProduct = productRepository.save(product);
            return ResponseEntity.ok(ProductDTO.from(updatedProduct));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Void> deleteProductBySku(Long sku){
        Optional<Product> product = productRepository.findByProductSku(sku);
        if(product.isPresent()){
            productRepository.deleteById(product.get().getIdProduct());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
