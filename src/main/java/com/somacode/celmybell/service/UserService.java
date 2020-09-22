package com.somacode.celmybell.service;

import com.google.gson.JsonObject;
import com.somacode.celmybell.config.exception.BadRequestException;
import com.somacode.celmybell.config.exception.NotFoundException;
import com.somacode.celmybell.entity.User;
import com.somacode.celmybell.repository.UserRepository;
import com.somacode.celmybell.service.tool.PatchTool;
import com.somacode.celmybell.service.tool.TokenTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired UserRepository userRepository;
    @Autowired PatchTool patchTool;
    @Autowired PasswordEncoder passwordEncoder;
    @Autowired TokenTool tokenTool;

    public void init() {
        User user = new User();
        user.setUsername("celmybell");
        user.setName("celmy");
        user.setLastname("guzman");
        user.setEmail("celmy@gmail.com");
        user.setPassword(passwordEncoder.encode("1234"));
        user.setActivated(true);
        create(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User does not exist");
        }
        return userRepository.getOne(id);
    }

    public User create(User user) {
        if(user.getName() == null || user.getName().isEmpty()) {
          throw new BadRequestException("Invalid name");
        }
        return userRepository.save(user);
    }

    public User update(Long id, User user) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User does not exist");
        }
        User u = userRepository.getOne(id);

        user.setId(null);
        user.setPassword(null);

        patchTool.patch(user, u);
        return userRepository.save(u);
    }

    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User does not exist");
        }
        userRepository.deleteById(id);
    }

    public JsonObject login(String username, String password) {
        String token = tokenTool.getToken(username);
        JsonObject tokenObject = new JsonObject();
        tokenObject.addProperty("password", password);
        tokenObject.addProperty("token", token);
        return tokenObject;

    }

//    public JsonObject logout(HttpServletRequest request, HttpServletResponse response) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth != null) {
//            new SecurityContextLogoutHandler().logout(request, response, auth);
//        }
//
//        JsonObject json = new JsonObject();
//        json.addProperty("message", "you have logged out");
//        return json;
//    }
}
