package org.ecommerce.ecommerce.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "fullname",length = 100)
    private String fullName;
    @Column(name = "email",length = 100)
    private String email;
    @Column(name = "phone_number",length = 20)
    private String phoneNumber;
    @Column(name = "address",length = 200)
    private String address;
    @Column(name = "note",length = 100)
    private String note;
    @Column(name = "order_date")
    private Date orderDate;
    @Column(name = "status",length = 20)
    private String status;
    @Column(name = "total_money")
    private double totalMoney;
    @Column(name = "shipping_method",length = 100)
    private String shippingMethod;
    @Column(name = "payment_method",length = 100)
    private String paymentMethod;
    @Column(name = "shipping_address",length = 200)
    private String shippingAddress;
    @Column(name = "shipping_date")
    private Date shippingDate;
    @Column(name = "tracking_number",length = 100)
    private String trackingNumber;
    @Column(name = "active",columnDefinition = "TINYINT(1)",nullable = false)
    private boolean active;
    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<OrderDetail> orderDetails;

}
