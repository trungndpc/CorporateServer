package vn.com.insee.corporate.service.external.zalo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Recipient {
    @JsonProperty("user_id")
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
