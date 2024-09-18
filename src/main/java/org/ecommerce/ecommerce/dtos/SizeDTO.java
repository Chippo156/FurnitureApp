package org.ecommerce.ecommerce.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class SizeDTO {
    private String size;
    @JsonProperty("price_size")
    private double priceSize;
    @JsonProperty("product_id")
    private Long productId;
}
