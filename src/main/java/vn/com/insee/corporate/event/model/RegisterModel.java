package vn.com.insee.corporate.event.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.com.insee.corporate.service.external.ZaloService;
import vn.com.insee.corporate.service.external.zalo.api.Message;

import java.util.Arrays;

@Component
public class RegisterModel {

    @Autowired
    private ZaloService zaloService;

    private static final String IMAGE_TO_REGISTER_MSG = "";
    private static final String TITLE_TO_REGISTER_MSG = "";
    private static final String DESC_TO_REGISTER_MSG = "";
    private static final String ICON_BUTTON_REGISTER_MSG = "";
    private static final String LABEL_BUTTON_REGISTER_MSG = "";
    private static final String LINK_TO_REGISTER = "";
    private static final String LINK_TO_SUB_ACTION = "";

    public boolean sendLinkToRegister(String follower) throws JsonProcessingException {
        Message.Attachment.Payload.Element.Action action = new Message.Attachment.Payload.Element.Action();
        action.type = "oa.open.url";
        action.url = LINK_TO_REGISTER;

        Message.Attachment.Payload.Element element = new Message.Attachment.Payload.Element();
        element.title = TITLE_TO_REGISTER_MSG;
        element.subtitle = DESC_TO_REGISTER_MSG;
        element.imageUrl = IMAGE_TO_REGISTER_MSG;
        element.action = action;

        Message.Attachment.Payload.Element.Action subAction = new Message.Attachment.Payload.Element.Action();
        subAction.type = "oa.open.url";
        action.url = LINK_TO_SUB_ACTION;

        Message.Attachment.Payload.Element subElement = new Message.Attachment.Payload.Element();
        subElement.title = LABEL_BUTTON_REGISTER_MSG;
        subElement.imageUrl = ICON_BUTTON_REGISTER_MSG;
        subElement.action = subAction;

        Message message = Message.toListTemplate(Arrays.asList(element, subElement));
        return zaloService.sendMessage(follower, message);
    }
}
