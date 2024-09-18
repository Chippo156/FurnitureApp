package org.ecommerce.ecommerce.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProvinceDTO {
    @JsonProperty("province_name")
    private String provinceName;
    @JsonProperty("province_id")
    private Long provinceId;
}
