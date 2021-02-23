package vn.com.insee.corporate.dto.response.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import vn.com.insee.corporate.dto.response.ConstructionDTO;
import vn.com.insee.corporate.dto.response.PromotionDTO;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class HistoryGiftCustomerDTO {
    private int id;
    private int type;
    private int status;
    private String name;
    private ConstructionDTO construction;
    private PromotionDTO promotion;
    private Long updatedTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ConstructionDTO getConstruction() {
        return construction;
    }

    public void setConstruction(ConstructionDTO construction) {
        this.construction = construction;
    }

    public PromotionDTO getPromotion() {
        return promotion;
    }

    public void setPromotion(PromotionDTO promotion) {
        this.promotion = promotion;
    }

    public Long getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Long updatedTime) {
        this.updatedTime = updatedTime;
    }
}
