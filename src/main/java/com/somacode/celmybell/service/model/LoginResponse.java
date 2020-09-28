package com.somacode.celmybell.service.model;


import com.somacode.celmybell.entity.Authority;
import com.somacode.celmybell.entity.User;
import lombok.Data;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import java.util.Set;

@Data
public class LoginResponse {

    private OAuth2AccessToken oAuth2AccessToken;
    private User user;
    private Set<Authority> authorities;

}
