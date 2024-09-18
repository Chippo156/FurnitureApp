package org.ecommerce.ecommerce.services.impl;

import lombok.RequiredArgsConstructor;
import org.ecommerce.ecommerce.models.Coupon;
import org.ecommerce.ecommerce.repository.CouponRepository;
import org.ecommerce.ecommerce.responses.CouponResponse;
import org.ecommerce.ecommerce.services.iCouponService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponService implements iCouponService
{
    private final CouponRepository couponRepository;


    @Override
    public Coupon getCouponByCode(String code) {
        return couponRepository.findByCode(code);
    }

    @Override
    public List<CouponResponse> getAllCoupons() {
        return couponRepository.findAllByExpiration().stream().map(CouponResponse::from).toList();
    }
}
