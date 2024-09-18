package org.ecommerce.ecommerce.repository;

import org.ecommerce.ecommerce.models.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Long>{
    Coupon findByCode(String code);

    @Query("SELECT c FROM Coupon c WHERE c.expirationDate >= CURRENT_DATE")
    List<Coupon> findAllByExpiration();

}
