package org.ecommerce.ecommerce.responses;

import lombok.*;
import org.ecommerce.ecommerce.models.Comment;
import org.ecommerce.ecommerce.models.CommentImage;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponse {
    private int rating;
    private String content;
    private String userName;
    private List<CommentImage> images;
    public static CommentResponse fromComment(Comment comment) {
        return CommentResponse.builder()
                .content(comment.getCommentContent())
                .rating(comment.getRating())
                .userName(comment.getUser().getFullName())
                .images(comment.getImages())
                .build();
    }
}
