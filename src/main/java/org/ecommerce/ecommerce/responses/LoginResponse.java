package org.ecommerce.ecommerce.responses;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
    private String message;
    private String token;
    private String refreshToken;
    private String userName;
    private String tokenType;
    private Long id;

}
