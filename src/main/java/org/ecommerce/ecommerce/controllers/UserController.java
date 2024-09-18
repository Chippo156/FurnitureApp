package org.ecommerce.ecommerce.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.ecommerce.ecommerce.dtos.*;
import org.ecommerce.ecommerce.models.Token;
import org.ecommerce.ecommerce.models.User;
import org.ecommerce.ecommerce.responses.LoginResponse;
import org.ecommerce.ecommerce.responses.RegisterResponse;
import org.ecommerce.ecommerce.responses.UserListResponse;
import org.ecommerce.ecommerce.responses.UserResponse;
import org.ecommerce.ecommerce.services.impl.ProductRedisService;
import org.ecommerce.ecommerce.services.impl.ProvinceRedisService;
import org.ecommerce.ecommerce.services.impl.TokenService;
import org.ecommerce.ecommerce.services.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private ProvinceRedisService provinceRedisService;

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO , BindingResult result) throws Exception {
        try {
            if(result.hasErrors())
            {
                List<String> errors = result.getFieldErrors().stream().map(error -> error.getField() + " " + error.getDefaultMessage()).toList();
                return ResponseEntity.badRequest().body(errors);
            }
            if(!userDTO.getPassword().equals(userDTO.getRetypePassword()))
            {
                return ResponseEntity.badRequest().body("Password and retype password not match");
            }
            User user = userService.createUser(userDTO);
            return ResponseEntity.ok(RegisterResponse.builder().message("Register successfully").user(user).build());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDTO userLoginDTO , BindingResult result, HttpServletRequest request) throws Exception {
        try {
            if(result.hasErrors())
            {
                List<String> errors = result.getFieldErrors().stream().map(error -> error.getField() + " " + error.getDefaultMessage()).toList();
                return ResponseEntity.badRequest().body(errors);
            }
            String token = userService.login(userLoginDTO.getPhoneNumber(), userLoginDTO.getPassword());
            String userAgent= request.getHeader("User-Agent");
            User user = userService.getUserDetailsFromToken(token);
            Token tokenRefresh=  tokenService.addToken(user, token, isMobileService(userAgent));
            return ResponseEntity.ok(LoginResponse.builder().message("Login successfully").token(token)
                    .refreshToken(tokenRefresh.getRefreshToken())
                    .build());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private boolean isMobileService(String userAgent) {

        return userAgent.toLowerCase().contains("mobile");
    }
    @GetMapping("/details")
    public ResponseEntity<?> getUserDetails(@RequestHeader("Authorization") String token) throws Exception {
        try {
            String extractedToken = token.substring(7);
            User user = userService.getUserDetailsFromToken(extractedToken);
            return ResponseEntity.ok(UserResponse.fromUser(user));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/refresh-token")
    public ResponseEntity<LoginResponse> refreshToken(
            @Valid @RequestBody RequestTokenDTO requestTokenDTO
    ){
        try{
            User userDetail = userService.getUserDetailsFromRefreshToken(requestTokenDTO.getRefreshToken());
            Token jwtToken = tokenService.refreshToken(requestTokenDTO.getRefreshToken(), userDetail);
            return ResponseEntity.ok(LoginResponse.builder()
                            .message("Token refreshed successfully")
                            .token(jwtToken.getToken())
                            .refreshToken(jwtToken.getRefreshToken())
                            .id(userDetail.getId())
                            .userName(userDetail.getFullName())
                            .tokenType(jwtToken.getTokenType())
                    .build());

        }catch (Exception e){
            return ResponseEntity.badRequest().body(LoginResponse.builder().message(e.getMessage()).build());
        }
    }
    @GetMapping("")
    public ResponseEntity<?> getUsers(@RequestParam("keyword") String keyword,
       @RequestParam int page,@RequestParam int limit
    ){
        try{
            PageRequest pageRequest = PageRequest.of(page, limit);
            Page<UserResponse> users = userService.getUsers(keyword, pageRequest);
            int totalPage = users.getTotalPages();
            List<UserResponse> userList = users.getContent();
            return ResponseEntity.ok(UserListResponse.builder().userResponses(userList).totalPages(totalPage).build());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeUser(@PathVariable Long id){
        try{
            UserResponse userResponse = userService.removeUser(id);
            return ResponseEntity.ok(userResponse);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id ,@Valid @RequestBody UserDTO userDTO ,BindingResult result){
        try{
            if(result.hasErrors())
            {
                List<String> errors = result.getFieldErrors().stream().map(error -> error.getField() + " " + error.getDefaultMessage()).toList();
                return ResponseEntity.badRequest().body(errors);
            }
            UserResponse userResponse = userService.updateUser(id, userDTO);
            return ResponseEntity.ok(userResponse);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/provinces")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Province>> getAllProvinces(){
        try{
            List<Province> provinces = provinceRedisService.getAllProvinces();
            if(provinces == null){
                provinces = userService.getAllProvinces();
                provinceRedisService.saveAllProvinces(provinces);
            }
            return ResponseEntity.ok(provinces);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(null);
        }
    }
    @GetMapping("/get-provinces")
    public ResponseEntity<?> getAllProvincesDTO(){
        try{
            List<ProvinceDTO> provinceDTOS = provinceRedisService.getAllProvincesDTO();
            if(provinceDTOS == null){
                provinceDTOS = userService.getAllProvincesDTO();
                provinceRedisService.saveAllProvince(provinceDTOS);
            }
            return ResponseEntity.ok(provinceDTOS);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/get-districts/{provinceId}")
    public ResponseEntity<?> getAllDistrictDTO(@PathVariable Long provinceId){
        try{
            List<DistrictDTO> districtDTOS = provinceRedisService.getAllDistrictDTO(provinceId);
            if(districtDTOS == null){
                districtDTOS = userService.getAllDistrictDTO(provinceId);
                provinceRedisService.saveAllDistrict(districtDTOS, provinceId);
            }
            return ResponseEntity.ok(districtDTOS);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/get-communes/{districtId}")
    public ResponseEntity<?> getAllCommuneDTO(@PathVariable Long districtId){
        try{
            List<CommuneDTO> communeDTOS = provinceRedisService.getAllCommuneDTO(districtId);
            if(communeDTOS == null){
                communeDTOS = userService.getAllCommuneDTO(districtId);
                provinceRedisService.saveAllCommune(communeDTOS, districtId);
            }
            return ResponseEntity.ok(communeDTOS);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/update-password")
    public ResponseEntity<?> update_password(@RequestBody UserDTO userDT0,BindingResult result){
    try{
            boolean updateResult = userService.updatePassword(userDT0.getPhoneNumber(), userDT0.getPassword());
            if(updateResult){
                return ResponseEntity.ok("Update password successfully");
            }
            return ResponseEntity.badRequest().body("Update password failed");

    }catch (Exception e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }
    }
    @PutMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPassword resetPassword){
        try{
            boolean updateResult = userService.updatePassword(resetPassword.getPhoneNumber(),resetPassword.getPassword());
            if(updateResult){
                return ResponseEntity.ok("Reset password successfully");
            }
            return ResponseEntity.badRequest().body("Reset password failed");

        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/update-social-account")
    public ResponseEntity<?> updateSocialAccount(@RequestParam String phoneNumber, @RequestParam String providerId){
        try{
            userService.updateSocialAccount(phoneNumber,providerId);
            return ResponseEntity.ok("Update social account successfully");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
