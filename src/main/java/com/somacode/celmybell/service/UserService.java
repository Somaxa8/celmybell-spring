package com.somacode.celmybell.service;

import com.somacode.celmybell.config.exception.BadRequestException;
import com.somacode.celmybell.config.exception.NotFoundException;
import com.somacode.celmybell.entity.Authority;
import com.somacode.celmybell.entity.User;
import com.somacode.celmybell.repository.UserRepository;
import com.somacode.celmybell.service.tool.PatchTool;
import com.somacode.celmybell.service.tool.TokenTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.stereotype.Service;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class UserService {

    @Autowired UserRepository userRepository;
    @Autowired PatchTool patchTool;
    @Autowired PasswordEncoder passwordEncoder;
    @Autowired AuthorityService authorityService;
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
        authorityService.relateUser(Authority.Name.ROLE_ADMIN, user.getId());

        user = new User();
        user.setUsername("soma");
        user.setName("silvio");
        user.setLastname("franco");
        user.setEmail("silvio@gmail.com");
        user.setPassword(passwordEncoder.encode("1234"));
        user.setActivated(true);
        create(user);
        authorityService.relateUser(Authority.Name.ROLE_ADMIN, user.getId());
    }

    public OAuth2AccessToken login(String username, String password) throws HttpRequestMethodNotSupportedException {
        if (!userRepository.existsByEmail(username)) {
            throw new InvalidGrantException("Bad credentials");
        }
        ResponseEntity<OAuth2AccessToken> accessToken = tokenTool.customLogin(username, password);
        return accessToken.getBody();
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

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }
}
