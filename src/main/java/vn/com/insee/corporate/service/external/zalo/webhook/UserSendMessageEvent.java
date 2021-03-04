package vn.com.insee.corporate.service.external.zalo.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class UserSendMessageEvent extends ZEvent{

    private static class Message {
        public String text;

        @JsonProperty("msg_id")
        public String msgId;

        @JsonProperty("attachments")
        public List<Attachment> attachments;

    }
    private static class Attachment {
        public String type;
        public Payload payload;

        private static class Payload {
            public String thumbnail;
            public String url;
            public String description;
            public String id;
            public String type;
            public String checksum;

            private static class  Coordinates {
                public String latitude;
                public String longitude;
            }
        }

    }

}


