package vn.com.insee.corporate.service.external.zalo;

public class Recipient {
    private String userId;

    public Recipient(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
