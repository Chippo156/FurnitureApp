package org.ecommerce.ecommerce.services;

import org.ecommerce.ecommerce.dtos.CommentDTO;
import org.ecommerce.ecommerce.dtos.CommentImageDTO;
import org.ecommerce.ecommerce.models.Comment;
import org.ecommerce.ecommerce.models.CommentImage;
import org.ecommerce.ecommerce.responses.CommentResponse;

import java.util.List;
import java.util.Map;

public interface iCommentService {

    Comment createComment(CommentDTO commentDTO);
    Comment updateComment(Long commentId, CommentDTO commentDTO);
    Comment getCommentById(Long commentId);
    void deleteComment(Long commentId);
    Map<Long,Integer> countComment();

    CommentImage createCommentImage(Long commentId, CommentImageDTO commentImageDTO);

    List<CommentResponse> getCommentsByProductId(Long productId);


}
