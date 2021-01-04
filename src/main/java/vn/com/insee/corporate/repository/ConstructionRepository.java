package vn.com.insee.corporate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.insee.corporate.entity.ConstructionEntity;
import vn.com.insee.corporate.entity.CustomerEntity;

import java.util.List;

public interface ConstructionRepository extends JpaRepository<ConstructionEntity, Integer> {
    List<ConstructionEntity> findByUserId(int uid);
}
