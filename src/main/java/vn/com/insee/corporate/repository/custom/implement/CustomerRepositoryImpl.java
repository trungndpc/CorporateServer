package vn.com.insee.corporate.repository.custom.implement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import vn.com.insee.corporate.entity.CustomerEntity;
import vn.com.insee.corporate.entity.CustomerEntity_;
import vn.com.insee.corporate.repository.custom.CustomerRepositoryCustom;
import vn.com.insee.corporate.repository.custom.metrics.CustomerDateMetric;
import vn.com.insee.corporate.repository.custom.metrics.CustomerLocationMetric;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CustomerRepositoryImpl implements CustomerRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<CustomerLocationMetric> statisticTotalCustomerByLocation() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
        Root<CustomerEntity> root = query.from(CustomerEntity.class);
        query.groupBy(root.get(CustomerEntity_.mainAreaId));
        query.multiselect(root.get(CustomerEntity_.mainAreaId), cb.count(root.get(CustomerEntity_.id)));
        List<Object[]> resultList = entityManager.createQuery(query).getResultList();
        return resultList.stream().map(r -> new CustomerLocationMetric(Integer.parseInt(r[0].toString()),
                Integer.parseInt(r[1].toString()))).collect(Collectors.toList());
    }

    @Override
    public List<CustomerDateMetric> statisticTotalCustomerByDate() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
        Root<CustomerEntity> root = query.from(CustomerEntity.class);
        Expression<Date> dateExpression = root.get(CustomerEntity_.createdTime).as(Date.class);
        query.groupBy(dateExpression);
        query.orderBy(cb.desc(dateExpression));
        query.multiselect(dateExpression, cb.count(root.get(CustomerEntity_.id)));
        List<Object[]> resultList = entityManager.createQuery(query).getResultList();
        return resultList.stream().map(r -> new CustomerDateMetric(r[0].toString(), Integer.parseInt(r[1].toString())))
                .collect(Collectors.toList());
    }


    @Override
    public Page<CustomerEntity> getListByStatusAndLocationAndLinkedUser(Integer status, Integer location, Boolean isLinkedUser, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<CustomerEntity> cq =cb.createQuery(CustomerEntity.class);
        Root<CustomerEntity> root = cq.from(CustomerEntity.class);
        List<Sort.Order> sortList = pageable.getSort().get().collect(Collectors.toList());
        String property = sortList.get(0).getProperty();
        if (sortList.get(0).isAscending()) {
            cq.orderBy(cb.asc(root.get(property)));
        }else {
            cq.orderBy(cb.desc(root.get(property)));
        }
        List<Predicate> predicates = getListPredicate(cb, root, status, location, isLinkedUser);
        cq.where(predicates.toArray(new Predicate[0]));
        TypedQuery<CustomerEntity> typedQuery = entityManager.createQuery(cq);
        typedQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        typedQuery.setMaxResults(pageable.getPageSize());
        return new PageImpl<>(typedQuery.getResultList(), pageable, countListByStatusAndLocationAndLinkedUser(status, location, isLinkedUser));
    }

    private List<Predicate> getListPredicate(CriteriaBuilder cb, Root<CustomerEntity> root, Integer status, Integer location, Boolean isLinkedUser) {
        List<Predicate> predicates = new ArrayList<>();
        if (status != null) {
            predicates.add(cb.equal(root.get("status"), status));
        }
        if (location != null) {
            predicates.add(cb.equal(root.get("mainAreaId"), location));
        }
        if (isLinkedUser != null) {
            predicates.add(cb.equal(root.get("isLinkedUser"), isLinkedUser));
        }
        return predicates;
    }

    private long countListByStatusAndLocationAndLinkedUser(Integer status, Integer location, Boolean isLinkedUser) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<CustomerEntity> root = cq.from(CustomerEntity.class);
        List<Predicate> predicates = getListPredicate(cb, root, status, location, isLinkedUser);
        cq.select(cb.count(root)).where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(cq).getSingleResult();
    }
}
