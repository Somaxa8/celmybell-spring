package com.somacode.celmybell.service;

import com.somacode.celmybell.config.exception.NotFoundException;
import com.somacode.celmybell.entity.Authority;
import com.somacode.celmybell.entity.User;
import com.somacode.celmybell.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;

@Service
@Transactional
public class AuthorityService {

    @Autowired AuthorityRepository authorityRepository;
    @Autowired UserService userService;


    public void init() {
        if (authorityRepository.count() <= 0) {
            for (Authority.Role role : Authority.Role.values()) {
                Authority authority = new Authority();
                authority.setRole(role);
                authority.setDescription("");
                authorityRepository.save(authority);
            }
        }
    }

    public void relateUser(Authority.Role role, Long userId) {
        if (!authorityRepository.existsById(role) || !userService.existsById(userId)) {
            throw new NotFoundException();
        }
        Authority authority = authorityRepository.getOne(role);
        User user = userService.findById(userId);
        authority.getUsers().add(user);
        if (user.getAuthorities() == null) {
            user.setAuthorities(new HashSet<>());
        }
        user.getAuthorities().add(authority);
        authorityRepository.save(authority);
    }

    public void unrelateUser(Authority.Role role, Long userId) {
        if (!authorityRepository.existsById(role) || !userService.existsById(userId)) {
            throw new NotFoundException();
        }

        User user = userService.findById(userId);
        Authority authority = authorityRepository.getOne(role);

        authority.getUsers().remove(user);
        user.getAuthorities().remove(authority);
        authorityRepository.save(authority);
    }
}
