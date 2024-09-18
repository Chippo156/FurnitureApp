package org.ecommerce.ecommerce.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.ecommerce.ecommerce.models.Role;
import org.ecommerce.ecommerce.models.User;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("full_name")
    private String fullname;
    @JsonProperty("phone_number")
    private String phone;
    private String address;
    @JsonProperty("is_active")
    private boolean active;
    private Role role;
    @JsonProperty("date_of_birth")
    private Date dateOfbirth;
    @JsonProperty("facebook_account_id")
    private int facebookAccountId;
    @JsonProperty("google_account_id")
    private int googleAccountId;
    public static UserResponse fromUser(User user)
    {
        return UserResponse.builder()
                .id(user.getId())
                .fullname(user.getFullName())
                .phone(user.getPhoneNumber())
                .address(user.getAddress())
                .active(user.isActive())
                .role(user.getRole())
                .dateOfbirth(user.getDateOfBirth())
                .facebookAccountId(user.getFacebookAccountId())
                .googleAccountId(user.getGoogleAccountId())
                .build();
    }
}
