package com.somacode.celmybell.config;

import com.somacode.celmybell.service.AuthorityService;
import com.somacode.celmybell.service.DocumentCategoryService;
import com.somacode.celmybell.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PopulateConfig {
    @Autowired UserService userService;
    @Autowired AuthorityService authorityService;
    @Autowired
    DocumentCategoryService documentCategoryService;

    public void init() {
        authorityService.init();
        userService.init();
        //documentCategoryService.init();
    }
}
