package com.somacode.celmybell.repository;

import com.somacode.celmybell.entity.oauth.OAuthAccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OAuthAccessTokenRepository extends JpaRepository<OAuthAccessToken, String> {

    void deleteByUserName_Id(Long employeeId);

}
