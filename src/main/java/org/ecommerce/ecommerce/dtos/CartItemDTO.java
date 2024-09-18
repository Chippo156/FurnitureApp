package org.ecommerce.ecommerce.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CartItemDTO {

    @JsonProperty("product_id")
    private Long productId;
    private int quantity;
}
