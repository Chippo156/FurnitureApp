package org.ecommerce.ecommerce.responses;

import lombok.*;

import java.util.List;
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ProductListResponse {
    private List<ProductResponse> productResponses;
    private int totalPage;
}
