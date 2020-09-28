package com.somacode.celmybell.service;

import com.somacode.celmybell.entity.Authority;
import com.somacode.celmybell.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class AuthorityService {

    @Autowired AuthorityRepository authorityRepository;

    public void init() {
        if (authorityRepository.count() <= 0) {
            for (Authority.Name name : Authority.Name.values()) {
                Authority authority = new Authority();
                authority.setName(name);
                authority.setDescription("");
                authorityRepository.save(authority);
            }
        }
    }
}
