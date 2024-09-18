package org.ecommerce.ecommerce.services;

import org.ecommerce.ecommerce.models.Coupon;
import org.ecommerce.ecommerce.responses.CouponResponse;

import java.util.List;

public interface iCouponService {
    Coupon getCouponByCode(String code);

    List<CouponResponse> getAllCoupons();


}
