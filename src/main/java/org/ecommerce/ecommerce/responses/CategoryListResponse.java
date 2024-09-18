package org.ecommerce.ecommerce.responses;

import lombok.*;
import org.ecommerce.ecommerce.models.Category;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryListResponse {
    private List<Category> categories;
    private int totalPage;

}
