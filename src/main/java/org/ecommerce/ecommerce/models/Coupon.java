package org.ecommerce.ecommerce.models;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "coupons")
public class Coupon {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    @Column(name = "code",length = 100)
    private String code;
    private double discount;
    @Column(name = "discount_type",length = 100)
    private String discountType;
    @Column(name = "expiration_date")
    private Date expirationDate;

}
