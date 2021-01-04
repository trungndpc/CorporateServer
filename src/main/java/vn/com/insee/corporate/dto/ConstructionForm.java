package vn.com.insee.corporate.dto;

import org.hibernate.annotations.Type;

import javax.persistence.Column;
import java.util.List;

public class ConstructionForm {
    private Integer id;
    private int city;
    private String district;
    private String name;
    private String phone;
    private int quantity;
    private List<String> billIds;
    private List<String> imageIds;
    private int estimateTimeStart;
    private int typeConstruction;
    private int type;
    private int userId;
    private int status;
    private String address;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getCity() {
        return city;
    }

    public void setCity(int city) {
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
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

    public int getEstimateTimeStart() {
        return estimateTimeStart;
    }

    public void setEstimateTimeStart(int estimateTimeStart) {
        this.estimateTimeStart = estimateTimeStart;
    }

    public int getTypeConstruction() {
        return typeConstruction;
    }

    public void setTypeConstruction(int typeConstruction) {
        this.typeConstruction = typeConstruction;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
