package vn.com.insee.corporate.dto.response.admin.report;

import com.fasterxml.jackson.annotation.JsonProperty;
import vn.com.insee.corporate.dto.response.admin.report.metric.ConstructionLocationMetricDTO;
import vn.com.insee.corporate.dto.response.admin.report.metric.CustomerDateMetricDTO;
import vn.com.insee.corporate.dto.response.admin.report.metric.CustomerLocationMetricDTO;

import java.util.List;

public class DashboardDTO {
    @JsonProperty(value = "total_register")
    private Integer totalRegister;

    @JsonProperty(value = "register_by_location")
    private List<CustomerLocationMetricDTO> registerByLocation;

    @JsonProperty(value = "register_by_date")
    private List<CustomerDateMetricDTO> registerByDate;

    @JsonProperty(value = "total_register_waiting")
    private Integer totalWaitingRegister;

    @JsonProperty(value = "total_construction_waiting")
    private Integer totalWaitingConstruction;

    @JsonProperty("construction_by_location")
    private List<ConstructionLocationMetricDTO> constructionLocationMetric;


    public Integer getTotalRegister() {
        return totalRegister;
    }

    public void setTotalRegister(Integer totalRegister) {
        this.totalRegister = totalRegister;
    }

    public List<CustomerLocationMetricDTO> getRegisterByLocation() {
        return registerByLocation;
    }

    public void setRegisterByLocation(List<CustomerLocationMetricDTO> registerByLocation) {
        this.registerByLocation = registerByLocation;
    }

    public List<CustomerDateMetricDTO> getRegisterByDate() {
        return registerByDate;
    }

    public void setRegisterByDate(List<CustomerDateMetricDTO> registerByDate) {
        this.registerByDate = registerByDate;
    }

    public Integer getTotalWaitingRegister() {
        return totalWaitingRegister;
    }

    public void setTotalWaitingRegister(Integer totalWaitingRegister) {
        this.totalWaitingRegister = totalWaitingRegister;
    }

    public Integer getTotalWaitingConstruction() {
        return totalWaitingConstruction;
    }

    public void setTotalWaitingConstruction(Integer totalWaitingConstruction) {
        this.totalWaitingConstruction = totalWaitingConstruction;
    }

    public List<ConstructionLocationMetricDTO> getConstructionLocationMetric() {
        return constructionLocationMetric;
    }

    public void setConstructionLocationMetric(List<ConstructionLocationMetricDTO> constructionLocationMetric) {
        this.constructionLocationMetric = constructionLocationMetric;
    }
}
