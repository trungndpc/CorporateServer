package vn.com.insee.corporate.repository.custom.implement;

import org.springframework.stereotype.Repository;
import vn.com.insee.corporate.entity.ConstructionEntity;
import vn.com.insee.corporate.entity.ConstructionEntity_;
import vn.com.insee.corporate.entity.CustomerEntity;
import vn.com.insee.corporate.repository.custom.ConstructionRepositoryCustom;
import vn.com.insee.corporate.repository.custom.metrics.ConstructionLocationMetric;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ConstructionRepositoryImpl implements ConstructionRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<ConstructionLocationMetric> statisticConstructionByLocation() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
        Root<ConstructionEntity> root = query.from(ConstructionEntity.class);
        query.groupBy(root.get(ConstructionEntity_.city));
        query.multiselect(root.get(ConstructionEntity_.city), cb.count(root.get(ConstructionEntity_.id)));
        List<Object[]> resultList = entityManager.createQuery(query).getResultList();
        return resultList.stream().map(r -> new ConstructionLocationMetric(Integer.parseInt(r[0].toString()),
                Integer.parseInt(r[1].toString()))).collect(Collectors.toList());
    }
}
