package org.ecommerce.ecommerce.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.ecommerce.ecommerce.models.Order;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class OrderResponse {
    @JsonProperty("id")
    private Long orderId;
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("full_name")
    private String fullName;
    private String email;
    @JsonProperty("phone_number")
    private String phoneNumber;
    private String address;
    private String note;
    private String status;
    @JsonProperty("order_date")
    private Date orderDate;
    @JsonProperty("total_money")
    private double totalMoney;
    @JsonProperty("shipping_method")
    private String shippingMethod;
    @JsonProperty("payment_method")
    private String paymentMethod;
    @JsonProperty("shipping_address")
    private String shippingAddress;
    @JsonProperty("shipping_date")
    private Date shipping_date;
    @JsonProperty("tracking_number")
    private String trackingNumber;
    @JsonProperty("is_active")
    private boolean active;
    @JsonProperty("order_details")
    private List<OrderDetaiResponse> orderDetailsResponses;

    public static OrderResponse fromOrder(Order order)
    {
        return OrderResponse.builder()
                .orderId(order.getId())
                .userId(order.getUser().getId())
                .fullName(order.getFullName())
                .email(order.getEmail())
                .phoneNumber(order.getPhoneNumber())
                .address(order.getAddress())
                .note(order.getNote())
                .status(order.getStatus())
                .orderDate(order.getOrderDate())
                .totalMoney(order.getTotalMoney())
                .shippingMethod(order.getShippingMethod())
                .paymentMethod(order.getPaymentMethod())
                .shippingAddress(order.getShippingAddress())
                .trackingNumber(order.getTrackingNumber())
                .shipping_date(order.getShippingDate())
                .active(order.isActive())
                .orderDetailsResponses(order.getOrderDetails().stream().map(OrderDetaiResponse::fromOrderDetail).toList())
                .build();
    }


}
