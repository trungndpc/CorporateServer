package vn.com.insee.corporate.dto.response.admin;

import vn.com.insee.corporate.dto.response.CustomerDTO;
import vn.com.insee.corporate.dto.response.UserDTO;

public class HistoryConstructionPromotionDTO {
    private Integer id;
    private int city;
    private String district;
    private String address;
    private int status;
    private Long updatedTime;
    private CustomerDTO customer;
    private UserDTO user;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Long getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Long updatedTime) {
        this.updatedTime = updatedTime;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}
