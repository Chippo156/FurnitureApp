package org.ecommerce.ecommerce.services;

import org.ecommerce.ecommerce.dtos.CommuneDTO;
import org.ecommerce.ecommerce.dtos.DistrictDTO;
import org.ecommerce.ecommerce.dtos.Province;
import org.ecommerce.ecommerce.dtos.ProvinceDTO;

import java.util.List;

public interface iProvinceRedisService {
    void clear();
    List<Province> getAllProvinces() throws Exception;
    void saveAllProvinces(List<Province> provinces) throws Exception;

    List<ProvinceDTO> getAllProvincesDTO() throws Exception;
    void saveAllProvince(List<ProvinceDTO> provinceDTOS) throws Exception;
    List<DistrictDTO> getAllDistrictDTO(Long provinceId) throws Exception;
    void saveAllDistrict(List<DistrictDTO> districtDTOS, Long provinceId) throws Exception;
    List<CommuneDTO> getAllCommuneDTO(Long districtId) throws Exception;

    void saveAllCommune(List<CommuneDTO> communeDTOS, Long districtId) throws Exception;
}
