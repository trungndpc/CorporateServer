package vn.com.insee.corporate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.insee.corporate.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    UserEntity findByPhone(String phone);
    UserEntity findByZaloId(String zaloId);
    UserEntity findByFollowerZaloId(String followerId);
}
