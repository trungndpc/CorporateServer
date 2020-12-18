package vn.com.insee.corporate.common;

public enum  StatusEnum {
    INIT(1);

    StatusEnum(int status) {
        this.status = status;
    }

    private int status;

    public int getStatus() {
        return status;
    }
}
