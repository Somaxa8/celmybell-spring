package com.somacode.celmybell.service.tool;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class CriteriaTool {

    public static <T> Root<T> criteria(EntityManager entityManager, Class clazz) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> q = cb.createQuery(clazz);
        return q.from(clazz);
    }

    public static <T> PageImpl<T> page(EntityManager entityManager, CriteriaQuery<T> q, Integer page, Integer size) {
        TypedQuery<T> query = entityManager.createQuery(q);
        int total = query.getResultList().size();
        return new PageImpl<>(query.setFirstResult(page * size).setMaxResults(size).getResultList(), PageRequest.of(page, size), total);
    }

}
