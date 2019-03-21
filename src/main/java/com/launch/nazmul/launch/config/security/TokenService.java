package com.launch.nazmul.launch.config.security;

import com.launch.nazmul.launch.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class TokenService {
    private final TokenStore tokenStore;
    @Value("${app.client.id}")
    private String clientId;

    @Autowired
    public TokenService(@Qualifier("inMemoryTokenStore") TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    public void revokeAuthentication(User user) {
        Collection<OAuth2AccessToken> tokens = tokenStore.findTokensByClientIdAndUserName(clientId, user.getUsername());
        for (OAuth2AccessToken token : tokens) {
            revokeToken(token.getValue());
        }
    }

    private boolean revokeToken(String tokenValue) {
        OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
        if (accessToken == null) {
            return false;
        }
        if (accessToken.getRefreshToken() != null) {
            tokenStore.removeRefreshToken(accessToken.getRefreshToken());
        }
        tokenStore.removeAccessToken(accessToken);
        return true;
    }

}
