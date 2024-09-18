package org.ecommerce.ecommerce.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class OrderDTO {

    @JsonProperty("full_name")
    private String fullName;
    private String address;
    @JsonProperty("phone_number")
    @NotBlank(message = "Phone number cannot be empty")
    private String phoneNumber;
    private String email;
    private String note;
    @JsonProperty("order_status")
    private String orderStatus;
    @JsonProperty("total_money")
    @Min(value=0, message = "Total money must be greater than or equal 0")
    private double totalMoney;
    @JsonProperty("user_id")
    @Min(value=1, message = "User ID must be greater than or equal 1")
    private Long userId;
    @JsonProperty("shipping_method")
    private String shippingMethod;
    @JsonProperty("payment_method")
    private String paymentMethod;
    @JsonProperty("shipping_address")
    private String shippingAddress;
    @JsonProperty("shipping_date")
    private Date shippingDate;
    @JsonProperty("tracking_number")
    private String trackingNumber;
    @JsonProperty("cart_items")
    private List<CartItemDTO> cartItems;

}
