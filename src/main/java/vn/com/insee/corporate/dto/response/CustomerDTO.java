package vn.com.insee.corporate.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import vn.com.insee.corporate.common.dto.CustomerDTOStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerDTO {
    private Integer id;
    private Integer birthday;
    private Integer mainAreaId;
    private String phone;
    private String pass;
    private String fullName;
    private String avatar;
    private Integer userId;
    private Integer finalStatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBirthday() {
        return birthday;
    }

    public void setBirthday(Integer birthday) {
        this.birthday = birthday;
    }

    public Integer getMainAreaId() {
        return mainAreaId;
    }

    public void setMainAreaId(Integer mainAreaId) {
        this.mainAreaId = mainAreaId;
    }

    public String getPhone() {
        if(phone != null) {
            phone = phone.replace("+", "");
        }
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getFinalStatus() {
        return finalStatus;
    }

    public void setFinalStatus(Integer finalStatus) {
        this.finalStatus = finalStatus;
    }
}


