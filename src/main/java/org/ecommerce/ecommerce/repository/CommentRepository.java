package org.ecommerce.ecommerce.repository;

import org.ecommerce.ecommerce.models.Comment;
import org.ecommerce.ecommerce.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c.product.id, COUNT(c) as comment FROM Comment c WHERE c.product.id IS NOT NULL GROUP BY c.product.id")
    List<Object[]> countComment();

    @Query("SELECT c FROM Comment c WHERE c.product.id = :productId ORDER BY c.createdAt DESC limit 6")
    List<Comment> findByProduct(@Param("productId") Long productId);
}
