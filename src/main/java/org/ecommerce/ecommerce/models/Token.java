package org.ecommerce.ecommerce.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table (name = "tokens")
public class Token {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    @Column(name = "token",unique = true,nullable = false,length = 255)
    private String token;
    @Column(name = "token_type",nullable = false,length = 50)
    private String tokenType;
    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;
    @Column(name = "revoked",nullable = false,columnDefinition = "TINYINT(1)")
    private boolean revoked;
    @Column(name = "expired",nullable = false,columnDefinition = "TINYINT(1)")
    private boolean expired;

    @Column(name = "is_mobile",nullable = false,columnDefinition = "TINYINT(1)")
    private boolean isMobile;
    @Column(name = "refresh_token",length = 255)
    private String refreshToken;
    @Column(name = "refresh_expiration_date")
    private LocalDateTime refreshExpirationDate;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;
}
