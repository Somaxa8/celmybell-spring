package com.somacode.celmybell.entity.oauth;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Blob;

@Entity
@Getter
@Setter
@Table(name = "oauth_refresh_token")
public class OAuthRefreshToken {

    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "token_id")
    private String tokenId;
    @Lob
    private Blob token;
    @Lob
    private Blob authentication;

}
