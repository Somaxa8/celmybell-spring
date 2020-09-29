package com.somacode.celmybell.config;

import com.somacode.celmybell.entity.Authority;
import com.somacode.celmybell.entity.oauth.OAuthClientDetails;
import com.somacode.celmybell.repository.OAuthClientDetailsRepository;
import com.somacode.celmybell.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.StringJoiner;

@Component
public class DatabaseConfig {

    @Autowired OAuthClientDetailsRepository oAuthClientDetailsRepository;
    @Autowired PasswordEncoder passwordEncoder;
    @Autowired PopulateConfig populateConfig;

    @Value("${custom.my-secret-token}") String mySecretToken;
    @Value("${custom.access-token-validity}") Integer accessTokenValidity;
    @Value("${custom.refresh-token-validity}") Integer refreshTokenValidity;
    @Value("${spring.application.name}") String appName;

    @PostConstruct
    public void init() {
        if (!oAuthClientDetailsRepository.existsById(appName)) {
            OAuthClientDetails oAuthClientDetails = new OAuthClientDetails();
            oAuthClientDetails.setClientId(appName);
            oAuthClientDetails.setResourceId("res_" + appName);
            oAuthClientDetails.setClientSecret(passwordEncoder.encode(mySecretToken));
            oAuthClientDetails.setScope("read,write");
            oAuthClientDetails.setAuthorizedGrantTypes("password,refresh_token,authorization_code,implicit");
            StringJoiner authorities = new StringJoiner(",");
            for (Authority.Name authority : Authority.Name.values()) {
                authorities.add(authority.toString());
            }
            LogService.out.info("Authorities: " + authorities);
            oAuthClientDetails.setAuthorities(authorities.toString());
            oAuthClientDetails.setAccessTokenValidity(accessTokenValidity);
            oAuthClientDetails.setRefreshTokenValidity(refreshTokenValidity);
            oAuthClientDetails.setAdditionalInformation("{}");
            oAuthClientDetails.setAutoapprove("true");
            oAuthClientDetailsRepository.save(oAuthClientDetails);
        }
        populateConfig.init();
    }
}
