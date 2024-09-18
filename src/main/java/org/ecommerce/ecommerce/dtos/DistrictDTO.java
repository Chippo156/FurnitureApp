package org.ecommerce.ecommerce.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DistrictDTO {
    @JsonProperty("district_name")
    private String districtName;
    @JsonProperty("district_id")
    private Long districtId;
}
