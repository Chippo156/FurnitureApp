package org.ecommerce.ecommerce.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommuneDTO {
    @JsonProperty("commune_name")
    private String communeName;
    @JsonProperty("commune_id")
    private Long communeId;
}
