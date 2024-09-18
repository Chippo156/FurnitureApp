package org.ecommerce.ecommerce.responses.create;

import lombok.*;
import org.ecommerce.ecommerce.models.Category;
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCategoryRepsonse {
    private Category category;
    private String message;
}
