package org.ecommerce.ecommerce.models;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name",nullable = false,length = 20)
    private String name;

    private final static String USER = "USER";
    private final static String ADMIN = "ADMIN";
}
