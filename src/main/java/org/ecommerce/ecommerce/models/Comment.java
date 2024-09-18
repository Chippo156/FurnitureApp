package org.ecommerce.ecommerce.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments")
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    @Column(name = "comment_content", length = 1000)
    @JsonProperty("comment_content")
    private String commentContent;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonProperty("user")
    @JsonIgnore
    private User user;
    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;
    private int rating;
    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<CommentImage> images;

}
