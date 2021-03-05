package vn.com.insee.corporate.event.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.com.insee.corporate.service.external.ZaloService;
import vn.com.insee.corporate.service.external.zalo.api.Message;

import java.util.Arrays;

@Component
public class QuestionJoinPromotionModel {
    private static final String TEXT_AFTER_SUCCESS_REGISTER_MSG = "";
    private static final String LABEL_BUTTON_REGISTER_MSG = "Upload hóa đơn";

    @Autowired
    private ZaloService zaloService;

    public void sendAfterSuccessRegister(String followerId) throws JsonProcessingException {
        Message.Attachment.Payload.Button button = new Message.Attachment.Payload.Button();
        button.type = "oa.query.hide";
        button.title = LABEL_BUTTON_REGISTER_MSG;
        button.payload = "#upload_bill_promotion";

        Message.Attachment.Payload payload = new Message.Attachment.Payload();
        payload.buttons = Arrays.asList(button);

        Message.Attachment attachment = new Message.Attachment();
        attachment.type = "template";
        attachment.payload = payload;

        Message message = new Message();
        message.text = TEXT_AFTER_SUCCESS_REGISTER_MSG;
        message.attachment = attachment;

        zaloService.sendMessage(followerId, message);
    }

//    public void questionTo


}
