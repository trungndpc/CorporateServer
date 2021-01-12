package vn.com.insee.corporate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.insee.corporate.entity.LabelEntity;

public interface LabelRepository extends JpaRepository<LabelEntity, Integer> {
}
