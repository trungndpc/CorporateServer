package vn.com.insee.corporate.dto.response.admin;

import vn.com.insee.corporate.dto.response.ConstructionDTO;
import vn.com.insee.corporate.dto.response.CustomerDTO;
import vn.com.insee.corporate.dto.response.PromotionDTO;

public class HistoryGiftDTO {
    private int id;
    private Integer type;
    private String name;
    private String data;
    private int status;
    private Long createdTime;
    private Long updatedTime;
    private ConstructionDTO construction;
    private CustomerDTO customer;
    private PromotionDTO promotion;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ConstructionDTO getConstruction() {
        return construction;
    }

    public void setConstruction(ConstructionDTO construction) {
        this.construction = construction;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    public PromotionDTO getPromotion() {
        return promotion;
    }

    public void setPromotion(PromotionDTO promotion) {
        this.promotion = promotion;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public Long getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Long updatedTime) {
        this.updatedTime = updatedTime;
    }
}
