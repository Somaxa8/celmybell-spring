package com.somacode.celmybell.controller;

import com.somacode.celmybell.entity.User;
import com.somacode.celmybell.service.UserService;
import com.somacode.celmybell.service.tool.TokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    @Autowired UserService userService;

    @PostMapping("/public/login")
    public ResponseEntity<TokenResponse> login(@RequestBody String username, @RequestBody String password) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.login(username, password));
    }

//    @GetMapping("/public/logout")
//    public ResponseEntity<JsonObject> logout(HttpServletRequest request, HttpServletResponse response) {
//        return ResponseEntity.status(HttpStatus.OK).body(userService.logout(request, response));
//    }

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
    public ResponseEntity<User> editUser(@PathVariable Long id, @RequestBody User user) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.update(id, user));
    }

    @DeleteMapping("/api/users/{id}")
    public ResponseEntity<User> editUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}
