package com.somacode.celmybell.security;

import com.google.gson.JsonObject;
import com.somacode.celmybell.service.LogService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LogoutSuccess extends AbstractAuthenticationTargetUrlRequestHandler implements LogoutSuccessHandler {

    @Autowired TokenStore tokenStore;


    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        LogService.out.debug("/logout");
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            String tokenString = token.substring(7);
            OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(tokenString);
            if (oAuth2AccessToken != null) {
                tokenStore.removeAccessToken(oAuth2AccessToken);
                setSuccessBody(response, tokenString);
            } else {
                setUnauthorizedBody(response, tokenString);
            }
        } else {
            setUnauthorizedBody(response);
        }
    }

    private void setSuccessBody(HttpServletResponse response, String tokenString) throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("message", "token_revoked");
        jsonObject.addProperty("description", "Access token revoked: " + tokenString);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().print(jsonObject);
    }

    private void setUnauthorizedBody(HttpServletResponse response, String tokenString) throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("error", "invalid_token");
        jsonObject.addProperty("error_description", "Invalid access token: " + tokenString);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().print(jsonObject);
    }

    private void setUnauthorizedBody(HttpServletResponse response) throws IOException {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("timestamp", DateTime.now().toString());
        jsonObject.addProperty("status", 401);
        jsonObject.addProperty("error", "Unauthorized");
        jsonObject.addProperty("message", "Full authentication is required to access this resource");
        jsonObject.addProperty("path", "/api/logout");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().print(jsonObject);
    }
}