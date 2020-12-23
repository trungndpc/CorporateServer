package vn.com.insee.corporate.common;

public enum UserStatusEnum {
    INIT_FROM_ZALO(1),
    INIT_FROM_WEB(2);
    private Integer id;

    UserStatusEnum(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
