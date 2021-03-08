package vn.com.insee.corporate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.insee.corporate.common.status.ConstructionStatus;
import vn.com.insee.corporate.common.status.CustomerStatus;
import vn.com.insee.corporate.dto.response.admin.report.DashboardDTO;
import vn.com.insee.corporate.dto.response.admin.report.metric.ConstructionLocationMetricDTO;
import vn.com.insee.corporate.dto.response.admin.report.metric.CustomerDateMetricDTO;
import vn.com.insee.corporate.dto.response.admin.report.metric.CustomerLocationMetricDTO;
import vn.com.insee.corporate.mapper.Mapper;
import vn.com.insee.corporate.repository.ConstructionRepository;
import vn.com.insee.corporate.repository.CustomerRepository;
import vn.com.insee.corporate.repository.custom.metrics.ConstructionLocationMetric;
import vn.com.insee.corporate.repository.custom.metrics.CustomerDateMetric;
import vn.com.insee.corporate.repository.custom.metrics.CustomerLocationMetric;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatisticalService {

    @Autowired
    private Mapper mapper;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ConstructionRepository constructionRepository;

    public DashboardDTO getDashboard() {
        DashboardDTO dashboardDTO = new DashboardDTO();
        List<CustomerLocationMetric> customerLocationMetrics = customerRepository.statisticTotalCustomerByLocation();
        if (customerLocationMetrics != null) {
            dashboardDTO.setRegisterByLocation(customerLocationMetrics.stream().map(c -> mapper.map(c, CustomerLocationMetricDTO.class))
                    .collect(Collectors.toList()));
        }

        List<CustomerDateMetric> customerDateMetrics = customerRepository.statisticTotalCustomerByDate();
        if (customerDateMetrics != null) {
            dashboardDTO.setRegisterByDate(customerDateMetrics.stream().map(o -> mapper.map(o, CustomerDateMetricDTO.class))
                    .collect(Collectors.toList()));
        }
        long totalRegister = customerRepository.count();
        dashboardDTO.setTotalRegister((int) totalRegister);

        long totalWaitingRegister = customerRepository.countByStatus(CustomerStatus.REVIEWING.getStatus());
        dashboardDTO.setTotalWaitingRegister((int) totalWaitingRegister);

        List<ConstructionLocationMetric> constructionLocationMetrics = constructionRepository.statisticConstructionByLocation();
        if (constructionLocationMetrics != null) {
            dashboardDTO.setConstructionLocationMetric(constructionLocationMetrics.stream().map(o -> mapper.map(o, ConstructionLocationMetricDTO.class))
                    .collect(Collectors.toList()));
        }
        long totalWaitingConstruction = constructionRepository.countByStatus(ConstructionStatus.WAITING_APPROVAL.getStatus());
        dashboardDTO.setTotalWaitingConstruction((int) totalWaitingConstruction);

        return dashboardDTO;
    }
}
