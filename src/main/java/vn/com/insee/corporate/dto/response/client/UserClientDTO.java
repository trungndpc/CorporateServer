package vn.com.insee.corporate.dto.response.client;

public class UserClientDTO {
    private Integer id;
    private String phone;
    private String avatar;
    private String name;
    private Integer customerId;
    private Integer roleId;
    private String note;
    private Integer birthday;
    private boolean isFollower;
    private CustomerUserClientDTO customer;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public CustomerUserClientDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerUserClientDTO customer) {
        this.customer = customer;
    }

    public Integer getBirthday() {
        return birthday;
    }

    public void setBirthday(Integer birthday) {
        this.birthday = birthday;
    }

    public boolean isFollower() {
        return isFollower;
    }

    public void setFollower(boolean follower) {
        isFollower = follower;
    }

    public static class CustomerUserClientDTO {
        private Integer id;
        private Integer birthday;
        private String fullName;
        private Integer mainAreaId;
        private String phone;
        private Integer userId;
        private Integer volumeCiment;
        private Integer status;

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

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
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

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public Integer getVolumeCiment() {
            return volumeCiment;
        }

        public void setVolumeCiment(Integer volumeCiment) {
            this.volumeCiment = volumeCiment;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }
    }



}
