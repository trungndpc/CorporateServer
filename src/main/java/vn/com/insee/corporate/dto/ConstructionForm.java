package vn.com.insee.corporate.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.annotations.Type;
import org.json.JSONObject;
import vn.com.insee.corporate.dto.response.ext.ExtraDTO;

import javax.persistence.Column;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConstructionForm {
    private Integer id;
    private Integer city;
    private String district;
    private String name;
    private String phone;
    private Integer quantity;
    private List<String> billIds;
    private List<String> imageIds;
    private Integer estimateTimeStart;
    private Integer typeConstruction;
    private Integer type;
    private Integer status;
    private String address;
    private int promotionId;
    private ExtraDTO extra;
    private Integer cement;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCity() {
        return city;
    }

    public void setCity(Integer city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public List<String> getBillIds() {
        return billIds;
    }

    public void setBillIds(List<String> billIds) {
        this.billIds = billIds;
    }

    public List<String> getImageIds() {
        return imageIds;
    }

    public void setImageIds(List<String> imageIds) {
        this.imageIds = imageIds;
    }

    public Integer getEstimateTimeStart() {
        return estimateTimeStart;
    }

    public void setEstimateTimeStart(Integer estimateTimeStart) {
        this.estimateTimeStart = estimateTimeStart;
    }

    public Integer getTypeConstruction() {
        return typeConstruction;
    }

    public void setTypeConstruction(Integer typeConstruction) {
        this.typeConstruction = typeConstruction;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(int promotionId) {
        this.promotionId = promotionId;
    }

    public ExtraDTO getExtra() {
        return extra;
    }

    public void setExtra(ExtraDTO extra) {
        this.extra = extra;
    }

    public Integer getCement() {
        return cement;
    }

    public void setCement(Integer cement) {
        this.cement = cement;
    }
}
