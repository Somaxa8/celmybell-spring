package com.somacode.celmybell.repository.criteria;


import com.somacode.celmybell.entity.Resource;
import com.somacode.celmybell.service.tool.CriteriaTool;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Repository
public class ResourceCriteria {

    @PersistenceContext
    EntityManager entityManager;


    public Page<Resource> findFilterPageable(Integer page, Integer size, String search) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Resource> q = cb.createQuery(Resource.class);
        Root<Resource> resource = q.from(Resource.class);
        q.select(resource).where();
        return CriteriaTool.page(entityManager, q, page, size);
    }

}
