package org.ecommerce.ecommerce.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "social_accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocialAccount {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    @Column(name = "provider",nullable = false,length = 20)
    private String provider;
    @Column(name = "provider_id",nullable = false,length = 50)
    private String providerId;
    @Column(name = "email",nullable = false,length = 150)
    private String email;
    @Column(name = "name",nullable = false,length = 100)
    private String name;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
