package org.ecommerce.ecommerce.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.ecommerce.ecommerce.dtos.CommuneDTO;
import org.ecommerce.ecommerce.dtos.DistrictDTO;
import org.ecommerce.ecommerce.dtos.Province;
import org.ecommerce.ecommerce.dtos.ProvinceDTO;
import org.ecommerce.ecommerce.responses.ProductResponse;
import org.ecommerce.ecommerce.services.iProvinceRedisService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProvinceRedisService implements iProvinceRedisService {
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper redisObjectMapper;

    @Override
    public void clear() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }


    @Override
    public List<Province> getAllProvinces() throws Exception {
        String key = "all_provinces";
        String json = redisTemplate.opsForValue().get(key);
        List<Province> provinces = json != null ? redisObjectMapper.readValue(json, new TypeReference<List<Province>>() {
        }) : null;
        return provinces;
    }

    @Override
    public void saveAllProvinces(List<Province> provinces) throws Exception {
        String key = "all_provinces";
        String json = redisObjectMapper.writeValueAsString(provinces);
        redisTemplate.opsForValue().set(key, json);
    }

    @Override
    public List<ProvinceDTO> getAllProvincesDTO() throws Exception {
        clear();
        String key = "all_provinces_dto";
        String json = redisTemplate.opsForValue().get(key);
        List<ProvinceDTO> provinceDTOS = json != null ? redisObjectMapper.readValue(json, new TypeReference<List<ProvinceDTO>>() {
        }) : null;
        return provinceDTOS;
    }

    @Override
    public void saveAllProvince(List<ProvinceDTO> provinceDTOS) throws Exception {
        String key = "all_provinces_dto";
        String json = redisObjectMapper.writeValueAsString(provinceDTOS);
        redisTemplate.opsForValue().set(key, json);

    }

    private String getKeyFromDistrict(Long provinceId) {
        return String.format("districts:%d", provinceId);
    }

    @Override
    public List<DistrictDTO> getAllDistrictDTO(Long provinceId) throws Exception {
        String key = this.getKeyFromDistrict(provinceId);
        String json = redisTemplate.opsForValue().get(key);
        List<DistrictDTO> districtDTOS = json != null ? redisObjectMapper.readValue(json, new TypeReference<List<DistrictDTO>>() {
        }) : null;
        return districtDTOS;
    }

    @Override
    public void saveAllDistrict(List<DistrictDTO> districtDTOS, Long provinceId) throws Exception {
        String key = this.getKeyFromDistrict(provinceId);
        String json = redisObjectMapper.writeValueAsString(districtDTOS);
        redisTemplate.opsForValue().set(key, json);
    }

    private String getKeyFromCommune(Long districtId) {
        return String.format("communes:%d", districtId);
    }

    @Override
    public List<CommuneDTO> getAllCommuneDTO(Long districtId) throws Exception {
        String key = this.getKeyFromCommune(districtId);
        String json = redisTemplate.opsForValue().get(key);
        List<CommuneDTO> communeDTOS = json != null ? redisObjectMapper.readValue(json, new TypeReference<List<CommuneDTO>>() {
        }) : null;
        return communeDTOS;
    }

    @Override
    public void saveAllCommune(List<CommuneDTO> communeDTOS, Long districtId) throws Exception {
        String key = this.getKeyFromCommune(districtId);
        String json = redisObjectMapper.writeValueAsString(communeDTOS);
        redisTemplate.opsForValue().set(key, json);
    }
}
