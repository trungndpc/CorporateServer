package vn.com.insee.corporate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.insee.corporate.entity.BillEntity;

public interface BillRepository extends JpaRepository<BillEntity, Integer> {
}
