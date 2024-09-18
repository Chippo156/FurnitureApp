package org.ecommerce.ecommerce.dtos;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ResetPassword {
    private String phoneNumber;
    private String password;
}
