package vn.com.insee.corporate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.insee.corporate.entity.LabelEntity;

import java.util.List;

public interface LabelRepository extends JpaRepository<LabelEntity, Integer> {
    List<LabelEntity> findByType(int type);
}
