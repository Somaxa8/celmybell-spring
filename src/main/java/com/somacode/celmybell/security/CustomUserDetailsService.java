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
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;


@Component("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user != null) {
            ArrayList<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            for (Authority authority : user.getAuthorities()) {
                grantedAuthorities.add(new SimpleGrantedAuthority(authority.getRole().toString()));
            }
            return new CustomUserDetails(username, user.getPassword(), grantedAuthorities, user.getId());
        } else {
            throw new UsernameNotFoundException("User " + username + " was not found in the database");
        }
    }

    public static class CustomUserDetails extends org.springframework.security.core.userdetails.User {
        private Long id;
        public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, Long id) {
            this(username, password, authorities);
            this.id = id;
        }
        public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
            super(username, password, authorities);
        }
        public CustomUserDetails(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
            super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        }
        public Long getId() {
            return id;
        }
    }
}
