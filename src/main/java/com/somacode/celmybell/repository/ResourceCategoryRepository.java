package com.somacode.celmybell.repository;

import com.somacode.celmybell.entity.ResourceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceCategoryRepository extends JpaRepository<ResourceCategory, Long> {
}
