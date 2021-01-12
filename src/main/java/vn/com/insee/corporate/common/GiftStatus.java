package vn.com.insee.corporate.common;

public enum GiftStatus {
    SEND(1), RECEIVED(2);
    private int status;

    GiftStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
