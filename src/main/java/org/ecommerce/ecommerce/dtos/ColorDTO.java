package org.ecommerce.ecommerce.dtos;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ColorDTO {
    private String color;
    private String code;
    private Long productId;

}
