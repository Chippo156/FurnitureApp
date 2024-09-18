package org.ecommerce.ecommerce.responses;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ProductRatingResponse {
    private Long product_id;
    private double rating;
    private Long evaluation;


}
