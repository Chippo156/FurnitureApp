package org.ecommerce.ecommerce.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CommentImageDTO {
    @JsonProperty("comment_id")
    @Min(value = 1, message = "Comment id must be greater than 0")
    private Long commentId;
    @JsonProperty("image_url")
    @Size(min = 5, max = 200, message = "Image URL must be between 5 and 200 characters")
    private String image_url;
}
