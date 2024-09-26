package org.ecommerce.ecommerce.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "messages")
public class Message {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long sender; // "user" hoặc "admin"
    private Long receiver; // "admin" hoặc "user"
    private String content;
    @Column(name = "is_read", nullable = false)
    private boolean isRead = false;
    private LocalDateTime timestamp = LocalDateTime.now();

    // Getters và Setters
}