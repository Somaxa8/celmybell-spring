package com.somacode.celmybell.entity.oauth;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Blob;

@Entity
@Getter
@Setter
@Table(name = "oauth_code")
public class OAuthCode {

    @Id
    @GeneratedValue
    private Long id;
    private String code;
    @Lob
    private Blob authentication;

}
