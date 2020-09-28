package com.somacode.celmybell.entity.oauth;

import com.somacode.celmybell.entity.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Blob;

@Entity
@Getter
@Setter
@Table(name = "oauth_client_token")
public class OAuthClientToken {

    @Column(name = "token_id")
    private String tokenId;
    @Lob
    private Blob token;
    @Id
    @Column(name = "authentication_id")
    private String authenticationId;
    @OneToOne
    @JoinColumn(name = "user_name", referencedColumnName = "email")
    private User userName;
    @Column(name = "client_id")
    private String clientId;

}
