package com.grupo7.renthotels.model.dto;

import com.grupo7.renthotels.model.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ProductDTO {
    private Long productSku;
    private String productTitle;
    private String productDetails;
    private Float productPrice;
    private String productAddress;
    private String productLatitude;
    private String productLongitude;
    private List<ImageDTO> images;
    private List<PolicyDTO> policies;

    private CategoryDTO category;

    private CityDTO city;

    public static ProductDTO from(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductSku(product.getProductSku());
        productDTO.setProductTitle(product.getProductTitle());
        productDTO.setProductDetails(product.getProductsDetails());
        productDTO.setProductPrice(product.getProductPrice());
        productDTO.setProductAddress(product.getProductAddress());
        productDTO.setProductLatitude(product.getProductLatitude());
        productDTO.setProductLongitude(product.getProductLongitude());
        productDTO.setImages(product.getImages().stream().map(ImageDTO::from).collect(Collectors.toList()));
        productDTO.setCategory(CategoryDTO.from(product.getCategory()));
        productDTO.setCity(CityDTO.from(product.getCity()));
        productDTO.setPolicies(product.getPolicies().stream().map(PolicyDTO::from).collect(Collectors.toList()));
        return productDTO;
    }

    public Product toEntity(){
        Product product = new Product();
        product.setProductSku(this.productSku);
        product.setProductTitle(this.productTitle);
        product.setProductsDetails(this.productDetails);
        product.setProductPrice(this.productPrice);
        product.setProductAddress(this.productAddress);
        product.setProductLatitude(this.productLatitude);
        product.setProductLongitude(this.productLongitude);
        product.setImages(this.images.stream().map(ImageDTO::toEntity).collect(Collectors.toList()));
        product.setCategory(this.category.toEntity());
        product.setCity(this.city.toEntity());
        product.setPolicies(this.policies.stream().map(PolicyDTO::toEntity).collect(Collectors.toList()));
        return product;
    }
}
