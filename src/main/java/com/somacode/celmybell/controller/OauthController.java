package com.somacode.celmybell.controller;

import com.somacode.celmybell.entity.User;
import com.somacode.celmybell.service.OauthService;
import com.somacode.celmybell.service.UserService;
import com.somacode.celmybell.service.model.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OauthController {

    @Autowired UserService userService;
    @Autowired OauthService oauthService;


    @PostMapping("/public/oauth/login")
    public ResponseEntity<LoginResponse> postLogin(@RequestParam String username, @RequestParam String password) throws HttpRequestMethodNotSupportedException {
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setOAuth2AccessToken(oauthService.login(username, password));
        User user = userService.findByEmail(username);
        loginResponse.setUser(user);
        loginResponse.setAuthorities(user.getAuthorities());
        return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
    }
    @PostMapping("/api/oauth/refresh")
    public ResponseEntity<LoginResponse> postRefresh(@RequestParam String refreshToken) throws HttpRequestMethodNotSupportedException {
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setOAuth2AccessToken(oauthService.refresh(refreshToken));
        return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
    }

    @PostMapping("/api/oauth/logout")
    public ResponseEntity<?> postLogout(@RequestParam Long userId) {
        oauthService.logout(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
