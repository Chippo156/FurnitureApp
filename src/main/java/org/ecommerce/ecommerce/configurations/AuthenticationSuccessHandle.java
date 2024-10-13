package org.ecommerce.ecommerce.configurations;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.ecommerce.ecommerce.components.JwtTokenUtils;
import org.ecommerce.ecommerce.models.Role;
import org.ecommerce.ecommerce.models.SocialAccount;
import org.ecommerce.ecommerce.models.User;
import org.ecommerce.ecommerce.repository.RoleRepository;
import org.ecommerce.ecommerce.repository.SocialAccountRepository;
import org.ecommerce.ecommerce.repository.UserRepository;
import org.ecommerce.ecommerce.security.oauth2.CustomerOAuth2User;
import org.ecommerce.ecommerce.services.impl.UserService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Component
public class AuthenticationSuccessHandle implements AuthenticationSuccessHandler {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    SocialAccountRepository socialAccountRepository;
    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomerOAuth2User oAuth2User = (CustomerOAuth2User) authentication.getPrincipal();
        Logger logger = org.slf4j.LoggerFactory.getLogger(WebSecurityConfig.class);
        logger.info("OAuth2User: {}", oAuth2User);
        System.out.println(oAuth2User);
        oAuth2User.getAttributes().forEach((k, v) -> {
            System.out.println(k + " : " + v);
        });
        logger.info("OAuth2User: {}", oAuth2User.getAuthorities());
        String id = "";
        if (oAuth2User.getAttributes().size() == 3) {
            id = oAuth2User.getAttribute("id");
        } else if (oAuth2User.getAttributes().size() > 15) {
            id = Objects.requireNonNull(oAuth2User.getAttribute("id")).toString();
        } else {
            id = oAuth2User.getAttribute("sub");
        }
        String email = "";
        if (oAuth2User.getAttributes().size() == 3) {
            email = oAuth2User.getAttribute("email");
        } else if (oAuth2User.getAttributes().size() > 15) {
            email = oAuth2User.getAttribute("login");
        } else {
            email = oAuth2User.getAttribute("email");
        }
        String name = oAuth2User.getAttribute("name");
        logger.info("OAuth2User: {}", oAuth2User.getAttributes().size());
        String token = "";
        if (!socialAccountRepository.existsByProviderId(id)) {
            Optional<User> user;
            if (request.getParameter("phone_number") != null) {
                user = userRepository.findByPhoneNumber(request.getParameter("phone_number"));
                if (user.isPresent()) {
                    token = jwtTokenUtils.generateToken(user.get());
                }
            } else {
                if (oAuth2User.getAttributes().size() == 3) {
                    user = userRepository.findByEmailAndFacebookAccountId(email, 1);
                    if (user.isPresent()) {
                        token = jwtTokenUtils.generateToken(user.get());
                    } else {
                        Role roleUser = roleRepository.findById(2L).get();
                        User newUser = User.builder()
                                .fullName(name)
                                .googleAccountId(0)
                                .facebookAccountId(1)
                                .phoneNumber("N/A")
                                .address("N/A")
                                .password("N/A")
                                .email(email)
                                .isActive(true)
                                .role(roleUser)
                                .build();
                        userRepository.save(newUser);
                        user = Optional.of(newUser);
                        token = jwtTokenUtils.generateToken(newUser);
                    }
                } else if (oAuth2User.getAttributes().size() > 15) {

                    user = userRepository.findByEmailAndGithubAccountId(email, 1);
                    if (user.isPresent()) {
                        token = jwtTokenUtils.generateToken(user.get());
                    } else {
                        Role roleUser = roleRepository.findById(2L).get();
                        User newUser = User.builder()
                                .fullName(name)
                                .googleAccountId(0)
                                .facebookAccountId(0)
                                .githubAccountId(1)
                                .phoneNumber("N/A")
                                .address("N/A")
                                .password("N/A")
                                .email(email)
                                .isActive(true)
                                .role(roleUser)
                                .build();
                        userRepository.save(newUser);
                        user = Optional.of(newUser);
                        token = jwtTokenUtils.generateToken(newUser);
                    }
                } else {
                    user = userRepository.findByEmailAndGoogleAccountId(email, 1);
                    if (user.isPresent()) {
                        token = jwtTokenUtils.generateToken(user.get());
                    } else {
                        Role roleUser = roleRepository.findById(2L).get();
                        User newUser = User.builder()
                                .fullName(name)
                                .googleAccountId(1)
                                .facebookAccountId(0)
                                .phoneNumber("N/A")
                                .address("N/A")
                                .password("N/A")
                                .email(email)
                                .isActive(true)
                                .role(roleUser)
                                .build();
                        userRepository.save(newUser);
                        user = Optional.of(newUser);
                        token = jwtTokenUtils.generateToken(newUser);
                    }
                }
            }
            SocialAccount socialAccount = SocialAccount.builder()
                    .providerId(id)
                    .provider(oAuth2User.getAttributes().size() == 3 ? "facebook" : oAuth2User.getAttributes().size() > 15 ? "github" : "google")
                    .email(email)
                    .name(name)
                    .user(user.get())
                    .build();
            socialAccountRepository.save(socialAccount);
        } else {
            Optional<SocialAccount> socialAccount = socialAccountRepository.findByProviderId(id);
            if (socialAccount.isPresent()) {
                token = jwtTokenUtils.generateToken(socialAccount.get().getUser());
            }
        }
        new DefaultRedirectStrategy().sendRedirect(request, response, "http://localhost:4200?access_token=" + token+"&providerId="+id);
    }
}
