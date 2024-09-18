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
public class OrderListResponse {
    private List<OrderResponse> orders;
    private int totalPage;
}
