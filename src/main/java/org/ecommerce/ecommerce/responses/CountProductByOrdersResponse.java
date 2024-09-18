package org.ecommerce.ecommerce.responses;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CountProductByOrdersResponse {
    private Long productId;
    private Long count;
}
