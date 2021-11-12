package com.somacode.celmybell.repository.criteria;


import com.somacode.celmybell.entity.Document;
import com.somacode.celmybell.entity.Document_;
import com.somacode.celmybell.entity.Resource;
import com.somacode.celmybell.entity.Resource_;
import com.somacode.celmybell.service.tool.CriteriaTool;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ResourceCriteria {

    @PersistenceContext
    EntityManager entityManager;


    public Page<Resource> findFilterPageable(Integer page, Integer size, Document.Type documentType, String search) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Resource> q = cb.createQuery(Resource.class);
        Root<Resource> resource = q.from(Resource.class);

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(resource.join(Resource_.document).get(Document_.type), documentType));

        if (search != null && !search.isEmpty()) {
            String word = search.trim().toLowerCase();
            String like = "%"+word+"%";
            predicates.add(
                    cb.or(
                            cb.like(cb.lower(resource.get(Resource_.title)), like),
                            cb.like(cb.lower(resource.get(Resource_.description)), like)
                    )
            );
        }

        q.select(resource).where(
                predicates.toArray(new Predicate[0])
        );
        return CriteriaTool.page(entityManager, q, page, size);
    }

}
