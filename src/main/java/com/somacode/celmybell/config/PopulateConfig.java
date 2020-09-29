package com.somacode.celmybell.config;

import com.somacode.celmybell.service.AuthorityService;
import com.somacode.celmybell.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PopulateConfig {
    @Autowired UserService userService;
    @Autowired AuthorityService authorityService;

    public void init() {
        authorityService.init();
        userService.init();
    }
}
