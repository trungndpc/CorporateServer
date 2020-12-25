package vn.com.insee.corporate.common;

public enum ImageStatus {
    WAITING_APPROVAL(1), APPROVED(2), REJECT(3) ;
    private int status;

    ImageStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
