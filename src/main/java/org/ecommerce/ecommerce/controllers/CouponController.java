package org.ecommerce.ecommerce.controllers;

import lombok.RequiredArgsConstructor;
import org.ecommerce.ecommerce.models.Coupon;
import org.ecommerce.ecommerce.responses.CouponResponse;
import org.ecommerce.ecommerce.services.impl.CouponService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/coupons")
@RestController
@RequiredArgsConstructor
public class CouponController {
    private final CouponService couponService;
    @GetMapping ("/code/{code}")
    public ResponseEntity<?> getCouponByCode(@PathVariable String code) {
        Coupon coupon = couponService.getCouponByCode(code);
        if(coupon == null)
        {
            return ResponseEntity.badRequest().body("Coupon not found");
        }
        return ResponseEntity.ok(coupon);
    }
    @GetMapping("")
    public ResponseEntity<?> getAllCoupons() {
        return ResponseEntity.ok(couponService.getAllCoupons());
    }
}
