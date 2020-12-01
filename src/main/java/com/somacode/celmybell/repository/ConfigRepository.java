package com.somacode.celmybell.repository;

import com.somacode.celmybell.entity.Config;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConfigRepository extends JpaRepository<Config, Long> {
    List<Config> findByTag(String tag);
    Config findByKey(String key);
}
