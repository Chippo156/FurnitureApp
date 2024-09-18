package org.ecommerce.ecommerce.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CategoryDTO {
    @JsonProperty("category_name")
    @Size(min = 3, max = 300, message = "Category name must be between 3 and 300 characters")
    private String name;
}
