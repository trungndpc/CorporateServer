package vn.com.insee.corporate.entity;

import javax.persistence.*;

@Entity
@Table(name = "customer", schema="promotion")
public class CustomerEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer birthday;
    private Integer mainAreaId;
    private String phone;
    private String fullName;
    private String avatar;
    private Integer status;
    private Integer userId;
    private Boolean isLinkedUser;
    private String note;
    private Integer volumeCiment;

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
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Boolean getLinkedUser() {
        return isLinkedUser;
    }

    public void setLinkedUser(Boolean linkedUser) {
        isLinkedUser = linkedUser;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getVolumeCiment() {
        return volumeCiment;
    }

    public void setVolumeCiment(Integer volumeCiment) {
        this.volumeCiment = volumeCiment;
    }
}
