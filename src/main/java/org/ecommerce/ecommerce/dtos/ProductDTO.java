package org.ecommerce.ecommerce.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.ecommerce.ecommerce.models.ProductSize;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ProductDTO {
    @JsonProperty("product_name")
    @NotBlank
    @Size(min = 3, max = 300,message = "Product name must be between 3 and 300 characters")
    private String productName;
    private double price;
    private String thumbnail;
    private String description;
    private ProductSize size;
    private String codeColor;
    @JsonProperty("category_id")
    private Long categoryId;
    @JsonProperty("sale_id")
    private Long saleId;
}
