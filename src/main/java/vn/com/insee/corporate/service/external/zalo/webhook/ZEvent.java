package vn.com.insee.corporate.service.external.zalo.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class ZEvent {
    @JsonProperty("app_id")
    public long appId;

    @JsonProperty("oa_id")
    public long oaId;

    @JsonProperty("user_id_by_app")
    public long userIdByApp;

    @JsonProperty("event_name")
    public String eventName;

    @JsonProperty("timestamp")
    public int timestamp;

    @JsonProperty("sender")
    public Sender sender;

    @JsonProperty("recipient")
    public Recipient recipient;

    public static class Sender {
        public String id;
    }

    public static class Recipient {
        public String id;
    }

}
