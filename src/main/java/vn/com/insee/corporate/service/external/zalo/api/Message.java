package vn.com.insee.corporate.service.external.zalo.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.List;

public class Message {
    public String text;
    public Attachment attachment;


    public static Message toTextMessage(String text) {
        Message message = new Message();
        message.text = text;
        return message;
    }

    public static Message toImageTemplate(String text, String url) {

        Attachment.Payload.Element element = new Attachment.Payload.Element();
        element.mediaType = "image";
        element.url = url;

        Attachment.Payload payload = new Attachment.Payload();
        payload.templateType = "media";
        payload.elements = Arrays.asList(element);

        Attachment attachment = new Attachment();
        attachment.type = "template";
        attachment.payload = payload;

        Message message = new Message();
        message.text = text;
        message.attachment = attachment;

        return message;
    }

    public static Message toListTemplate(List<Attachment.Payload.Element> elements) {
        Attachment.Payload payload = new Attachment.Payload();
        payload.templateType = "list";
        payload.elements = elements;

        Attachment attachment = new Attachment();
        attachment.type = "template";
        attachment.payload = payload;

        Message message = new Message();
        message.attachment = attachment;
        return message;
    }

    public static class Attachment {
        public String type;
        public Payload payload;

        public static class Payload {
            @JsonProperty("template_type")
            public String templateType;
            public List<Element> elements;
            public List<Button> buttons;

            public static class Element {
                @JsonProperty("media_type")
                public String mediaType;

                @JsonProperty("attachment_id")
                public String attachmentId;

                public Integer width;
                public Integer height;
                public String title;
                public String subtitle;
                @JsonProperty("image_url")
                public String imageUrl;

                @JsonProperty("url")
                public String url;

                @JsonProperty("default_action")
                public Action action;

                public static class Action {
                    public String type;
                    public String url;
                }

            }

            public static class Button {
                public String title;
                public String type;
                public Object payload;

                public static class ButtonPayload {
                    public String url;
                    public String content;
                    @JsonProperty("phone_code")
                    public String phoneCode;
                }
            }
        }
    }

}
