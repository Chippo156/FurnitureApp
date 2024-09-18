package org.ecommerce.ecommerce.services;

import org.ecommerce.ecommerce.models.Token;
import org.ecommerce.ecommerce.models.User;

public interface iTokenService {
    Token addToken(User user, String token, boolean isMobileService);
    Token refreshToken(String refreshToken, User UserDetail);
}
