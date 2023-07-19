package com.grupo7.renthotels.model.dto;

import com.grupo7.renthotels.model.Image;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImageDTO {
    private Long imageSku;
    private String imageTitle;
    private String imageUrl;

    public static ImageDTO from(Image image){
        return new ImageDTO(
                image.getImageSku(),
                image.getImageTitle(),
                image.getImageUrl());
    }

    public Image toEntity(){
        Image image = new Image();
        image.setImageSku(this.imageSku);
        image.setImageTitle(this.imageTitle);
        image.setImageUrl(this.imageUrl);
        return image;
    }

}
