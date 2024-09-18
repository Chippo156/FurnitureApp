package org.ecommerce.ecommerce.services;

import org.ecommerce.ecommerce.dtos.*;
import org.ecommerce.ecommerce.exceptions.DataNotFoundException;
import org.ecommerce.ecommerce.models.User;
import org.ecommerce.ecommerce.responses.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface iUserService {
    User createUser(UserDTO userDto) throws Exception;
    String login (String phoneNumber, String password) throws DataNotFoundException;

    User getUserDetailsFromToken(String token) throws Exception;
    User getUserDetailsFromRefreshToken(String refreshToken) throws Exception;

    Page<UserResponse> getUsers(String keyword, PageRequest pageRequest);

    UserResponse removeUser(Long userId) throws DataNotFoundException;
    UserResponse updateUser(Long userId, UserDTO userDTO) throws DataNotFoundException;

    boolean updatePassword(String phoneNumber, String newPassword) throws DataNotFoundException;

    List<Province> getAllProvinces() throws Exception;
    List<ProvinceDTO> getAllProvincesDTO() throws Exception;
    List<DistrictDTO> getAllDistrictDTO(Long provinceId) throws Exception;
    List<CommuneDTO> getAllCommuneDTO(Long districtId) throws Exception;

    void updateSocialAccount(String phoneNumber, String providerId) throws Exception;

}
