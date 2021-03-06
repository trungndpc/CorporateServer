package vn.com.insee.corporate.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import vn.com.insee.corporate.dto.response.admin.report.PromotionReportDTO;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PromotionDTO {
    private Integer id;
    private String title;
    private String content;
    private int typePromotion;
    private String summary;
    private Integer status;
    private List<Integer> location;
    private Long timeStart;
    private Long timeEnd;
    private Long updatedTime;
    private Integer ruleQuantily;
    private String cover;
    private List<Integer> ruleAcceptedCement;
    private PromotionReportDTO report;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getTypePromotion() {
        return typePromotion;
    }

    public void setTypePromotion(int typePromotion) {
        this.typePromotion = typePromotion;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Integer> getLocation() {
        return location;
    }

    public void setLocation(List<Integer> location) {
        this.location = location;
    }

    public Long getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Long timeStart) {
        this.timeStart = timeStart;
    }

    public Long getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Long timeEnd) {
        this.timeEnd = timeEnd;
    }

    public Long getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Long updatedTime) {
        this.updatedTime = updatedTime;
    }

    public PromotionReportDTO getReport() {
        return report;
    }

    public void setReport(PromotionReportDTO report) {
        this.report = report;
    }

    public Integer getRuleQuantily() {
        return ruleQuantily;
    }

    public void setRuleQuantily(Integer ruleQuantily) {
        this.ruleQuantily = ruleQuantily;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public List<Integer> getRuleAcceptedCement() {
        return ruleAcceptedCement;
    }

    public void setRuleAcceptedCement(List<Integer> ruleAcceptedCement) {
        this.ruleAcceptedCement = ruleAcceptedCement;
    }
}
