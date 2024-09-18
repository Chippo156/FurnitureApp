package org.ecommerce.ecommerce.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ecommerce.ecommerce.components.JwtTokenUtils;
import org.ecommerce.ecommerce.dtos.*;
import org.ecommerce.ecommerce.exceptions.DataNotFoundException;
import org.ecommerce.ecommerce.models.Role;
import org.ecommerce.ecommerce.models.SocialAccount;
import org.ecommerce.ecommerce.models.Token;
import org.ecommerce.ecommerce.models.User;
import org.ecommerce.ecommerce.repository.RoleRepository;
import org.ecommerce.ecommerce.repository.SocialAccountRepository;
import org.ecommerce.ecommerce.repository.TokenRepository;
import org.ecommerce.ecommerce.repository.UserRepository;
import org.ecommerce.ecommerce.responses.UserResponse;
import org.ecommerce.ecommerce.services.iUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService implements iUserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtils jwtTokenUtils;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private SocialAccountRepository socialAccountRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private static final Logger logger = LogManager.getLogger(UserService.class);

    @Override
    public User createUser(UserDTO userDto) throws Exception {
        String phoneNumber = userDto.getPhoneNumber();
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new Exception("Phone number already exists");
        }
        User user = User.builder()
                .fullName(userDto.getFullName())
                .phoneNumber(userDto.getPhoneNumber())
                .password(userDto.getPassword())
                .address(userDto.getAddress())
                .dateOfBirth(userDto.getDateOfBirth())
                .facebookAccountId(userDto.getFacebookAccountId())
                .googleAccountId(userDto.getGoogleAccountId())
                .isActive(true)
                .build();
        Role role = roleRepository.findById(userDto.getRoleId()).orElseThrow(() -> new Exception("Role not found"));
        user.setRole(role);
        if (userDto.getFacebookAccountId() == 0 && userDto.getGoogleAccountId() == 0) {
            String password = userDto.getPassword();
            String passwordEncoded = passwordEncoder.encode(password);
            user.setPassword(passwordEncoded);
        }
        return userRepository.save(user);
    }

    @Override
    public String login(String phoneNumber, String password) throws DataNotFoundException {
        Optional<User> user = userRepository.findByPhoneNumber(phoneNumber);
        if (user.isEmpty()) {
            throw new DataNotFoundException("User not found");
        }
        User existingUser = user.get();
        //Check password
        if (existingUser.getFacebookAccountId() == 0 && existingUser.getGoogleAccountId() == 0) {
            if (!passwordEncoder.matches(password, existingUser.getPassword())) {
                throw new BadCredentialsException("Invalid password");
            }
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                phoneNumber, password, existingUser.getAuthorities()
        );
        authenticationManager.authenticate(authenticationToken);
        return jwtTokenUtils.generateToken(existingUser);
    }


    @Override
    public User getUserDetailsFromToken(String token) throws Exception {
        if (jwtTokenUtils.isTokenExpired(token)) {
            throw new Exception("Token is expired");
        }
        String phoneNumber = jwtTokenUtils.extractPhoneNumber(token);
        Long userId = jwtTokenUtils.extractUserId(token);
        if(!socialAccountRepository.findByUserId(userId)){
            Optional<User> user = userRepository.findByPhoneNumber(phoneNumber);
            if (user.isPresent()) {
                return user.get();
            } else {
                throw new Exception("User not found");
            }
        }else {
            Optional<User> user = userRepository.findById(userId);
            if ( user.isPresent()) {
                return user.get();
            } else {
                throw new Exception("User not found");

            }
        }

    }
    @Override
    public User getUserDetailsFromRefreshToken(String refreshToken) throws Exception {
        Token existingToken = tokenRepository.findByRefreshToken(refreshToken);
        return getUserDetailsFromToken(existingToken.getToken());
    }

    @Override
    public Page<UserResponse> getUsers(String keyword, PageRequest pageRequest) {
        return userRepository.getUsers(keyword, pageRequest).map(UserResponse::fromUser);
    }

    @Override
    public UserResponse removeUser(Long userId) throws DataNotFoundException {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException("User not found"));
            user.setActive(false);
            userRepository.save(user);
            return UserResponse.fromUser(user);
        } catch (Exception e) {
            throw new DataNotFoundException("Save failed");
        }
    }

    @Override
    public UserResponse updateUser(Long userId, UserDTO userDTO) throws DataNotFoundException {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException("User not found"));
            if (userDTO.getFullName() != null) {
                user.setFullName(userDTO.getFullName());
            }
            if (userDTO.getAddress() != null) {
                user.setAddress(userDTO.getAddress());
            }
            if (userDTO.getDateOfBirth() != null) {
                user.setDateOfBirth(userDTO.getDateOfBirth());
            }
            if (userDTO.getPhoneNumber() != null) {
                user.setPhoneNumber(userDTO.getPhoneNumber());
            }
            userRepository.save(user);
            return UserResponse.fromUser(user);
        } catch (Exception e) {
            throw new DataNotFoundException("Save failed");
        }
    }

    @Override
    public boolean updatePassword(String phoneNumber, String newPassword) throws DataNotFoundException {
        try {
            User user = userRepository.findByPhoneNumber(phoneNumber).orElseThrow(() -> new DataNotFoundException("User not found"));
            String passwordEncoded = passwordEncoder.encode(newPassword);
            user.setPassword(passwordEncoded);
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            throw new DataNotFoundException("Save failed");
        }
    }

    @Override
    public List<Province> getAllProvinces() throws Exception {
        try {
            Resource resource = new ClassPathResource("province/provinces.json");
            Province[] provinces = objectMapper.readValue(resource.getInputStream(), Province[].class);
            return List.of(provinces);
        } catch (Exception e) {
            logger.warn("Can not get all provinces !Notice");
            return null;
        }
    }


    @Override
    public List<ProvinceDTO> getAllProvincesDTO() throws Exception {
        try {

            Resource resource = new ClassPathResource("province/provinces.json");
            Province[] provinces = objectMapper.readValue(resource.getInputStream(), Province[].class);
            Set<Long> containProvinceIdCheck = new HashSet<>();
            List<ProvinceDTO> provinceDTOS = new ArrayList<>();
            for (Province province : provinces) {
                if (!containProvinceIdCheck.contains(province.getProvinceId())) {
                    containProvinceIdCheck.add(province.getProvinceId());
                    ProvinceDTO provinceDTO = ProvinceDTO.builder()
                            .provinceId(province.getProvinceId())
                            .provinceName(province.getProvinceName())
                            .build();
                    provinceDTOS.add(provinceDTO);
                }
            }
            return provinceDTOS;
        } catch (Exception e) {
            logger.warn("Can not get 63 provinces !Notice");
            return null;
        }
    }

    @Override
    public List<DistrictDTO> getAllDistrictDTO(Long provinceId) throws Exception {
        try {
            Resource resource = new ClassPathResource("province/provinces.json");
            Province[] provinces = objectMapper.readValue(resource.getInputStream(), Province[].class);
            Set<Long> containDistrictIdCheck = new HashSet<>();
            List<DistrictDTO> districtDTOS = new ArrayList<>();

            for (Province province : provinces) {
                if (!containDistrictIdCheck.contains(province.getDistrictId()) && Objects.equals(province.getProvinceId(), provinceId)){
                    containDistrictIdCheck.add(province.getDistrictId());
                    DistrictDTO districtDTO = DistrictDTO.builder()
                            .districtId(province.getDistrictId())
                            .districtName(province.getDistrictName())
                            .build();
                    districtDTOS.add(districtDTO);
                }
            }
            return districtDTOS;
        } catch (Exception e) {
            logger.warn("Can not get all districts !Notice");
            return null;
        }
    }

    @Override
    public List<CommuneDTO> getAllCommuneDTO(Long districtId) throws Exception {
        try {
            Resource resource = new ClassPathResource("province/provinces.json");
            Province[] provinces = objectMapper.readValue(resource.getInputStream(), Province[].class);
            Set<Long> communeIdCheck = new HashSet<>();
            List<CommuneDTO> communeDTOS = new ArrayList<>();
            for (Province province : provinces) {
                if (!communeIdCheck.contains(province.getCommuneId()) && Objects.equals(province.getDistrictId(), districtId)){
                    communeIdCheck.add(province.getCommuneId());
                    CommuneDTO communeDTO = CommuneDTO.builder()
                            .communeId(province.getCommuneId())
                            .communeName(province.getCommuneName())
                            .build();
                    communeDTOS.add(communeDTO);
                }
            }
            return communeDTOS;
        } catch (Exception e) {
            logger.warn("Can not get all communes !Notice");
            return null;
        }
    }

    @Override
    public void updateSocialAccount(String phoneNumber, String providerId) throws Exception {
        SocialAccount socialAccount = socialAccountRepository.findByProviderId(providerId).orElseThrow(() -> new Exception("Social account not found"));
        socialAccount.setUser(userRepository.findByPhoneNumber(phoneNumber).orElseThrow(() -> new Exception("User not found")));
        socialAccountRepository.save(socialAccount);
    }


}
