package org.ecommerce.ecommerce.dtos;

import lombok.*;

@Getter
@Setter
@ToString
public class PaymentDTO {

    @Builder
    public static class VNPayResponse{
        public String code;
        public String message;
        public String paymentUrl;
    }
}
