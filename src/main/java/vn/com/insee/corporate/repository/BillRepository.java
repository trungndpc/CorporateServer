package vn.com.insee.corporate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.insee.corporate.entity.BillEntity;

import java.util.List;

public interface BillRepository extends JpaRepository<BillEntity, Integer> {
    List<BillEntity> findByLabelId(String labelId);
}
