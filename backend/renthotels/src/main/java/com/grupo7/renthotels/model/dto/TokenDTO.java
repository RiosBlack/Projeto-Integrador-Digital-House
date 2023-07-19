package com.grupo7.renthotels.model.dto;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TokenDTO {
    private String token;
    private Long userId;
    private Long functionId;
}
