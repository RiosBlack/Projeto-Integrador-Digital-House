package com.grupo7.renthotels.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="images")
@Getter
@Setter
@NoArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_image", nullable = false, unique = true)
    private Long idImage;

    @Column(name = "image_sku", nullable = false, unique = true)
    private Long imageSku;

    @Column(name = "image_title", length = 50, nullable = false)
    private String imageTitle;

    @Column(name = "image_url", length = 200)
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "id_product")
    private Product product;

    public void createImageSku() {
        long generated = (long) (Math.random() * 9999999L);
        this.imageSku = (generated < 0 ? generated * -1 : generated);
    }
    @Builder
    public Image(String imageUrl, Product product){
        this.imageUrl = imageUrl;
        this.product = product;
    }
}
