package com.somacode.celmybell.service.tool;

import com.somacode.celmybell.entity.Authority;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TokenTool {

    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 12;
    private static final String TOKEN_ID = "somacode";

    public String getToken(String username) {
        String secretKey = "mySecretKey";
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils.
                commaSeparatedStringToAuthorityList(Authority.Name.ROLE_USER.toString());

        String token = Jwts
                .builder()
                .setId(TOKEN_ID)
                .setSubject(username)
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, secretKey.getBytes())
                .compact();

        return token;
    }
}
