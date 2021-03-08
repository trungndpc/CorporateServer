package vn.com.insee.corporate.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.insee.corporate.entity.CustomerEntity;
import vn.com.insee.corporate.repository.custom.CustomerRepositoryCustom;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Integer>, CustomerRepositoryCustom {
    CustomerEntity findByPhone(String phone);
    Page<CustomerEntity> findByStatusAndIsLinkedUser(int status, boolean isLinkedUser, Pageable pageable);
    Page<CustomerEntity> findByIsLinkedUser(boolean isLinkedUser, Pageable pageable);
    CustomerEntity findByUserId(Integer userId);
    long countByStatus(int status);
}
