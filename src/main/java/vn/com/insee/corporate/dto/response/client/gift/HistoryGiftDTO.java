package vn.com.insee.corporate.dto.response.client.gift;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class HistoryGiftDTO {
    private int id;
    private Integer type;
    private int status;
    private String name;
    private HistoryGiftConstructionDTO construction;
    private HistoryGiftPromotionDTO promotion;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
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

    public HistoryGiftConstructionDTO getConstruction() {
        return construction;
    }

    public void setConstruction(HistoryGiftConstructionDTO construction) {
        this.construction = construction;
    }

    public HistoryGiftPromotionDTO getPromotion() {
        return promotion;
    }

    public void setPromotion(HistoryGiftPromotionDTO promotion) {
        this.promotion = promotion;
    }
}
