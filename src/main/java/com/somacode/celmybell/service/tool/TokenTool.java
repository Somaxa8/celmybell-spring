package com.somacode.celmybell.service.tool;

import com.somacode.celmybell.repository.OAuthAccessTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class TokenTool {

    @Value("${custom.my-secret-token}") String secretToken;
    @Value("${spring.application.name}") String clientId;
    @Autowired TokenEndpoint tokenEndpoint;
    @Autowired OAuthAccessTokenRepository oAuthAccessTokenRepository;

    public ResponseEntity<OAuth2AccessToken> customLogin(String email, String password) throws HttpRequestMethodNotSupportedException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("username", email);
        parameters.put("password", password);
        parameters.put("grant_type", "password");
        parameters.put("scope", "read write");
        parameters.put("client_secret", secretToken);
        parameters.put("client_id", clientId);

        Object p = new User(clientId, secretToken, SecurityTool.getAllAuthorities());
        Principal principal = new UsernamePasswordAuthenticationToken(p, secretToken, SecurityTool.getAllAuthorities());
        return tokenEndpoint.postAccessToken(principal, parameters);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void customLogout(Long employeeId) {
        oAuthAccessTokenRepository.deleteByUserName_Id(employeeId);
    }

}
