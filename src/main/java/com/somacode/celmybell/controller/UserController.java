package com.somacode.celmybell.controller;

import com.somacode.celmybell.entity.User;
import com.somacode.celmybell.service.UserService;
import com.somacode.celmybell.service.model.LoginResponse;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    @Autowired UserService userService;

    @PostMapping("/public/users/login")
    public ResponseEntity<LoginResponse> postLogin(@RequestParam String username, @RequestParam String password) throws HttpRequestMethodNotSupportedException {
        var loginResponse = new LoginResponse();
        loginResponse.setOAuth2AccessToken(userService.login(username, password));
        User user = userService.findByEmail(username);
        loginResponse.setUser(user);
        loginResponse.setAuthorities(user.getAuthorities());
        return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
    }
    @PostMapping("/api/users/refresh")
    public ResponseEntity<LoginResponse> postRefresh(@RequestParam String refreshToken) throws HttpRequestMethodNotSupportedException {
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setOAuth2AccessToken(userService.refresh(refreshToken));
        return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
    }

    @PostMapping("/api/users/logout")
    public ResponseEntity<?> postLogout(@RequestParam Long userId) {
        userService.logout(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @GetMapping("/api/users")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
    }

    @GetMapping("/api/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findById(id));
    }

    @PostMapping("/api/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(user));
    }

    @PatchMapping("/api/users/{id}")
    public ResponseEntity<User> patchUser(@PathVariable Long id, @RequestBody User user) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.update(id, user));
    }

    @DeleteMapping("/api/users/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}
