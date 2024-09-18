package org.ecommerce.ecommerce.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.ecommerce.ecommerce.models.*;
import org.ecommerce.ecommerce.services.impl.ProductService;
import org.hibernate.engine.jdbc.Size;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder

public class ProductResponse extends BaseResponse {
    private Long id;
    @JsonProperty("name")
    private String name;
    private String description;
    private double price;
    private String thumbnail;
    @JsonProperty("created_at")
    private LocalDateTime createAt;
    @JsonProperty("category_id")
    private Long categoryId;
    @JsonProperty("classify_color_id")
    private Long classifyColorId;
    @JsonProperty("product_images")
    private List<ProductImage> productImages = new ArrayList<>();
    @JsonProperty("product_sale")
    private ProductSale sale;
    @JsonProperty("code_color")
    private String codeColor;

    @JsonProperty("product_sizes")
    private List<ProductSize> productSizes = new ArrayList<>();
    public static ProductResponse fromProduct(Product product) {

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .thumbnail(product.getThumbnail())
                .createAt(product.getCreatedAt())
                .categoryId(product.getCategory().getId())
                .classifyColorId(product.getClassifyColor().getId())
                .productImages(product.getProductImages())
                .sale(product.getSale())
                .codeColor(product.getCodeColor())
                .productSizes(product.getProductSizes())
                .build();
    }
}
