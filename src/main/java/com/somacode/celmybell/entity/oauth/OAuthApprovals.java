package com.somacode.celmybell.entity.oauth;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "oauth_approvals")
public class OAuthApprovals {

    @Id
    @GeneratedValue
    private Long id;
    private String userId;
    private String clientId;
    private String scope;
    private String status;
    private Timestamp expiresAt;
    private Timestamp lastModifiedAt;

}
