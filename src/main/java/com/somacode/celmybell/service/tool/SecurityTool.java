package com.somacode.celmybell.service.tool;

import com.somacode.celmybell.config.exception.UnauthorizedException;
import com.somacode.celmybell.entity.Authority;
import com.somacode.celmybell.security.CustomUserDetailsService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class SecurityTool {


    public boolean isUser(Long id) {
        return id.longValue() == getCustomUserDetails().getId().longValue();
    }

    public boolean isAuthenticated() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            return false;
        }
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof CustomUserDetailsService.CustomUserDetails;
    }

    public Long getUserId() {
        if (!isAuthenticated()) {
            throw new UnauthorizedException();
        }
        return getCustomUserDetails().getId();
    }

    public static Collection<? extends GrantedAuthority> getAllAuthorities() {
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        for (Authority.Name authority : Authority.Name.values()) {
            authorities.add(new SimpleGrantedAuthority(authority.toString()));
        }
        return authorities;
    }

    private CustomUserDetailsService.CustomUserDetails getCustomUserDetails() {
        return ((CustomUserDetailsService.CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

}
