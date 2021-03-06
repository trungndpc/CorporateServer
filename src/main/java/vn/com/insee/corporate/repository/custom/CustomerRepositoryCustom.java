package vn.com.insee.corporate.repository.custom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.com.insee.corporate.entity.CustomerEntity;
import vn.com.insee.corporate.repository.custom.metrics.CustomerDateMetric;
import vn.com.insee.corporate.repository.custom.metrics.CustomerLocationMetric;

import java.util.List;

public interface CustomerRepositoryCustom {
    Page<CustomerEntity> getListByStatusAndLocationAndLinkedUser(Integer status, Integer location, Boolean isLinkedUser, Pageable pageable);
    List<CustomerLocationMetric> statisticTotalCustomerByLocation();
    List<CustomerDateMetric> statisticTotalCustomerByDate();
}
