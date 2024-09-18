package org.ecommerce.ecommerce.responses.create;

import lombok.*;
import org.ecommerce.ecommerce.responses.CommentCountResponse;

import java.util.List;
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListCommentCountResponse {
    private List<CommentCountResponse> commentCountResponses;
}
