package org.ecommerce.ecommerce.repository;

import org.ecommerce.ecommerce.models.CommentImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentImageRepository extends JpaRepository<CommentImage, Integer> {
    List<CommentImage>  findAllByCommentId(Long commentId);
}
