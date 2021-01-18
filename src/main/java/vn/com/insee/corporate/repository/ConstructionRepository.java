package vn.com.insee.corporate.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.insee.corporate.entity.ConstructionEntity;

import java.util.List;

public interface ConstructionRepository extends JpaRepository<ConstructionEntity, Integer> {
    List<ConstructionEntity> findByCustomerIdAndPromotionId(int customerId, int promotionId);
    List<ConstructionEntity> findByCustomerId(int customerId);
    Page<ConstructionEntity> findByTypeAndStatus(int type, int status, Pageable pageable);
    Page<ConstructionEntity> findByType(int type, Pageable pageable);

}
