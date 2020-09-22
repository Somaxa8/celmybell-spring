package com.somacode.celmybell.service;

import com.somacode.celmybell.config.exception.BadRequestException;
import com.somacode.celmybell.config.exception.NotFoundException;
import com.somacode.celmybell.entity.User;
import com.somacode.celmybell.repository.UserRepository;
import com.somacode.celmybell.service.tool.PatchTool;
import com.somacode.celmybell.service.model.TokenResponse;
import com.somacode.celmybell.service.tool.TokenTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

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

    public TokenResponse login(String username, String password) {
        String token = tokenTool.getToken(username);
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setToken(token);
        tokenResponse.setPassword(password);
        return tokenResponse;

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
