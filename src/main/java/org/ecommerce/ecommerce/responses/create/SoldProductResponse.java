package org.ecommerce.ecommerce.responses.create;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.ecommerce.ecommerce.models.Product;
import org.ecommerce.ecommerce.responses.ProductResponse;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SoldProductResponse {
    @JsonProperty("product")
    private ProductResponse productResponse;
    private int quantity;
}
