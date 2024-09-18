package org.ecommerce.ecommerce.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.ecommerce.ecommerce.models.OrderDetail;
import org.ecommerce.ecommerce.models.Product;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class OrderDetaiResponse {


    private Long id;
    @JsonProperty("product")
    private ProductResponse productResponse;
    private double price;
    @JsonProperty("number_of_products")
    private int numberOfProducts;
    @JsonProperty("total_money")
    private double totalMoney;

    @JsonProperty("sub_total")
    private double subTotal;

    public static OrderDetaiResponse fromOrderDetail(OrderDetail orderDetail)
    {
        return OrderDetaiResponse.builder()
                .id(orderDetail.getId())
                .productResponse(ProductResponse.fromProduct(orderDetail.getProduct()))
                .price(orderDetail.getPrice())
                .numberOfProducts(orderDetail.getNumberOfProducts())
                .totalMoney(orderDetail.getTotalMoney())
                .subTotal(orderDetail.getSubtotal())
                .build();
    }
}
