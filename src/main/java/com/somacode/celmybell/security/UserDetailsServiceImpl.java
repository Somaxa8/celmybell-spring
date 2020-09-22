package com.somacode.celmybell.security;

import com.somacode.celmybell.entity.Authority;
import com.somacode.celmybell.entity.User;
import com.somacode.celmybell.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User appUser = userRepository.findByUsername(username).
        orElseThrow(() -> new UsernameNotFoundException("No existe usuario"));

        List<GrantedAuthority> grantList = new ArrayList<>();

        for (Authority authority: appUser.getAuthorities()) {
            // ROLE_USER, ROLE_ADMIN,..
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority.getName().toString());
            grantList.add(grantedAuthority);
        }
        //Crear El objeto UserDetails que va a ir en sesion y retornarlo.
        UserDetails user = (UserDetails) new org.springframework.security.core.userdetails.User(appUser.getUsername(), appUser.getPassword(), grantList);
        return user;
    }
}
