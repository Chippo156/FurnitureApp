package org.ecommerce.ecommerce.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "products")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(ProductListener.class)
public class Product extends BaseEntity{

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name",length = 350)
    private String name;
    @Column(name = "price",nullable = false)
    private double price;
    @Column(name = "thumbnail",length = 300)
    private String thumbnail;
    @Column(name = "description")
    private String description;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @ManyToOne
    @JoinColumn(name = "sale_id")
    private ProductSale sale;
    @ManyToOne
    @JoinColumn(name = "classify_color_id")
    private ClassifyColor classifyColor;

    @Column(name = "code_color",length = 10)
    private String codeColor;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<ProductImage> productImages;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Comment> comments;

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<ProductSize> productSizes;

}
