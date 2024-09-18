package org.ecommerce.ecommerce.services.impl;

import lombok.RequiredArgsConstructor;
import org.ecommerce.ecommerce.components.JwtTokenUtils;
import org.ecommerce.ecommerce.models.Token;
import org.ecommerce.ecommerce.models.User;
import org.ecommerce.ecommerce.repository.TokenRepository;
import org.ecommerce.ecommerce.repository.UserRepository;
import org.ecommerce.ecommerce.services.iTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenService implements iTokenService {
    private static final int MAX_TOKENS = 3;
    @Value("${jwt.expiration}")
    private int expiration;

    @Value("${jwt.expirationRefreshToken}")
    private int expirationRefreshToken;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;

    @Override
    public Token addToken(User user, String token, boolean isMobileService) {
        List<Token> userTokens = tokenRepository.findByUser(user);
        int tokenCount = userTokens.size();
        if (tokenCount >= MAX_TOKENS) {
            //Kiểm tra xem danh sách userTokens có tồn tại ít nhất
            //một token không phải thiết bị di động (non-mobile)
            boolean hasNonMobileToken = !userTokens.stream().allMatch(Token::isMobile);
            Token tokenToDelete;
            if (hasNonMobileToken) {
                tokenToDelete = userTokens.stream().filter(userToken ->
                        !userToken.isMobile()).findFirst().orElse(userTokens.get(0));
            } else {
                tokenToDelete = userTokens.get(0);
            }
            tokenRepository.delete(tokenToDelete);
        }
        long expirationInSeconds = expiration;
        LocalDateTime expirationDate = LocalDateTime.now().plusSeconds(expirationInSeconds);
        Token newToken = Token.builder()
                .token(token)
                .expirationDate(expirationDate)
                .userId(user)
                .expired(false)
                .revoked(false)
                .tokenType("Bearer")
                .isMobile(isMobileService)
                .build();
        newToken.setRefreshToken(UUID.randomUUID().toString());
        newToken.setRefreshExpirationDate(LocalDateTime.now().plusSeconds(expirationRefreshToken));
        tokenRepository.save(newToken);
        return newToken;
    }

    @Override
    public Token refreshToken(String refreshToken, User UserDetail) {
        Token token = tokenRepository.findByRefreshToken(refreshToken);

        if (token == null) {
            throw new RuntimeException("Refresh token not found");
        }
        User user = token.getUserId();

        String newToken = jwtTokenUtils.generateToken(user);
        token.setToken(newToken);
        token.setExpirationDate(LocalDateTime.now().plusSeconds(expiration));
        tokenRepository.save(token);
        return token;
    }
}
