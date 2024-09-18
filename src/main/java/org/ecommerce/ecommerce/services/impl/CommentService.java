package org.ecommerce.ecommerce.services.impl;

import lombok.RequiredArgsConstructor;
import org.ecommerce.ecommerce.dtos.CommentDTO;
import org.ecommerce.ecommerce.dtos.CommentImageDTO;
import org.ecommerce.ecommerce.models.Comment;
import org.ecommerce.ecommerce.models.CommentImage;
import org.ecommerce.ecommerce.models.Product;
import org.ecommerce.ecommerce.models.User;
import org.ecommerce.ecommerce.repository.CommentImageRepository;
import org.ecommerce.ecommerce.repository.CommentRepository;
import org.ecommerce.ecommerce.repository.ProductRepository;
import org.ecommerce.ecommerce.repository.UserRepository;
import org.ecommerce.ecommerce.responses.CommentResponse;
import org.ecommerce.ecommerce.services.iCommentService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommentService implements iCommentService {
    private final CommentRepository commentRepository;
    private final ProductRepository productRepository;
    private final CommentImageRepository commentImageRepository;

    private final UserRepository userRepository;

    @Override
    public Comment createComment(CommentDTO commentDTO) {

        Product existProduct = productRepository.findById(commentDTO.getProductId()).orElseThrow(()
                -> new RuntimeException("Product not found"));
        User existUser = userRepository.findById(commentDTO.getUserId()).orElseThrow(()
                -> new RuntimeException("User not found"));
        Comment comment = Comment.builder().
                commentContent(commentDTO.getContent()).
                rating(commentDTO.getRating())
                .user(existUser)
                .product(existProduct)
                .build();
        return commentRepository.save(comment);
    }

    @Override
    public Comment updateComment(Long commentId, CommentDTO comment) {
        return null;
    }

    @Override
    public Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(()-> new RuntimeException("Comment not found"));
    }

    @Override
    public void deleteComment(Long commentId) {

    }

    @Override
    public Map<Long,Integer> countComment() {
        List<Object[]> results = commentRepository.countComment();
        Map<Long, Integer> commentCountMap = new HashMap<>();

        for (Object[] result : results) {
            Long productId = (Long) result[0];
            Integer count = ((Number) result[1]).intValue();
            commentCountMap.put(productId, count);
        }
        return commentCountMap;
    }

    @Override
    public CommentImage createCommentImage(Long commentId, CommentImageDTO commentImageDTO) {
        try {
            Comment existComment = commentRepository.findById(commentImageDTO.getCommentId()).orElseThrow(()
                    -> new RuntimeException("Comment not found"));
            CommentImage commentImage =  CommentImage.builder()
                    .imageUrl(commentImageDTO.getImage_url())
                    .comment(existComment)
                    .build();
            int size = commentImageRepository.findAllByCommentId(existComment.getId()).size();
            if(size >= 5){
                throw new RuntimeException("You can't upload more than 5 images");

            }
            return commentImageRepository.save(commentImage);
        }catch (Exception e){
            throw new RuntimeException("Error while saving image");
        }
    }

    @Override
    public List<CommentResponse> getCommentsByProductId(Long productId) {
        return commentRepository.findByProduct(productId).stream().map(CommentResponse::fromComment).toList();
    }
}
