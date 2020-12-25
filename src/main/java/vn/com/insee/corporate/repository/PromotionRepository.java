package vn.com.insee.corporate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.insee.corporate.entity.PromotionEntity;

public interface PromotionRepository extends JpaRepository<PromotionEntity, Integer> {
}
