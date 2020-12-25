package vn.com.insee.corporate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.insee.corporate.entity.ImageEntity;

public interface ImageRepository extends JpaRepository<ImageEntity, Integer> {
}
