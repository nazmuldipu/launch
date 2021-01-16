package com.ship.nazmul.ship.config.security.oauth;

import com.ship.nazmul.ship.entities.User;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

public class CustomTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        final Map<String, Object> additionalInfo = new HashMap<>();

        additionalInfo.put("id", user.getId());
        additionalInfo.put("name", user.getName());
        additionalInfo.put("username", user.getUsername());
        additionalInfo.put("phone", user.getPhoneNumber());
        additionalInfo.put("authorities", user.getAuthorities());
        additionalInfo.put("canReserve", user.isCanReserve());
        additionalInfo.put("canCancelReservation", user.isCanCancelReservation());
        additionalInfo.put("canCancelBooking", user.isCanCancelBooking());

        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);

        return accessToken;
    }
}

