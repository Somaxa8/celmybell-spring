package com.somacode.celmybell.repository;

import com.somacode.celmybell.entity.oauth.OAuthClientDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OAuthClientDetailsRepository extends JpaRepository<OAuthClientDetails, String> {
}
