package org.ecommerce.ecommerce.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.ecommerce.ecommerce.components.JwtTokenUtils;
import org.ecommerce.ecommerce.configurations.AuthenticationSuccessHandle;
import org.ecommerce.ecommerce.models.User;
import org.ecommerce.ecommerce.repository.SocialAccountRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Value("${api.prefix}")
    private String apiPrefix;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtTokenUtils jwtTokenUtils;
    @Autowired
    private SocialAccountRepository  socialAccountRepository;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        try {
            if (isByPassToken(request)) {
                filterChain.doFilter(request, response);
                return;
            }
            final String authenticationHeader = request.getHeader("Authorization");
            if (authenticationHeader == null || !authenticationHeader.startsWith("Bearer")) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            final String token = authenticationHeader.substring(7);
            final String phoneNumber = jwtTokenUtils.extractPhoneNumber(token);
            final Long userId = jwtTokenUtils.extractUserId(token);
            Logger logger = org.slf4j.LoggerFactory.getLogger(AuthenticationSuccessHandle.class);
            logger.info("User id: {}", userId);
            if(socialAccountRepository.findByUserId(userId)){
                filterChain.doFilter(request, response);
                return;
            }
            if (phoneNumber != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User userDetails = (User) userDetailsService.loadUserByUsername(phoneNumber);
                if (jwtTokenUtils.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private boolean isByPassToken(@NotNull HttpServletRequest request) {
        final List<Pair<String, String>> byPassTokens = Arrays.asList(
                Pair.of("oauth2/authorization/", "GET"),
                Pair.of(String.format("%s/users/register", apiPrefix), "POST"),
                Pair.of(String.format("%s/users/login", apiPrefix), "POST"),
                Pair.of(String.format("%s/users/reset-password", apiPrefix), "PUT"),
                Pair.of(String.format("%s/roles", apiPrefix), "GET"),
                Pair.of(String.format("%s/categories", apiPrefix), "GET"),
                Pair.of(String.format("%s/coupons", apiPrefix), "GET"),
                Pair.of(String.format("%s/comments", apiPrefix), "GET"),
                Pair.of(String.format("%s/orders", apiPrefix), "POST"),
                Pair.of(String.format("%s/users/refresh-token", apiPrefix), "POST"),
                Pair.of(String.format("%s/order-details/countQuantity", apiPrefix), "GET")
        );
        String path = request.getServletPath();
        String method = request.getMethod();
        if (path.contains(String.format("%s/orders", apiPrefix))
                && method.equals("GET")) {
            // Allow access to %s/orders
            return true;
        }
        if (path.equals(String.format("%s/categories", apiPrefix))
                && method.equals("GET")) {
            // Allow access to %s/orders
            return true;
        }
        if (path.contains(String.format("%s/products", apiPrefix))
                && method.equals("GET")) {
            // Allow access to %s/orders
            return true;
        }
        if (path.contains(String.format("%s/products/viewImages", apiPrefix))
                && method.equals("GET")) {
            // Allow access to %s/orders
            return true;
        }
        if (path.contains(String.format("%s/payment", apiPrefix))
                && method.equals("GET")) {
            // Allow access to %s/orders
            return true;
        }
        if (path.contains(String.format("%s/users/get-provinces", apiPrefix))
                && method.equals("GET")) {
            return true;
        }
        if (path.contains(String.format("%s/users/get-districts/", apiPrefix))
                && method.equals("GET")) {
            return true;
        }
        if (path.contains(String.format("%s/users/get-communes/", apiPrefix))
                && method.equals("GET")) {
            return true;
        }

        for (Pair<String, String> byPassToken : byPassTokens) {
            if (request.getServletPath().contains(byPassToken.getFirst()) && request.getMethod().equals(byPassToken.getSecond())) {
                return true;
            }
        }
        return false;
    }
}
