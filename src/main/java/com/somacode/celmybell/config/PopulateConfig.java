package com.somacode.celmybell.config;

import com.somacode.celmybell.service.AuthorityService;
import com.somacode.celmybell.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class PopulateConfig {
    @Autowired UserService userService;
    @Autowired AuthorityService authorityService;

    @PostConstruct
    public void populateDatabase() {
        authorityService.init();
        userService.init();
    }
}
