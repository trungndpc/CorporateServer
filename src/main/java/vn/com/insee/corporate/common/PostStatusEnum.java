package vn.com.insee.corporate.common;

public enum  PostStatusEnum {
    INIT(1), EDITING(2), PUBLISHED(3), DELETED(4);

    PostStatusEnum(int status) {
        this.status = status;
    }

    private int status;

    public int getStatus() {
        return status;
    }
}
