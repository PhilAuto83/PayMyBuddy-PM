package com.phildev.pmb.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

public final class UserInfoUtility {

    private static final Logger logger = LoggerFactory.getLogger(UserInfoUtility.class);

    private UserInfoUtility(){};

    public static String getUserAuthenticatedEmail(@AuthenticationPrincipal OidcUser oidcUser, Principal user){
        if(user instanceof UsernamePasswordAuthenticationToken){
            return user.getName();
        }else if(user instanceof OAuth2AuthenticationToken){
            return oidcUser.getEmail();
        }else{
            logger.error("No user connected found in Pay My Buddy");
            throw new RuntimeException("Cannot find an oidc user or a current user of Pay My Buddy");
        }
    }

}
