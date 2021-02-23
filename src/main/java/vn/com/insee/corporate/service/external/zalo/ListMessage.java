package vn.com.insee.corporate.service.external.zalo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ListMessage {

    private Recipient recipient;
    private Message message;

    public Recipient getRecipient() {
        return recipient;
    }

    public void setRecipient(Recipient recipient) {
        this.recipient = recipient;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public static class Message {
        private Attachment attachment;

        public Attachment getAttachment() {
            return attachment;
        }

        public void setAttachment(Attachment attachment) {
            this.attachment = attachment;
        }
    }

    public static class Attachment {
        private String type = "template";
        private Payload payload;

        public Attachment(Payload payload) {
            this.payload = payload;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Payload getPayload() {
            return payload;
        }

        public void setPayload(Payload payload) {
            this.payload = payload;
        }
    }

    public static class Payload {
        @JsonProperty("template_type")
        private String templateType = "list";
        private List<Element> elements;

        public Payload(List<Element> elements) {
            this.elements = elements;
        }

        public String getTemplateType() {
            return templateType;
        }

        public void setTemplateType(String templateType) {
            this.templateType = templateType;
        }

        public List<Element> getElements() {
            return elements;
        }

        public void setElements(List<Element> elements) {
            this.elements = elements;
        }
    }
    public static class Action {
        private String type;
        private String url;

        public Action(String type, String url) {
            this.type = type;
            this.url = url;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
    public static class Element {
        private String title;
        @JsonProperty("subtitle")
        private String subtitle;
        @JsonProperty("image_url")
        private String imageUrl;
        @JsonProperty("default_action")
        private Action  defaultAction;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public Action getDefaultAction() {
            return defaultAction;
        }

        public void setDefaultAction(Action defaultAction) {
            this.defaultAction = defaultAction;
        }
    }
}
