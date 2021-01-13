package vn.com.insee.corporate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.insee.corporate.entity.GiftEntity;

import java.util.List;

public interface GiftRepository extends JpaRepository<GiftEntity, Integer> {
    List<GiftEntity> findByUserId(int userId);
}
