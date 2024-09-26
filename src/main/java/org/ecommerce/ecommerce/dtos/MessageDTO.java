package org.ecommerce.ecommerce.dtos;

import jakarta.persistence.Column;
import jakarta.persistence.OneToOne;
import lombok.*;
import org.ecommerce.ecommerce.models.User;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDTO {
    private Long sender; // "user" hoặc "admin"
    private Long receiver; // "admin" hoặc "user"
    private String content;
}
