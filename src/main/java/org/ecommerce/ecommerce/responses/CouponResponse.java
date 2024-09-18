package org.ecommerce.ecommerce.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;
import org.ecommerce.ecommerce.models.Coupon;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CouponResponse {



    private String code;

    private double discount;

    @JsonProperty("discount_type")
    private String discountType;

    public static CouponResponse from(Coupon coupon) {
        return CouponResponse.builder()
                .code(coupon.getCode())
                .discount(coupon.getDiscount())
                .discountType(coupon.getDiscountType())
                .build();
    }


}
